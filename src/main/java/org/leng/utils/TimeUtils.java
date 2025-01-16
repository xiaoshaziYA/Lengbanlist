package org.leng.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static long generateTimestampFromDays(int days) {
        long currentTimeMillis = System.currentTimeMillis();
        long daysInMillis = TimeUnit.DAYS.toMillis(days);
        return currentTimeMillis + daysInMillis;
    }

    public static String timestampToReadable(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}