package org.stas.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author stas ambartsumov
 * a wrapper over Spring MessageSource for getting messages from messages.properties
 */
@Component
public class MessageService {
    @Autowired
    private MessageSource messageSource;

    /**
     * get a message from messages.properties by id
     * @param id
     * @return
     */
    public String getMessage(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(id, null, locale);
    }
}
