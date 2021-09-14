package com.gammon.pcms.helper;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Calendar;
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

    public static YearMonth findYearMonthFromCutoffDate(Date date, int rocCutoffDate) {
        return YearMonth.from(toCutOffDate(date, rocCutoffDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public static boolean compareDatePeriodCutoff(Date date1, Date date2, int rocCutoffDate) {
        YearMonth yearMonth1 = findYearMonthFromCutoffDate(date1, rocCutoffDate);
        YearMonth yearMonth2 = findYearMonthFromCutoffDate(date2, rocCutoffDate);
        return compareYearMonthPeriod(yearMonth1, yearMonth2);
    }

    private static Date toCutOffDate(Date inputDate, int rocCutoffDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day <= rocCutoffDate) {
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
        }
        return calendar.getTime();
    }

    // Others

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
