package cn.notemi.core.security;

import cn.notemi.common.DefaultErrorResult;
import cn.notemi.common.ErrorModel;
import cn.notemi.common.HeaderConstants;
import cn.notemi.constant.ResultCode;
import cn.notemi.controller.APIController;
import cn.notemi.utils.JacksonUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Title：AuthenticationFilter
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/
public class AuthenticationFilter extends RequestHeaderAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String TOKEN_SESSION_KEY = "TOKEN_SESSION";
    private static final String USER_SESSION_KEY = "USER_SESSION";

    private AuthenticationManager authenticationManager;

    AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Optional<String> username = Optional.ofNullable(httpRequest.getHeader(HeaderConstants.X_AUTH_USERNAME));
        Optional<String> password = Optional.ofNullable(httpRequest.getHeader(HeaderConstants.X_AUTH_PASSWORD));
        Optional<String> token = Optional.ofNullable(httpRequest.getHeader(HeaderConstants.X_AUTH_TOKEN));
        Optional<String> wechatApp = Optional.ofNullable(httpRequest.getHeader(HeaderConstants.X_AUTH_WECHAT_APP));

        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);
        logger.info("request method: {}  url: {}", httpRequest.getMethod(), resourcePath);
        try {
            if (postToAuthentication(httpRequest, resourcePath)) {
                logger.debug("Trying to authenticate user {} by x-auth-username and x-auth-password", username.get());
                this.processPasswordAuthentication(username, password);
            } else if (wechatApp.isPresent()) {
                logger.debug("Trying to authenticate by x-auth-wechat-app openid: {}", wechatApp.get());
                this.processWebChatAppAuthentication(wechatApp);
            } else {
                logger.debug("Trying to authenticate user by x-auth-token: {}", token.get());
                this.processTokenAuthentication(token);
            }
            logger.debug("AuthenticationFilter is passing request down to the filter chain");
            this.addSessionContentToLogging();
            chain.doFilter(request, response);
        } catch (BadCredentialsException badCredentialsException) {
            logger.error("Bad credentials exception", badCredentialsException);
            DefaultErrorResult errorResult = DefaultErrorResult.failure(ResultCode.USER_LOGIN_ERROR, badCredentialsException, HttpStatus.UNAUTHORIZED);
            this.errorHandle(httpResponse, errorResult);
        } catch (InternalAuthenticationServiceException internalAuthException) {
            logger.error("Internal authentication service exception", internalAuthException);
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (AuthenticationException authenticationException) {
            logger.error("Authentication exception", authenticationException);
            DefaultErrorResult errorResult = DefaultErrorResult.failure(ResultCode.USER_LOGIN_ERROR, authenticationException, HttpStatus.UNAUTHORIZED);
            this.errorHandle(httpResponse, errorResult);
        } catch (NoSuchElementException noSuchElementException) {
            logger.error("Authentication exception", noSuchElementException);
            DefaultErrorResult errorResult = DefaultErrorResult.failure(ResultCode.USER_TOKEN_NOT_EXIST, noSuchElementException, HttpStatus.UNAUTHORIZED);
            this.errorHandle(httpResponse, errorResult);
        } finally {
            MDC.remove(TOKEN_SESSION_KEY);
            MDC.remove(USER_SESSION_KEY);
        }
    }

    private void errorHandle(HttpServletResponse httpResponse, DefaultErrorResult errorResult) throws IOException {
        SecurityContextHolder.clearContext();
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.addHeader("Content-Type", "application/json");
        httpResponse.getWriter().printf(JacksonUtil.toJSon(errorResult));
    }
    private Boolean postToAuthentication(HttpServletRequest httpRequest, String resourcePath) {
        return resourcePath.toLowerCase().contains(APIController.AUTH_LOCAL_URL)
            && httpRequest.getMethod().equals("POST");
    }

    private void addSessionContentToLogging() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenValue = "EMPTY";
        if (authentication != null && authentication.getDetails() != null
            && !Strings.isNullOrEmpty(authentication.getDetails().toString())) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            tokenValue = encoder.encode(authentication.getDetails().toString());
        }
        MDC.put(TOKEN_SESSION_KEY, tokenValue);

        String userValue = "EMPTY";
        if (authentication != null && authentication.getPrincipal() != null
            && !Strings.isNullOrEmpty(authentication.getPrincipal().toString())) {
            userValue = authentication.getPrincipal().toString();
        }
        MDC.put(USER_SESSION_KEY, userValue);
    }

    private void processPasswordAuthentication(Optional<String> username, Optional<String> password) {
        Authentication authResult = tryAuthenticateWithPassword(username, password);
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    private Authentication tryAuthenticateWithPassword(Optional<String> username, Optional<String> password) {
        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        return tryAuthenticate(requestAuthentication);
    }

    private void processTokenAuthentication(Optional<String> token) {
        Authentication authResult = tryAuthenticateWithToken(token);
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    private Authentication tryAuthenticateWithToken(Optional<String> token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        return tryAuthenticate(requestAuthentication);
    }

    // 自定义wechat
    private void processWebChatAppAuthentication(Optional<String> wechatApp) {
        Authentication authResult = tryWeChatAppAuthenticate(wechatApp);
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    private Authentication tryWeChatAppAuthenticate(Optional<String> wechatApp) {
        WeChatAppAuthentication requestAuthentication = new WeChatAppAuthentication(wechatApp);
        return tryAuthenticate(requestAuthentication);
    }

    private Authentication tryAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate user for provided credentials");
        }
        logger.debug("Account successfully authenticated");
        return responseAuthentication;
    }
}
