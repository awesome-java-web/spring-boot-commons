package com.github.awesome.springboot.commons.base;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * 日期时间工具类，该类不应被实例化，所有方法均为静态方法。
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.0.5
 */
public final class DateTime {

    /**
     * 默认的日期时间格式化模式
     */
    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认的日期格式化器，使用{@link DateTimeFormatter#ISO_LOCAL_DATE}格式化模式(yyyy-MM-dd)
     */
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * 默认的日期时间格式化器，使用{@link #DEFAULT_DATE_TIME_PATTERN}格式化模式。
     */
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN);

    /**
     * 私有构造函数，防止该工具类被实例化。
     *
     * @throws UnsupportedOperationException 如果尝试实例化该类，则抛出此异常。
     */
    private DateTime() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * 将给定的日期时间对象转换为目标时区的对应值。
     * <p>
     * 支持的输入类型包括：{@link Date}、{@link String}、{@link LocalDate}、{@link LocalDateTime}。
     * 如果输入是{@link String}，将根据内容自动判断日期或日期时间格式进行解析。
     * </p>
     *
     * @param datetime     输入的日期时间对象，可以是{@link Date}、{@link String}、{@link LocalDate}或{@link LocalDateTime}。
     * @param targetZoneId 目标时区。
     * @return 转换后的目标时区的日期时间对象，类型可能为{@link Date}、{@link String}、{@link LocalDate}或{@link LocalDateTime}。
     * @throws IllegalArgumentException 如果输入的 datetime 或 targetZoneId 为 {@code null}，或者 datetime 类型不支持。
     */
    public static Object atZone(Object datetime, ZoneId targetZoneId) {
        if (datetime == null || targetZoneId == null) {
            throw new IllegalArgumentException("datetime or targetZoneId is null");
        }

        if (datetime instanceof Date) {
            return toDate(toLocalDateTime((Date) datetime, targetZoneId));
        }

        if (datetime instanceof String) {
            final String dt = datetime.toString();
            final boolean isLocalDate = isParseableLocalDate(dt);
            DateTimeFormatter formatter = isLocalDate ? DEFAULT_DATE_FORMATTER : DEFAULT_DATE_TIME_FORMATTER;
            return isLocalDate
                ? toLocalDate(dt, formatter, targetZoneId).format(formatter)
                : toLocalDateTime(dt, formatter, targetZoneId).format(formatter);
        }

        if (datetime instanceof LocalDate) {
            return toLocalDate((LocalDate) datetime, targetZoneId);
        }

        if (datetime instanceof LocalDateTime) {
            return toLocalDateTime((LocalDateTime) datetime, targetZoneId);
        }

        throw new IllegalArgumentException("Unsupported type: " + datetime.getClass().getName());
    }

    /**
     * 将日期时间字符串解析为{@link LocalDateTime}对象，并根据目标时区进行转换。
     *
     * @param datetime     日期时间字符串。
     * @param formatter    日期时间的格式化器。
     * @param targetZoneId 目标时区。
     * @return 转换后的 {@link LocalDateTime} 对象。
     */
    public static LocalDateTime toLocalDateTime(final String datetime, DateTimeFormatter formatter, ZoneId targetZoneId) {
        LocalDateTime localDateTime = LocalDateTime.parse(datetime, formatter);
        return toLocalDateTime(localDateTime, targetZoneId);
    }

    /**
     * 将{@link Date}对象转换为目标时区的{@link LocalDateTime}对象。
     *
     * @param date         输入的{@link Date}对象。
     * @param targetZoneId 目标时区。
     * @return 转换后的 {@link LocalDateTime} 对象。
     */
    public static LocalDateTime toLocalDateTime(Date date, ZoneId targetZoneId) {
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        return zonedDateTime.withZoneSameInstant(targetZoneId).toLocalDateTime();
    }

    /**
     * 将{@link LocalDateTime}对象转换为目标时区的{@link LocalDateTime}对象。
     *
     * @param localDateTime 输入的{@link LocalDateTime}对象。
     * @param targetZoneId  目标时区。
     * @return 转换后的 {@link LocalDateTime} 对象。
     */
    public static LocalDateTime toLocalDateTime(LocalDateTime localDateTime, ZoneId targetZoneId) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.withZoneSameInstant(targetZoneId).toLocalDateTime();
    }

    /**
     * 将日期字符串解析为{@link LocalDate}对象，并根据目标时区进行转换。
     *
     * @param date         日期字符串。
     * @param formatter    日期格式化器。
     * @param targetZoneId 目标时区。
     * @return 转换后的 {@link LocalDate} 对象。
     */
    public static LocalDate toLocalDate(final String date, DateTimeFormatter formatter, ZoneId targetZoneId) {
        LocalDate localDate = LocalDate.parse(date, formatter);
        return toLocalDate(localDate, targetZoneId);
    }

    /**
     * 将给定的{@link LocalDate}对象转换为目标时区的{@link LocalDate}对象。
     * <p>
     * 该方法首先将{@link LocalDate}对象转换为{@link LocalDateTime}，然后通过{@link ZonedDateTime}
     * 转换到目标时区，最后返回对应时区的{@link LocalDate}对象。
     * </p>
     *
     * @param localDate    输入的{@link LocalDate}对象。
     * @param targetZoneId 目标时区。
     * @return 转换后的目标时区的 {@link LocalDate} 对象。
     */
    public static LocalDate toLocalDate(LocalDate localDate, ZoneId targetZoneId) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return zonedDateTime.withZoneSameInstant(targetZoneId).toLocalDate();
    }

    /**
     * 将{@link LocalDateTime}对象转换为{@link Date}对象。
     * <p>
     * 该方法将{@link LocalDateTime}对象转换为系统默认时区的{@link Instant}，然后通过{@link Date#from}
     * 方法转换为{@link Date}对象。
     * </p>
     *
     * @param localDateTime 输入的{@link LocalDateTime}对象。
     * @return 转换后的 {@link Date} 对象。
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 判断给定的日期字符串是否可以被解析为{@link LocalDate}对象。
     * <p>
     * 该方法尝试使用{@link DateTimeFormatter#ISO_LOCAL_DATE}格式化器解析给定的日期字符串。
     * 如果解析成功，返回{@code true}；否则，返回{@code false}。
     * </p>
     *
     * @param date 输入的日期字符串。
     * @return 如果输入的日期字符串可以解析为 {@link LocalDate} 对象，则返回{@code true}；否则返回{@code false}。
     */
    public static boolean isParseableLocalDate(final String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 判断两个时区是否为相同的时区。
     * <p>
     * 该方法通过比较当前时刻在两个时区的{@link Instant}是否相等来判断两个时区是否相同。
     * </p>
     *
     * @param zoneId        当前时区。
     * @param anotherZoneId 另一个时区。
     * @return 如果两个时区相同，返回{@code true}；否则返回{@code false}。
     */
    public static boolean isSameZone(ZoneId zoneId, ZoneId anotherZoneId) {
        LocalDateTime now = LocalDateTime.now();
        Instant instant = now.atZone(zoneId).toInstant();
        Instant anotherInstant = now.atZone(anotherZoneId).toInstant();
        return instant.equals(anotherInstant);
    }

    /**
     * 返回多个{@link LocalDateTime}对象中的最大值。
     * <p>
     * 该方法会遍历所有的{@link LocalDateTime}对象，找到最晚的一个并返回。
     * </p>
     *
     * @param datetime 输入的多个{@link LocalDateTime}对象。
     * @return 最大的 {@link LocalDateTime} 对象。
     * @throws IllegalArgumentException 如果输入的数组为空，则抛出此异常。
     */
    public static LocalDateTime max(LocalDateTime... datetime) {
        LocalDateTime max = datetime[0];
        for (LocalDateTime dt : datetime) {
            max = dt.isAfter(max) ? dt : max;
        }
        return max;
    }

    /**
     * 返回多个{@link LocalDateTime}对象中的最小值。
     * <p>
     * 该方法会遍历所有的{@link LocalDateTime}对象，找到最早的一个并返回。
     * </p>
     *
     * @param datetime 输入的多个{@link LocalDateTime}对象。
     * @return 最小的 {@link LocalDateTime} 对象。
     * @throws IllegalArgumentException 如果输入的数组为空，则抛出此异常。
     */
    public static LocalDateTime min(LocalDateTime... datetime) {
        LocalDateTime min = datetime[0];
        for (LocalDateTime dt : datetime) {
            min = dt.isBefore(min) ? dt : min;
        }
        return min;
    }

    /**
     * 返回多个{@link LocalDate}对象中的最大值。
     * <p>
     * 该方法会遍历所有的{@link LocalDate}对象，找到最晚的一个并返回。
     * </p>
     *
     * @param date 输入的多个{@link LocalDate}对象。
     * @return 最大的 {@link LocalDate} 对象。
     * @throws IllegalArgumentException 如果输入的数组为空，则抛出此异常。
     */
    public static LocalDate max(LocalDate... date) {
        LocalDate max = date[0];
        for (LocalDate dt : date) {
            max = dt.isAfter(max) ? dt : max;
        }
        return max;
    }

    /**
     * 返回多个{@link LocalDate}对象中的最小值。
     * <p>
     * 该方法会遍历所有的{@link LocalDate}对象，找到最早的一个并返回。
     * </p>
     *
     * @param date 输入的多个{@link LocalDate}对象。
     * @return 最小的 {@link LocalDate} 对象。
     * @throws IllegalArgumentException 如果输入的数组为空，则抛出此异常。
     */
    public static LocalDate min(LocalDate... date) {
        LocalDate min = date[0];
        for (LocalDate dt : date) {
            min = dt.isBefore(min) ? dt : min;
        }
        return min;
    }

}
