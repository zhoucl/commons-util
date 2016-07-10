package com.eboji.commons.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间相关的工具类
 * 
 * @author zhoucl
 *
 */
public class DateUtil {
	public static final String FORMATTER_YYYY_MM_DDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMATTER_YYYY_MM_DDHHMM = "yyyy-MM-dd HH:mm";
	public static final String FORMATTER_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String FORMATTER_YYMMDD = "yyMMdd";
	public static final String FORMATTER_YYYYMMDD = "yyyyMMdd";
	public static final String FORMATTER_HHMMSS = "HH:mm:ss";

	public static String formatDate(Date date, String formatter) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		return sdf.format(date);
	}
	
	public static Date formatDate(String date, String formatter) {
		Date ret = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		try {
			ret = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}
