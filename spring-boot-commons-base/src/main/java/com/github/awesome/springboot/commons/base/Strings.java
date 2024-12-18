package com.github.awesome.springboot.commons.base;

/**
 * 一个工具类，提供与字符串相关的常用方法
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.1.0
 */
public final class Strings {

    /**
     * 一个常量，表示空字符串。
     * <p>
     * 该常量用于避免硬编码空字符串（""），提供更清晰的表示。
     * </p>
     */
    public static final String EMPTY = "";

    /**
     * 私有构造函数，防止该类被实例化。
     * <p>
     * 如果尝试实例化该类，会抛出{@link UnsupportedOperationException}异常。
     * </p>
     */
    private Strings() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * 如果给定的对象为{@code null}或空字符串（去除空白后），则返回默认字符串，否则返回对象的字符串表示。
     * <p>
     * 该方法用于在处理字符串时，避免出现空指针异常或空字符串，提供一个默认值。
     * </p>
     *
     * @param object     被检查的对象，可能为{@code null}。
     * @param defaultStr 如果{@code object}为{@code null}或空字符串时，返回的默认字符串。
     * @return 如果 {@code object} 为 {@code null} 或空字符串，则返回 {@code defaultStr}，否则返回 {@code object.toString()} 的结果。
     */
    public static String defaultIfNullOrEmpty(final Object object, final String defaultStr) {
        return object == null || object.toString().trim().isEmpty() ? defaultStr : object.toString();
    }

}
