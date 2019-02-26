package cn.notemi.service;

import cn.notemi.common.MessageService;
import cn.notemi.constant.AccountStatus;
import cn.notemi.core.security.externalservice.AuthenticationWithToken;
import cn.notemi.model.entity.Account;
import cn.notemi.model.repository.AccountRepository;
import cn.notemi.model.request.AuthenticatedAccount;
import cn.notemi.utils.JacksonUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Title：AccountService
 *
 * @author Flicker
 * @create 2018/4/18 0018 11:06
 **/
@Component
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private static final BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder(8);

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MessageService messageService;

    public Account createAccount(Account account) {
        logger.info("Creating new account form: {}", account.toString());
        return accountRepository.save(account);
    }

    public void deleteByAccountId(Long accountId) {
        logger.info("Delete account form by id: {}", accountId);
        Account accountActivate = accountRepository.findById(accountId).get();
        accountActivate.setStatus(AccountStatus.INACTIVE);
        accountRepository.save(accountActivate);
    }

    public Account updateAccount(Account account) {
        logger.info("Update account form for: {}", account.getId());
        Account accountInDB = accountRepository.findById(account.getId()).get();
        Preconditions.checkArgument(accountInDB != null, "修改的用户不存在!");
        accountInDB.merge(account);
        return accountRepository.save(accountInDB);
    }

    public List<Account> findAllAccount(AccountStatus status) {
        logger.info("Find all account form by status: {}", status);
        if (status != null){
            return accountRepository.findAllByStatus(status);
        }
        return accountRepository.findAll();
    }

    public Account findByAccountId(Long accountId) {
        logger.info("Find account form by id: {}", accountId);
        return accountRepository.findById(accountId).get();
    }

    public Account getCurrentLoginAccount() {
        AuthenticationWithToken authentication = (AuthenticationWithToken) SecurityContextHolder.getContext().getAuthentication();
        return (Account) authentication.getPrincipal();
    }

    public AuthenticatedAccount loginLocal() {
        logger.info("Login local.");
        AuthenticationWithToken authentication = (AuthenticationWithToken) SecurityContextHolder.getContext().getAuthentication();
        Account account = this.getCurrentLoginAccount();
        String token = (String) authentication.getDetails();
        if (account == null || StringUtils.isEmpty(token)) {
            throw new BadCredentialsException(messageService.getMessage("user.login.invalid.credential"));
        }
        return new AuthenticatedAccount(account, token);
    }

    public Account authenticate(String username, String password) {
        logger.info("Trying to authenticate account: {}", username);
        Account usernameAccount = accountRepository.findOneByUsername(username);
        if (usernameAccount != null && pwdEncoder.matches(password, usernameAccount.getPassword())) {
            logger.info("Authenticated user name {}.", username);
            this.validationAccountStatus(usernameAccount.getStatus());
            return usernameAccount;
        }
        logger.warn("Authenticate user name {} failed.", username);
        return null;
    }

    public Account findMyLoginProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        if (account != null) {
            return accountRepository.findById(account.getId()).orElse(null);
        }
        return null;
    }

    public String getWeChatAppOpenId(String jscode) {
        // 用jscode获取openId
        return "";
    }

    public Account findAccountByOpenId(String openId) {
        return accountRepository.findOneByWechatOpenId(openId);
    }

    private void validationAccountStatus(AccountStatus status) {
        if(status != AccountStatus.ACTIVE){
            if (status == AccountStatus.INACTIVE){
                throw new BadCredentialsException("该账号已被禁用，请联系系统管理员");
            }else {
                throw new BadCredentialsException("账号还未激活，不能登录");
            }
        }
    }
}
