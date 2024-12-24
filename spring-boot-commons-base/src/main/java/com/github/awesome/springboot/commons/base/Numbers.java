package com.github.awesome.springboot.commons.base;

import com.google.common.annotations.VisibleForTesting;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * {@code Numbers}类是一个工具类，提供了处理数字的常用方法。
 * 该类包含的静态方法用于对数字进行检查、转换或默认值处理。
 * 该类的构造函数是私有的，不能实例化。
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.1.0
 */
public final class Numbers {

    /**
     * 默认的{@link BigDecimal}数字舍入模式
     */
    public static final RoundingMode BIG_DECIMAL_DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * 私有构造函数，防止实例化{@code Numbers}类
     *
     * @throws UnsupportedOperationException 总是抛出异常，表示不支持实例化
     */
    @VisibleForTesting
    Numbers() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * 如果给定的{@link Integer}为{@code null}，则返回指定的默认值；否则，返回{@link Integer}本身
     *
     * @param number       要检查的{@link Integer}数字，可能为{@code null}
     * @param defaultValue 如果{@code number}为{@code null}时返回的默认值
     * @return 如果 {@code number} 为 {@code null}，则返回 {@code defaultValue}，否则返回 {@code number} 的值
     */
    public static int defaultIfNull(final Integer number, final int defaultValue) {
        return number == null ? defaultValue : number;
    }

    /**
     * 如果给定的{@link BigDecimal}为{@code null}，则返回指定的默认值；否则，返回{@link BigDecimal}本身
     *
     * @param number       要检查的{@link BigDecimal}数字，可能为{@code null}
     * @param defaultValue 如果{@code number}为{@code null}时返回的默认值
     * @return 如果 {@code number} 为 {@code null}，则返回 {@code defaultValue}，否则返回 {@code number} 的值
     */
    public static BigDecimal defaultIfNull(final BigDecimal number, final BigDecimal defaultValue) {
        return number == null ? defaultValue : number;
    }

    /**
     * 判断给定的{@link BigDecimal}数字是否等于零
     *
     * @param number 要检查的{@link BigDecimal}数字，可能为{@code null}
     * @return 如果 {@code number} 为零，返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isZero(final BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 判断给定的{@link BigDecimal}数字是否大于零，即是否为正数
     *
     * @param number 要检查的{@link BigDecimal}数字，不能为{@code null}
     * @return 如果 {@code number} 为正数，返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isPositive(final BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 判断给定的{@link BigDecimal}数字是否小于零，即是否为负数
     *
     * @param number 要检查的{@link BigDecimal}数字，不能为{@code null}
     * @return 如果 {@code number} 为负数，返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isNegative(final BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * 将给定的{@link BigDecimal}数字转换为字符串表示形式。
     * 如果{@code number}为{@code null}，则取{@code "0"}并保留{@code scale}位小数位数。
     *
     * @param number       要转换的{@link BigDecimal}数字，可能为{@code null}
     * @param scale        要保留的小数位数
     * @param roundingMode 舍入模式
     * @return {@code number} 的字符串表示形式，保留指定的小数位数，使用指定的舍入模式
     */
    public static String toPlainString(final BigDecimal number, final int scale, RoundingMode roundingMode) {
        return defaultIfNull(number, BigDecimal.ZERO).setScale(scale, roundingMode).toPlainString();
    }

    /**
     * 将给定的{@link BigDecimal}数字转换为指定精度的字符串表示形式。
     * 如果{@code number}为{@code null}，则取{@code "0"}并保留{@code scale}位小数位数。
     * 注意：该方法使用默认的舍入模式{@link #BIG_DECIMAL_DEFAULT_ROUNDING_MODE}。
     *
     * @param number 要转换的{@link BigDecimal}数字，可能为{@code null}
     * @param scale  要保留的小数位数
     * @return {@code number} 的字符串表示形式，保留指定的小数位数
     */
    public static String toScaledPlainString(final BigDecimal number, final int scale) {
        return toPlainString(number, scale, BIG_DECIMAL_DEFAULT_ROUNDING_MODE);
    }

    /**
     * 判断给定的字符串是否可以成功转换为{@link BigDecimal}。
     * 如果字符串表示一个有效的数字格式，则返回{@code true}；否则返回{@code false}。
     *
     * @param number 要检查的字符串，可能为{@code null}或空字符串
     * @return 如果 {@code number} 可以成功转换为 {@link BigDecimal}，返回 {@code true}；否则返回 {@code false}
     */
    public static boolean isBigDecimalConvertible(final String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }

        try {
            new BigDecimal(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
