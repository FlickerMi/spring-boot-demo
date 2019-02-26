package cn.notemi.core.security.externalservice;

import cn.notemi.model.entity.Account;
import cn.notemi.service.AccountService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Title：WeChatAppAuthenticator
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/
@Component
public class WeChatAppAuthenticator implements ExternalServiceAuthenticator {

    @Autowired
    AccountService accountService;

    @SuppressWarnings("deprecation")
    @Override
    public AuthenticationWithToken authenticate(String wechatApp, String password, Boolean isAdmin) {
        String openId = accountService.getWeChatAppOpenId(wechatApp);
        Account account = accountService.findAccountByOpenId(openId);
        if (account == null) {
            return new AuthenticationWithToken(null, openId, null);
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
