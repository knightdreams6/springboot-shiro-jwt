package com.knight;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * i18n配置
 *
 * @author lixiao
 * @since 2022/06/03
 */
@AutoConfigureBefore(value = { WebMvcAutoConfiguration.class })
@Configuration
public class I18nConfig {

	/**
	 * 消息来源
	 * @return {@link MessageSource}
	 */
	@Bean
	MessageSource messageSource() {
		return new CustomMessageSource();
	}

	/**
	 * 区域设置解析器
	 * @return {@link LocaleResolver}
	 */
	@Bean
	LocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
		localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
		return localeResolver;
	}

}
