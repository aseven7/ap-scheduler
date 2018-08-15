package com.tanuputra.apsch.util;
import java.util.Calendar;
import java.util.Locale;

public class ApUtil {
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		return calendar;
	}
}
