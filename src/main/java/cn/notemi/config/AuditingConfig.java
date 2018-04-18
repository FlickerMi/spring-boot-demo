package cn.notemi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class AuditingConfig {

    final private static String SYSTEM_USER = "SYSTEM";

    @Bean
    public AuditorAware<String> createAuditorProvider() {
        return new SecurityAuditor();
    }

    public AuditingEntityListener createAuditingListener() {
        return new AuditingEntityListener();
    }

    public static class SecurityAuditor implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            String username = SYSTEM_USER;
            /*AuthenticationWithToken authentication = (AuthenticationWithToken) SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Account user = (Account) authentication.getPrincipal();
                username = user.getUsername();
            }*/
            return Optional.ofNullable(username);
        }
    }
}
