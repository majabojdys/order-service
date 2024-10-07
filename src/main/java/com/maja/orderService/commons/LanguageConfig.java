package com.maja.orderService.commons;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

@Configuration
class LanguageConfig {

    @Bean
    LocaleResolver localeResolver(){
        return new FixedLocaleResolver(Locale.ENGLISH);
    }
}

