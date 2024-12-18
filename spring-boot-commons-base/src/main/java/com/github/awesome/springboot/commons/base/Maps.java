package com.github.awesome.springboot.commons.base;

import java.math.BigDecimal;
import java.util.Map;

/**
 * {@code Maps}类是一个工具类，提供了处理{@link Map}的一些常用方法。该类的构造函数是私有的，不能实例化
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.1.0
 */
public final class Maps {

    /**
     * 私有构造函数，防止实例化{@code Maps}类
     *
     * @throws UnsupportedOperationException 总是抛出异常，表示不支持实例化
     */
    private Maps() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * 从给定的{@link Map}中安全获取指定键对应的值。
     * 如果{@link Map}或键为{@code null}，或者指定的键对应的值为{@code null}，则返回空字符串。
     *
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @param map 要访问的{@link Map}，可以为{@code null}
     * @param key 要查找的键，不能为空
     * @return 如果键对应的值存在，则返回该值的字符串表示，否则返回空字符串
     */
    public static <K, V> String getOrEmptyString(Map<K, V> map, K key) {
        if (map == null || key == null) {
            return Strings.EMPTY;
        }
        V value = map.get(key);
        return value == null ? Strings.EMPTY : value.toString();
    }

    /**
     * 从给定的{@link Map}中安全获取指定键对应的{@link BigDecimal}值。
     * 如果{@link Map}或键为{@code null}，或者指定的键没有对应的{@link BigDecimal}值，
     * 则返回{@code BigDecimal.ZERO}。
     *
     * @param <K> 键的类型
     * @param map 要访问的{@link Map}，可以为{@code null}
     * @param key 要查找的键
     * @return 如果键对应的值存在，则返回该 {@link BigDecimal} 值，否则返回 {@code BigDecimal.ZERO}
     */
    public static <K> BigDecimal getBigDecimalOrZero(Map<K, BigDecimal> map, K key) {
        return (map == null || key == null) ? BigDecimal.ZERO : map.getOrDefault(key, BigDecimal.ZERO);
    }

}
