package kr.co.eicn.ippbx.server.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EicnUtils {
	public static boolean isNotLocalhost(final String host) {
		return !StringUtils.isEmpty(host) && PatternUtils.isIp(host);
	}

	public static float getRateValue(long a, long b) {
		if(a == 0 || b == 0)
			return 0;

		long tmp_v = (a * 10000) / b;
		return (float) tmp_v / 100;
	}

	public static long getDiffTime(String s) {
		s = s.replace("-", "");
		s = s.replace(" ", "");
		s = s.replace(":", "");
		long l = getTimeToLong(s);
		long l1 = getDateTimeToLong();
		int i = (int)(l1 - l) / 1000;
		return i;
	}

	public static long getTimeToLong(String s) {
		if(s == null || s.length() != 14) {
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
			long l1 = calendar.getTimeInMillis();
			return l1;
		}
	}

	public static long getDateTimeToLong() {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		return l;
	}

	public static List<LocalDate> betweenDate(LocalDate startDate, LocalDate endDate) {
		final List<LocalDate> localDates = new ArrayList<>();
		long days = ChronoUnit.DAYS.between(startDate, endDate);
		for (int i = 0; i <= days; i++)
			localDates.add(startDate.plusDays(i));

		return localDates;
	}
}
