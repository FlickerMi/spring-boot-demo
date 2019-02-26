package cn.notemi.core.security.provider;

import cn.notemi.common.MessageService;
import cn.notemi.core.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Title：TokenAuthenticationProvider
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    TokenService tokenService;
    @Autowired
    MessageService messageService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> token = (Optional) authentication.getPrincipal();
        if (!token.isPresent() || token.get().isEmpty()) {
            throw new BadCredentialsException(messageService.getMessage("user.login.null.token"));
        }
        if (!tokenService.contains(token.get())) {
            throw new BadCredentialsException(messageService.getMessage("user.login.invalid.token"));
        }
        return tokenService.retrieve(token.get());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}
