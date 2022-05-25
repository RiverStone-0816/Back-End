package kr.co.eicn.ippbx.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class EicnUtils {
    public static boolean isNotLocalhost(final String host) {
        return !StringUtils.isEmpty(host) && PatternUtils.isIp(host);
    }

    public static float getRateValue(long a, long b) {
        if (a == 0 || b == 0)
            return 0;

        long tmp_v = (a * 10000) / b;
        return Float.parseFloat(String.format("%.1f", (float) tmp_v / 100));
    }

    public static long getDiffTime(String s) {
        s = s.replace("-", "");
        s = s.replace(" ", "");
        s = s.replace(":", "");
        long l = getTimeToLong(s);
        long l1 = getDateTimeToLong();
        return (int) (l1 - l) / 1000;
    }

    public static long getTimeToLong(String s) {
        if (s == null || s.length() != 14) {
            return 0L;
        } else {
            boolean flag = false;
            int i = Integer.parseInt(s.substring(0, 4));
            int j = Integer.parseInt(s.substring(4, 6));
            int k = Integer.parseInt(s.substring(6, 8));
            int l = Integer.parseInt(s.substring(8, 10));
            int i1 = Integer.parseInt(s.substring(10, 12));
            int j1 = Integer.parseInt(s.substring(12, 14));
            Calendar calendar = Calendar.getInstance();
            calendar.set(i, j - 1, k, l, i1, j1);
            return calendar.getTimeInMillis();
        }
    }

    public static long getDateTimeToLong() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public static List<LocalDate> betweenDate(LocalDate startDate, LocalDate endDate) {
        final List<LocalDate> localDates = new ArrayList<>();
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 0; i <= days; i++)
            localDates.add(startDate.plusDays(i));

        return localDates;
    }

    public static String getStuffZero(String src, int num) {
        if (src == null) {
            src = "";
        }
        int len = src.length();
        if (len >= num) {
            return src;
        }
        StringBuilder ret = new StringBuilder(src);
        for (int i = 0; i < (num - len); i++) {
            ret.insert(0, "0");
        }
        return ret.toString();
    }

    public static String getRandomNumber() {
        Random rd = new Random();
        int i_random = rd.nextInt(1000000);
		String rdstr = String.valueOf(i_random);
        if (rdstr.length() < 6) {
            rdstr = EicnUtils.getStuffZero(rdstr, 6);
        }

        return rdstr;
    }

    public static Integer convertStringToInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
