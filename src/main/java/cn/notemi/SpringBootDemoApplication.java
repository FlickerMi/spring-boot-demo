package cn.notemi;

import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class SpringBootDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
		localeResolver.setDefaultLocale(Locale.CHINA);
		return localeResolver;
	}

	@Bean
	public MessageSource messageSource() {
		Locale.setDefault(Locale.CHINA);
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:locale/messages");
		messageSource.setDefaultEncoding("utf-8");
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}
}
