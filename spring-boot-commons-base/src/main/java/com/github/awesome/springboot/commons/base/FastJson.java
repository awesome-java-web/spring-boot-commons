package com.github.awesome.springboot.commons.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 一个用于操作 fastjson 对象和数组的工具类。
 * <p>
 * 该类包含了多个静态方法，用于安全地从 fastjson 对象或数组中获取值，确保在输入为{@code null}或无效时返回空的{@link JSONObject}或{@link JSONArray}。
 * </p>
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.1.0
 */
public final class FastJson {

    /**
     * 空的{@link JSONObject}对象，作为默认返回值
     */
    private static final JSONObject EMPTY_JSON_OBJECT = new JSONObject();

    /**
     * 空的{@link JSONArray}对象，作为默认返回值
     */
    private static final JSONArray EMPTY_JSON_ARRAY = new JSONArray();

    /**
     * 私有构造函数，防止该工具类被实例化
     */
    private FastJson() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * 从给定的{@link JSONObject}中获取指定键对应的{@link JSONObject}值，如果该键对应的值为{@code null}，则返回空的{@link JSONObject}。
     * <p>
     * 如果传入的 JSON 对象为{@code null}，则直接返回空的{@link JSONObject}。
     * </p>
     *
     * @param json 要操作的{@link JSONObject}对象。
     * @param key  要获取的键名。
     * @return 如果键存在且对应值不为 {@code null}，则返回对应的 {@link JSONObject}；否则返回空的 {@link JSONObject}。
     */
    public static JSONObject getOrEmptyJsonObject(JSONObject json, final String key) {
        if (json == null) {
            return EMPTY_JSON_OBJECT;
        }
        JSONObject value = json.getJSONObject(key);
        return value == null ? EMPTY_JSON_OBJECT : value;
    }

    /**
     * 从给定的{@link JSONArray}中获取指定索引位置的{@link JSONObject}值，如果该位置的值为{@code null} 或索引越界，则返回空的{@link JSONObject}。
     * <p>
     * 如果传入的 JSON 数组为{@code null}、为空数组，或索引不合法，则直接返回空的{@link JSONObject}。
     * </p>
     *
     * @param json  要操作的{@link JSONArray}对象。
     * @param index 要获取的索引位置。
     * @return 如果索引位置有效且对应的值不为 {@code null}，则返回对应的 {@link JSONObject}；否则返回空的 {@link JSONObject}。
     */
    public static JSONObject getOrEmptyJsonObject(JSONArray json, final int index) {
        if (json == null || json.isEmpty() || index < 0 || index >= json.size()) {
            return EMPTY_JSON_OBJECT;
        }
        JSONObject value = json.getJSONObject(index);
        return value == null ? EMPTY_JSON_OBJECT : value;
    }

    /**
     * 从给定的{@link JSONObject}中获取指定键对应的{@link JSONArray}值，如果该键对应的值为{@code null}，则返回空的{@link JSONArray}。
     * <p>
     * 如果传入的 JSON 对象为{@code null}，则直接返回空的{@link JSONArray}。
     * </p>
     *
     * @param json 要操作的{@link JSONObject}对象。
     * @param key  要获取的键名。
     * @return 如果键存在且对应值不为 {@code null}，则返回对应的 {@link JSONArray}；否则返回空的 {@link JSONArray}。
     */
    public static JSONArray getOrEmptyJsonArray(JSONObject json, final String key) {
        if (json == null) {
            return EMPTY_JSON_ARRAY;
        }
        JSONArray value = json.getJSONArray(key);
        return value == null ? EMPTY_JSON_ARRAY : value;
    }

}
