package cn.notemi.core.security.provider;

import cn.notemi.core.security.TokenService;
import cn.notemi.core.security.externalservice.AuthenticationWithToken;
import cn.notemi.core.security.externalservice.UserServiceAuthenticator;
import cn.notemi.model.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Title：PasswordAuthenticationProvider
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/
@Component
public class PasswordAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    TokenService tokenService;
    @Autowired
    UserServiceAuthenticator userServiceAuthenticator;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        Optional<String> username = (Optional) authentication.getPrincipal();
        Optional<String> password = (Optional) authentication.getCredentials();
        Boolean isAdminLogin = (Boolean) authentication.getDetails();

        if (!username.isPresent() || !password.isPresent()) {
            throw new BadCredentialsException("Invalid user credentials");
        }

        AuthenticationWithToken authResult = userServiceAuthenticator
            .authenticate(username.get(), password.get(), isAdminLogin);
        String newToken = tokenService.generateToken((Account) authResult.getPrincipal());
        authResult.setToken(newToken);
        tokenService.store(newToken, authResult);
        return authResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
