package br.com.wendelnogueira.javaapiboilerplate.util;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import java.text.MessageFormat;
import java.util.Locale;

@Component
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MessageExceptionFormatter {
    @Autowired
    private static MessageSource messageSource;

    public static String getMessage(String code, String defaultMessage) {
        String resolvedMessage = messageSource.getMessage(code, null, defaultMessage, Locale.getDefault());
        return MessageFormat.format(resolvedMessage, defaultMessage);
    }
}