package com.github.springframework.boot.commons.util;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class SafeListReader {

    SafeListReader() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    @Nullable
    public static <E> E getByIndex(List<E> list, final int index) {
        return list != null && index >= 0 && index < list.size() ? list.get(index) : null;
    }

}
