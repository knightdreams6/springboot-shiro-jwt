package com.knight;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.knight.entity.enums.CommonResultConstants;
import lombok.NonNull;
import org.springframework.context.support.AbstractMessageSource;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 自定义消息来源
 *
 * @author lixiao
 * @date 2022/06/03
 */
public class CustomMessageSource extends AbstractMessageSource {

	/**
	 * 中文消息
	 */
	private final Map<String, String> zhMessage;

	/**
	 * 区域代码消息映射
	 */
	private final Map<Locale, Map<String, String>> localeCodeMessageMap = new HashMap<>(4);

	/**
	 * 代码区域消息映射
	 */
	private final Map<String, Map<Locale, MessageFormat>> codeLocaleMessageMap = new ConcurrentHashMap<>();

	public CustomMessageSource() {
		this.zhMessage = Arrays.stream(CommonResultConstants.values()).collect(
				Collectors.toMap(errorState -> String.valueOf(errorState.getCode()), CommonResultConstants::getMsg));
		// 英文消息
		Map<String, String> enMessage = Arrays.stream(CommonResultConstants.values()).collect(Collectors.toMap(
				errorState -> String.valueOf(errorState.getCode()),
				errorState -> StrUtil.isBlank(errorState.enMsg()) ? errorState.getMsg() : errorState.getEnMsg()));
		this.localeCodeMessageMap.put(Locale.SIMPLIFIED_CHINESE, zhMessage);
		this.localeCodeMessageMap.put(Locale.CHINESE, zhMessage);
		this.localeCodeMessageMap.put(Locale.ENGLISH, enMessage);
		this.localeCodeMessageMap.put(Locale.US, enMessage);
	}

	@Override
	protected String resolveCodeWithoutArguments(@NonNull String code, @NonNull Locale locale) {
		return localeCodeMessageMap.getOrDefault(locale, zhMessage).get(code);
	}

	@Override
	protected MessageFormat resolveCode(@NonNull String code, @NonNull Locale locale) {
		// 根据code获取localeMessageFormatMap
		Map<Locale, MessageFormat> localeMessageFormatMap = codeLocaleMessageMap.get(code);
		// 如果不为空
		if (CollUtil.isNotEmpty(localeMessageFormatMap)) {
			// 根据locale获取MessageFormat
			MessageFormat messageFormat = localeMessageFormatMap.get(locale);
			if (ObjectUtil.isNotNull(messageFormat)) {
				return messageFormat;
			}
		}
		// 获取msg
		String msg = localeCodeMessageMap.getOrDefault(locale, zhMessage).get(code);
		// 根据msg创建MessageFormat
		MessageFormat messageFormat = createMessageFormat(msg, locale);
		if (localeMessageFormatMap == null) {
			localeMessageFormatMap = this.codeLocaleMessageMap.computeIfAbsent(code, c -> new ConcurrentHashMap<>(16));
			localeMessageFormatMap.put(locale, messageFormat);
		}
		return messageFormat;
	}

}
