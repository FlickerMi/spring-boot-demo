package cn.notemi.core.security.externalservice;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

/**
 * Title：AuthenticationWithToken
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/

public class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken {
    public AuthenticationWithToken(Object aPrincipal, Object aCredentials) {
        super(aPrincipal, aCredentials);
    }

    public AuthenticationWithToken(Object aPrincipal, Object aCredentials, Collection<? extends GrantedAuthority> anAuthorities) {
        super(aPrincipal, aCredentials, anAuthorities);
    }

    public void setToken(String token) {
        setDetails(token);
    }

    public String getToken() {
        return (String) getDetails();
    }
}
