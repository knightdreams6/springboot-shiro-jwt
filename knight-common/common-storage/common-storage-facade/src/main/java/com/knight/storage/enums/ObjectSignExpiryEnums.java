package com.knight.storage.enums;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * 对象签名到期枚举
 *
 * @author knight
 * @since 2023/01/15
 */
@RequiredArgsConstructor
public enum ObjectSignExpiryEnums {

    /**
     * 一分钟
     */
    ONE_MINUTE(TimeUnit.MINUTES.toSeconds(1)),

    /**
     * 十分钟
     */
    TEN_MINUTE(TimeUnit.MINUTES.toSeconds(10)),

    /**
     * 二十分钟
     */
    TWENTY_MINUTE(TimeUnit.MINUTES.toSeconds(20)),

    /**
     * 三十分钟
     */
    THIRTY_MINUTE(TimeUnit.MINUTES.toSeconds(30)),

    /**
     * 一个小时
     */
    ONE_HOUR(TimeUnit.HOURS.toSeconds(1)),

    /**
     * 一天
     */
    ONE_DAY(TimeUnit.DAYS.toSeconds(1)),

    /**
     * 七天
     */
    SEVEN_DAY(TimeUnit.DAYS.toSeconds(7)),


    ;

    private final Long expirySeconds;

    public int expirySeconds() {
        return expirySeconds.intValue();
    }

}
