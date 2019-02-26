package cn.notemi.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Title：MessageService
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/

@Component
public class MessageService {

    @Autowired
    MessageSource messageSource;

    public String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }
}
