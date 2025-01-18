package org.leng.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static long parseTime(String timeStr) {
        Matcher matcher = Pattern.compile("(\\d+)([smhdwMy])").matcher(timeStr);
        if (!matcher.matches()) {
            return -1;
        }
        int amount = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2);
        long millis;
        switch (unit) {
            case "s": millis = Duration.ofSeconds(amount).toMillis(); break;
            case "m": millis = Duration.ofMinutes(amount).toMillis(); break;
            case "h": millis = Duration.ofHours(amount).toMillis(); break;
            case "d": millis = Duration.ofDays(amount).toMillis(); break;
            case "w": millis = Duration.ofDays(amount * 7).toMillis(); break;
            case "M": millis = Duration.ofDays(amount * 30).toMillis(); break;
            case "y": millis = Duration.ofDays(amount * 365).toMillis(); break;
            default: return -1;
        }
        return System.currentTimeMillis() + millis;
    }
}