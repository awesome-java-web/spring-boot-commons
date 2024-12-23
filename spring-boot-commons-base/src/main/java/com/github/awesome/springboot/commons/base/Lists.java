package com.github.awesome.springboot.commons.base;

import com.google.common.annotations.VisibleForTesting;

import java.util.List;
import java.util.Optional;

/**
 * {@code Lists}类是一个工具类，提供了处理{@link List}的一些常用方法。该类的构造函数是私有的，不能实例化
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.1.0
 */
public final class Lists {

    /**
     * 私有构造函数，防止实例化{@code Lists}类
     *
     * @throws UnsupportedOperationException 总是抛出异常，表示不支持实例化
     */
    @VisibleForTesting
    Lists() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * 安全地获取{@link List}中指定索引位置的元素。如果索引无效（越界或为空），返回一个空的{@link Optional}
     *
     * @param <E>   元素类型
     * @param list  需要访问的列表，不能为空
     * @param index 要访问的索引位置
     * @return 包含元素的 {@link Optional}，如果索引无效则返回{@link Optional#empty()}
     * @throws NullPointerException 如果传入的{@code list}为{@code null}，则抛出{@code NullPointerException}
     */
    public static <E> Optional<E> safeGet(List<E> list, final int index) {
        if (list == null || list.isEmpty() || index < 0 || index >= list.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(index));
    }

    /**
     * 获取{@link List}中的第一个元素。如果列表为空或为{@code null}，返回一个空的{@link Optional}
     *
     * @param <E>  元素类型
     * @param list 需要访问的列表，不能为空
     * @return 包含第一个元素的 {@link Optional}，如果列表为空则返回 {@link Optional#empty()}
     */
    public static <E> Optional<E> firstOf(List<E> list) {
        return safeGet(list, 0);
    }

}
