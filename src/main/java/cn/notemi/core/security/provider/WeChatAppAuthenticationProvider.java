package cn.notemi.core.security.provider;

import cn.notemi.core.security.TokenService;
import cn.notemi.core.security.WeChatAppAuthentication;
import cn.notemi.core.security.externalservice.AuthenticationWithToken;
import cn.notemi.core.security.externalservice.WeChatAppAuthenticator;
import cn.notemi.model.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Title：WeChatAppAuthenticationProvider
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/
@Component
public class WeChatAppAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    TokenService tokenService;
    @Autowired
    WeChatAppAuthenticator weChatAppAuthenticator;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        Optional<String> wechatApp = (Optional) authentication.getPrincipal();

        AuthenticationWithToken authResult = weChatAppAuthenticator
            .authenticate(wechatApp.get(), null, null);
        if (authResult.getCredentials() == null) {
            String newToken = tokenService.generateToken((Account) authResult.getPrincipal());
            authResult.setToken(newToken);
            tokenService.store(newToken, authResult);
        }
        return authResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(WeChatAppAuthentication.class);
    }
}
