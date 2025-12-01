package br.com.wendelnogueira.javaapiboilerplate.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import java.text.MessageFormat;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageExceptionFormatter {

    private final MessageSource messageSource;

    public String getMessage(String code, String defaultMessage) {
        String resolvedMessage = messageSource.getMessage(code, null, defaultMessage, Locale.getDefault());
        assert resolvedMessage != null;
        return MessageFormat.format(resolvedMessage, defaultMessage);
    }
}