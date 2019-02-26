package cn.notemi.core.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Title：WeChatAppAuthentication
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/
public class WeChatAppAuthentication extends AbstractAuthenticationToken {

    private final Object code;

    public WeChatAppAuthentication(Object code) {
        super(null);
        this.code = code;
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return code;
    }
}
