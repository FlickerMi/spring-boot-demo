package cn.notemi.core.security.externalservice;

/**
 * Title：ExternalServiceAuthenticator
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/
public interface ExternalServiceAuthenticator {
    AuthenticationWithToken authenticate(String username, String password, Boolean isAdmin);
}
