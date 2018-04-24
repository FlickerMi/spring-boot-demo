package cn.notemi.service;

import cn.notemi.constant.AccountStatus;
import cn.notemi.model.entity.Account;
import cn.notemi.model.repository.AccountRepository;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Autowired
    AccountRepository accountRepository;

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

    public List<Account> findAllAccount(String username) {
        if (username != null){
            logger.info("Find all account form by user name: {}", username);
            return accountRepository.findAllByUsername(username);
        }
        logger.info("Find all account form");
        return accountRepository.findAll();
    }

    public Account findByAccountId(Long accountId) {
        logger.info("Find account form by id: {}", accountId);
        return accountRepository.findById(accountId).get();
    }
}
