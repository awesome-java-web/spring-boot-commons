package com.github.springframework.boot.commons.util.safe;

import org.jetbrains.annotations.NotNull;

public final class SafeJsonReader {

	SafeJsonReader() {
		throw new UnsupportedOperationException("Utility class should not be instantiated");
	}

	public static final class FastJson {

		private static final com.alibaba.fastjson.JSONObject EMPTY_JSON_OBJECT = new com.alibaba.fastjson.JSONObject();

		FastJson() {
			throw new UnsupportedOperationException("Utility class should not be instantiated");
		}

		public static com.alibaba.fastjson.JSONObject getOrEmptyJsonObject(com.alibaba.fastjson.JSONObject json, @NotNull final String key) {
			if (json == null) {
				return EMPTY_JSON_OBJECT;
			}
			com.alibaba.fastjson.JSONObject value = json.getJSONObject(key);
			return value == null ? EMPTY_JSON_OBJECT : value;
		}

		public static com.alibaba.fastjson.JSONObject getOrEmptyJsonObject(com.alibaba.fastjson.JSONArray json, final int index) {
			if (json == null || json.isEmpty() || index < 0 || index >= json.size()) {
				return EMPTY_JSON_OBJECT;
			}
			com.alibaba.fastjson.JSONObject value = json.getJSONObject(index);
			return value == null ? EMPTY_JSON_OBJECT : value;
		}

		public static com.alibaba.fastjson.JSONArray getOrEmptyJsonArray(com.alibaba.fastjson.JSONObject json, @NotNull final String key) {
			if (json == null) {
				return new com.alibaba.fastjson.JSONArray();
			}
			com.alibaba.fastjson.JSONArray value = json.getJSONArray(key);
			return value == null ? new com.alibaba.fastjson.JSONArray() : value;
		}

	}

}
