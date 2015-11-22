package com.eboji.commons.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间相关的工具类
 * 
 * @author zhoucl
 *
 */
public class DateUtil {
	public static final String FORMATTER_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMATTER_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	public static final String FORMATTER_YYYYMMDD = "yyyy-MM-dd";

	public static String formatDate(Date date, String formatter) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		return sdf.format(date);
	}
}
