package cn.notemi.core.security.externalservice;

import cn.notemi.common.MessageService;
import cn.notemi.model.entity.Account;
import cn.notemi.service.AccountService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Title：UserServiceAuthenticator
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/
@Component
public class UserServiceAuthenticator implements ExternalServiceAuthenticator {

    @Autowired
    AccountService accountService;
    @Autowired
    MessageService messageService;

    @Override
    public AuthenticationWithToken authenticate(String username, String password, Boolean isAdmin) {
        Account account = accountService.authenticate(username, password);
        //String authorities = this.getAuthorityString(account.getRole().getOperations());
        if (account == null) {
            throw new BadCredentialsException(messageService.getMessage("user.login.invalid.credential"));
        }
        String authorities = this.getAuthorityString(Lists.newArrayList());
        return new AuthenticationWithToken(account, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
    }

    private String getAuthorityString(List<String> operations) {
        if (!CollectionUtils.isEmpty(operations)) {
            return operations.stream()
                .collect(Collectors.joining(","));
        }
        return "";
    }
}
