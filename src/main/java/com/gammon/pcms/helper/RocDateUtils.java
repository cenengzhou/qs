package com.gammon.pcms.helper;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class RocDateUtils {

    // Date Utils

    public static boolean compareYearMonthPeriod(YearMonth yearMonth1, YearMonth yearMonth2) {
        return yearMonth1.getYear() == yearMonth2.getYear() && yearMonth1.getMonthValue() == yearMonth2.getMonthValue();
    }

    public static YearMonth findYearMonthFromDate(Date date) {
        return YearMonth.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public static boolean compareDatePeriod(Date date1, Date date2) {
        YearMonth yearMonth1 = findYearMonthFromDate(date1);
        YearMonth yearMonth2 = findYearMonthFromDate(date2);
        return compareYearMonthPeriod(yearMonth1, yearMonth2);
    }

    // Others
    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
