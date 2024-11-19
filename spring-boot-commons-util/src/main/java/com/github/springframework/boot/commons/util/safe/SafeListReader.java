package com.github.springframework.boot.commons.util.safe;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class SafeListReader {

	public SafeListReader() {
		throw new UnsupportedOperationException("Utility class should not be instantiated");
	}

	@Nullable
	public static <E> E safeGetByIndex(List<E> list, final int index) {
		return (list != null && index >= 0 && index < list.size()) ? list.get(index) : null;
	}

}
