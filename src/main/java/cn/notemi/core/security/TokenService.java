package cn.notemi.core.security;

import cn.notemi.core.exception.BusinessException;
import cn.notemi.model.entity.Account;
import cn.notemi.utils.EncryptionUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Title：TokenService
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/
@Component
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private static final int HALF_HOUR_IN_MILLISECONDS = 30 * 60 * 1000;
    private static final long TOKEN_EXPIRE_DATE_SECONDS = 7 * 24 * 60 * 60;

    @Resource
    RedisTemplate<String, Authentication> redisTemplate;
    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(Account account) {
        return account.getId() + "-" + EncryptionUtil.md5(String.valueOf(account.getId()));
    }

    public void store(String token, Authentication authentication) {
        redisTemplate.opsForValue().set(token, authentication, TOKEN_EXPIRE_DATE_SECONDS, TimeUnit.SECONDS);
    }

    public Authentication retrieve(String token) {
        try {
            return redisTemplate.opsForValue().get(token);
        } catch (SerializationException serializationException) {
            logger.error("Internal serializationException exception", serializationException);
            redisTemplate.delete(token);
            SecurityContextHolder.clearContext();
            throw new InternalAuthenticationServiceException(serializationException.getMessage());
        }
    }

    public boolean contains(String token) {
        try {
            return redisTemplate.opsForValue().get(token) != null;
        } catch (SerializationException serializationException) {
            logger.error("Internal serializationException exception", serializationException);
            deleteKey(token);
            SecurityContextHolder.clearContext();
            throw new InternalAuthenticationServiceException(serializationException.getMessage());
        }
    }

    public void deleteKey(String token) {
        logger.info("delete token key: {}", token);
        redisTemplate.delete(token);
    }

    /* JWT token generation & verification */
    public String generateJsonWebToken(String issuer, String subject) throws BusinessException {
        try {
            Date expiresDate = Date.from(Instant.now().plusMillis(HALF_HOUR_IN_MILLISECONDS));
            String token = JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC256(jwtSecret));
            return token;
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to generate json web token for {}", issuer);
            throw new BusinessException("Failed to create jwt token");
        }
    }

    public DecodedJWT verifyJsonWebToken(String issuer, String token) throws BusinessException {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                .withIssuer(issuer)
                .build();
            return verifier.verify(token);
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException("Failed to verify jwt token");
        } catch (InvalidClaimException e) {
            throw new BusinessException("Your token has already expired");
        }
    }
}
