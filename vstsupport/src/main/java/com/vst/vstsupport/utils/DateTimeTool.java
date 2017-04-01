package com.vst.vstsupport.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.vstecs.android.funframework.utils.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间格式化工具
 */
@SuppressLint("SimpleDateFormat")
public class DateTimeTool {
	public static final int WEEKDAYS = 7;
	public static DateFormat hotelDetailDf = new SimpleDateFormat("MM月dd日");
	public static String[] WEEK = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	private static SimpleDateFormat ymd2 = new SimpleDateFormat("yy-MM-dd");
	private static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat ymdhmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
	private static SimpleDateFormat mdhm = new SimpleDateFormat("MM-dd HH:mm");
	private static SimpleDateFormat sdf2 = null;
	private static SimpleDateFormat formatBuilder;
	private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
	private static DateFormat hotelDf = new SimpleDateFormat("MM月dd日  E");

	public static String mdhmDate(long paramDate) {
		String retDate = null;
		try {
			retDate = mdhm.format(new Date(paramDate * 1000));
		} catch (Exception e) {
			retDate = "1970-01-01";
		}
		return retDate;
	}
	
	public static String ymdDate(long paramDate) {
		String retDate = null;
		try {
			retDate = ymd.format(new Date(paramDate * 1000));
		} catch (Exception e) {
			retDate = "1970-01-01";
		}
		return retDate;
	}

	public static String ymdhmsDate(long paramDate) {
		String retDate = null;
		try {
			retDate = ymdhms.format(new Date(paramDate * 1000));
		} catch (Exception e) {
			retDate = "1970-01-01 00:00:00";
		}
		return retDate;
	}
	public static String ymdhmsDate(String paramDate) {
		String retDate = null;
		if (StringUtils.isEmpty(paramDate)) {
			return paramDate;
		}
		try {
			long date = Long.parseLong(paramDate);
			retDate = ymdhms.format(new Date(date * 1000));
		} catch (Exception e) {
			retDate = "1970-01-01 00:00:00";
		}
		return retDate;
	}
	public static String ymdhmsDateWan(String paramDate) {
		String retDate = null;
		if (StringUtils.isEmpty(paramDate)) {
			return paramDate;
		}
		try {
			long date = Long.parseLong(paramDate);
			retDate = mdhm.format(new Date(date * 1000));
		} catch (Exception e) {
			retDate = "01-01 00:00";
		}
		return retDate;
	}
	
	public static String ymdhmssDate(long paramDate) {
		String retDate = null;
		try {
			retDate = ymdhmss.format(new Date(paramDate));
		} catch (Exception e) {
			retDate = "1970-01-01 00:00:00";
		}
		return retDate;
	}
	
	/**
	 * 
	 * @description：根据传入的类型获取时间字符串
	 * @author zc
	 * @date 2015年4月8日 上午11:29:42
	 */
	public static String getTimeFromFormat(String pattern, long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(time));
	}
	
/**
 * @description：01/02 13:00格式
 * @author samy
 * @date 2015-1-9 上午11:48:12
 */
	public static String specialmdhmDate(long paramDate) {
		SimpleDateFormat specialmdhmDate = new SimpleDateFormat("MM/dd HH:mm");
		String retDate = null;
		try {
			retDate = specialmdhmDate.format(new Date(paramDate * 1000));
		} catch (Exception e) {
			retDate = "01/01 00:00";
		}
		return retDate;
	}
	
	public static String ymdhmDate(long paramDate) {
		String retDate = null;
		try {
			retDate = ymdhm.format(new Date(paramDate * 1000));
		} catch (Exception e) {
			retDate = "1970-01-01 00:00";
		}
		return retDate;
	}
	
	public static String ymdhmDate(String paramDate) {
		String retDate = null;
		try {
			retDate = ymdhm.format(new Date(Long.parseLong(paramDate)* 1000));
		} catch (Exception e) {
//			retDate = "1970-01-01 00:00";
			retDate = paramDate;
		}
		return retDate;
	}
	
	/**
	 * 
	 * @param time
	 * @return HH:mm:ss
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeStandard(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(new Date(time*1000));
	}
	
	public static String getHhMmDate(String format) {
		return ymdhm.format(getDateFromString(format));
	}

	/**
	 * 方法概述：从字符串中提取出Date类型日期，在XMPP获取消息时使用
	 */
	public static Date getDateFromString(String dateStr) {
		Date date = null;
		try {
			date = ymdhms.parse(dateStr);
		} catch (ParseException e) {

		}
		return date;
	}

	/**
	 * 方法概述：判断两个时间戳是否相差指定时间
	 */
	public static boolean isTimeBetweenSeconds(String postTimeStamp, String preTimeStamp, long millisecond) {
		if (Long.parseLong(postTimeStamp) - Long.parseLong(preTimeStamp) <= millisecond) { return true; }
		return false;
	}

	public static String getDateToWeek(long time) {
		Date otherDay = new Date(time);
		return getDateToWeek(ymdhms.format(otherDay));
	}

	/**
	 * 日期变量转成对应的星期字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateToWeek(String date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDateFromString(date));
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayIndex < 1 || dayIndex > WEEKDAYS) { return null; }

		return WEEK[dayIndex - 1] + "  " + hm.format(getDateFromString(date));
	}

	/**
	 * 方法概述：获取当前时间的连续字符串 1.作为文件名使用 2.不带文件后缀
	 */
	public static String getFileNameByTime() {
		return sDateFormat.format(new Date());
	}

	/**
	 * 方法概述：获取两个时间点之间的差
	 */
	public static long compareTime(String begin, String end, SimpleDateFormat dateFormate) {
		long sss = 0;
		try {
			Date date1 = dateFormate.parse(begin);
			Date date2 = dateFormate.parse(end);
			long diff = date2.getTime() - date1.getTime();
			sss = diff / 1000; // 秒
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sss;
	}

	public static String getTimeStamp() { // 得到的是一个时间戳
		sdf2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf2.format(new Date());// 将当前日期进行格式化操作
	}

	public static String formatDate(long paramDate) {
		long currentTime = new Date().getTime();
		long blankTime = currentTime - paramDate;
		String retTime;

		if (blankTime > 86400000L) {
			retTime = ymd2.format(new Date(paramDate));
		} else if (blankTime < 1800000L) {
			retTime = "刚刚";
		} else if (blankTime > 1800000L && blankTime < 3600000L) {
			retTime = "半小时前";
		} else {
			Object[] arr1 = new Object[2];
			arr1[0] = Long.valueOf(blankTime / 3600000L);
			arr1[1] = "小时前";
			retTime = String.format("%d%s", arr1);
		}
		return retTime;
	}

	public static String formatDate(Date paramDate) {
		long currentTime = new Date().getTime();
		long occurTime = paramDate.getTime();
		long blankTime = currentTime - occurTime;
		String retTime;

		if (blankTime > 86400000L) {
			retTime = ymd2.format(paramDate);
		} else if (blankTime < 1800000L) {
			retTime = "刚刚";
		} else if (blankTime > 1800000L && blankTime < 3600000L) {
			retTime = "半小时前";
		} else {
			Object[] arr1 = new Object[2];
			arr1[0] = Long.valueOf(blankTime / 3600000L);
			arr1[1] = "小时前";
			retTime = String.format("%d%s", arr1);
		}
		return retTime;
	}

	public static String getIntervalDays(long sl, long el) {
		long ei = el - sl;
		// 根据毫秒数计算间隔天数
		int month = (int) (ei / (1000 * 60 * 60 * 24 * 12));
		int days = (int) (ei / (1000 * 60 * 60 * 24));
		int housrs = (int) (ei / (1000 * 60 * 60));
		int Minutes = (int) (ei / (1000 * 60));
		int ss = (int) (ei / (1000));
		if (ss <= 60) {
			return ss + " 秒钟前";
		} else if (Minutes <= 60) {
			return Minutes + " 分钟前";
		} else if (housrs <= 24) {
			return housrs + " 小时前";
		} else if (days <= 30) {
			return days + " 天前";
		} else {
			return month + " 月前";
		}

	}

	public static String getChatTime(long timesamp) {
		String result = "";
		long today = System.currentTimeMillis() / 1000; // 秒
		long other = timesamp / 1000;
		long daySeconds = 24 * 60 * 60;
		int temp = (int) (today / daySeconds) - (int) (other / daySeconds);
		switch (temp) {
			case 0:
				result = getHourAndMin(timesamp);
				break;
			case 1:
				result = "昨天 " + getHourAndMin(timesamp);
				break;
			case 2:
				result = "前天 " + getHourAndMin(timesamp);
				break;
			case 3:
			case 4:
			case 5:
				result = getDateToWeek(timesamp);
				break;

			default:
				result = getTime(timesamp);
				break;
		}

		return result;
	}

	/**
	 * 方法概述：通过时间戳格式化聊天时间
	 *
	 * @description 方法详细描述：
	 * @author 刘成伟（wwwlllll@126.com）
	 * @param @param timesamp
	 * @param @return
	 * @return String
	 * @throws
	 * @Title: DateUtil.java
	 * @Package com.huika.huixin.utils
	 * @date 2014-4-10 下午5:16:42
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getChatTime_old(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today)) - Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
			case 0:
				result = getHourAndMin(timesamp);
				break;
			case 1:
				result = "昨天 " + getHourAndMin(timesamp);
				break;
			case 2:
				result = "前天 " + getHourAndMin(timesamp);
				break;

			default:
				result = getTime(timesamp);
				break;
		}

		return result;
	}

	/**
	 *
	 * @description：根据格式去请求需要的时间戳
	 * @author zc
	 * @date 2015年6月16日 上午9:40:01
	 */
	public static String getTimeWithFormat(long timestamp, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(timestamp * 1000));
	}
	
	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	public static String getHourAndMin(String time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(getDateFromString(time));
	}

	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		return format.format(new Date(time));
	}

	public static String getTime(String time) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		return format.format(getDateFromString(time));
	}

	public static Date getDateFromLong(long time) {

		Date date = null;
		try {
			String timeStr = ymdhms.format(new Date(time));
			date = ymdhms.parse(timeStr);
		} catch (ParseException e) {}
		return date;

	}

	/**
	 * 将字符串转为时间戳 方法概述：
	 *
	 * @description 方法详细描述：
	 * @author lhy
	 * @param @param user_time
	 * @param @return
	 * @return String
	 * @throws
	 * @Title: DateUtil.java
	 * @Package com.huika.huixin.utils
	 * @date 2014-5-23 下午2:06:00
	 */
	public static long getTimestamp(String dateString) throws ParseException {
		Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
		long temp = date1.getTime();// JAVA的时间戳长度是13位
		return temp;
	}

	/**
	 * 将时间戳转为字符串 方法概述：
	 *
	 * @description 方法详细描述：
	 * @author lhy
	 * @param @param cc_time
	 * @param @return
	 * @return String
	 * @throws
	 * @Title: DateUtil.java
	 * @Package com.huika.huixin.utils
	 * @date 2014-5-23 下午2:06:09
	 */
	public static String getTimeStr(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	public static String getTimeStr2(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	public static String getPushMessageTime(String time) {
		return getChatTime(Long.parseLong(time + "000"));
	}

	@SuppressLint("SimpleDateFormat")
	public static String getShippintTime(String time) {
		Date otherDay = getDateFromString(time);

		return getChatTime(otherDay.getTime());
		/*
		 * String result = ""; SimpleDateFormat sdf = new SimpleDateFormat("dd"); Date today = new Date(System.currentTimeMillis()); Date otherDay = getDateFromString(time); int temp =
		 * Integer.parseInt(sdf.format(today)) - Integer.parseInt(sdf.format(otherDay)); switch (temp) { case 0: result = getHourAndMin(time); break; case 1: result = "昨天 " + getHourAndMin(time);
		 * break; case 2: case 3: case 4: case 5: result = getDateToWeek(time); break; default: result = getTime(time); break; } return result;
		 */
	}

	public static String getYMString(String timestr) {
		try {
			if (TextUtils.isEmpty(timestr)) { return ""; }
			timestr += "000";
			return getMonthByTimeStamp(timestr);
		} catch (Exception e) {}
		return "时间异常";
	}

	public static String getMonthByTimeStamp(String timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String monthString = sdf.format(Long.parseLong(timeStamp));
		return monthString;
	}

	public static String getSpecialTimeStamp(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm分");
		String monthString = sdf.format(new Date(timeStamp*1000));
		return monthString;
	}
	
	public static String getTradeTime(String timestr) {
		if (TextUtils.isEmpty(timestr)) { return ""; }
		long timesamp = Long.valueOf(timestr);
		return getTradeTime(timesamp * 1000);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getTradeTime(long timesamp) {
		Date otherDay = new Date(timesamp);

		return ymdhms.format(otherDay);
	}

	public static String getBCTTradeTime(String timestr) {
		if (TextUtils.isEmpty(timestr)) { return ""; }

		long timesamp = Long.valueOf(timestr);
		return getBCTTradeTime(timesamp * 1000);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getBCTTradeTime(long timesamp) {
		Date otherDay = new Date(timesamp);

		return ymd.format(otherDay);
	}

	/**
	 * @description：获取明天的时间和星期几
	 * @author shicm
	 * @date 2014年7月25日 下午2:27:22
	 */
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	public static String getTomorrowDate(long serverDate, int day) {

		Date date = new Date(serverDate * 1000);
		// 获取另外一天时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);

		// 另外一天的时间格式
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String tommorow = format.format(calendar.getTime());
		// 转化时间格式得到那一天的星期
		Date otherDay = getOtherDay(calendar);
		String week = getToWeek(ymdhms.format(otherDay));
		return tommorow + " " + week;
	}

	/**
	 * @description： 的另外一天
	 * @author shicm
	 * @date 2014年7月25日 下午4:22:28
	 */
	public static Date getOtherDay(Calendar calendar) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String otherTommrow = formatter.format(calendar.getTime());
		Date otherDay = null;
		try {
			otherDay = formatter.parse(otherTommrow);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return otherDay;
	}

	/**
	 * 得到服务器今天时间
	 *
	 * @description：
	 * @author shicm
	 * @date 2014年7月25日 下午4:22:45
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTodayTime(long serverDate) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		if (serverDate == 0) { return format.format(new Date()); }
		return format.format(new Date(serverDate * 1000));
	}

	/**
	 * 得到服务器今天时间
	 *
	 * @description：
	 * @author shicm
	 * @date 2014年7月25日 下午4:22:45
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getStandardServerTime(long serverDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (serverDate == 0) { return format.format(new Date()); }
		return format.format(new Date(serverDate * 1000));
	}

	/**
	 * 转化为星期
	 *
	 * @description：
	 * @author shicm
	 * @date 2014年7月25日 下午4:23:42
	 */
	public static String getToWeek(String date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDateFromString(date));
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayIndex < 1 || dayIndex > WEEKDAYS) { return null; }
		return WEEK[dayIndex - 1];
	}

	/**
	 * 获取今天是几月几日 周几
	 *
	 * @description：
	 * @author ldm
	 * @date 2014年8月4日 下午5:49:00
	 */
	public static String getTodayDate(boolean isShowWeek) {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		if (isShowWeek) {
			return hotelDf.format(date);
		} else {
			return hotelDetailDf.format(date);
		}

	}

	/**
	 * 获取明天是几月几日 周几
	 *
	 * @description：
	 * @author ldm
	 * @date 2014年8月4日 下午5:49:00
	 */
	public static String getNextDate(boolean isShowWeek) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, +1);
		Date date = calendar.getTime();
		if (isShowWeek) {
			return hotelDf.format(date);
		} else {
			return hotelDetailDf.format(date);
		}
	}
	
	/**
	 * 获取下一天是几月几日
	 *
	 * @description：
	 * @author ldm
	 * @date 2014年8月4日 下午5:49:00
	 */
	public static String getYesterdayDate(Calendar calendar) {
		calendar.add(Calendar.DAY_OF_YEAR, +1);
		Date date = calendar.getTime();
		return hotelDetailDf.format(date);
	}

	/**
	 * @description：获取昨天
	 * @author shicm
	 * @date 2014年8月14日 上午11:16:50
	 */
	public static Calendar getYesterdayDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return calendar;
	}

	/**
	 * @description：时间的处理
	 * @author samy
	 * @date 2014年7月11日 上午10:15:39
	 */
	public static String getTimeFromInt(long time) {
		if (time <= 0) { return "已结束"; }
		long day = time / (60 * 60 * 24);
		long hour = time / (60 * 60) % 24;
		long minute = time / 60 % 60;
		long second = time % 60;
		return day + "天 " + String.format("%02d",hour) + " : " + String.format("%02d",minute) + " : " + String.format("%02d",second);
//		return hour + "小时" + minute + "分" + second + "秒";
	}
	
	/**
	 * @description：时间的处理yyyy-MM-dd HH:mm:ss
	 * @author samy
	 * @date 2014年7月11日 上午10:15:39
	 */
	public static String getCountdownTimeMDHMS(long time) {
		if (time <= 0) { return "开始活动"; }
		long day = time / (60 * 60 * 24);
		long hour = time / (60 * 60) % 24;
		long minute = time / 60 % 60;
		long second = time % 60;
		return day + "天 " + String.format("%02d",hour) + " : " + String.format("%02d",minute) + " : " + String.format("%02d",second);
//		return hour + "小时" + minute + "分" + second + "秒";
	}
	
	public static String getCountdownTimeMDHMS2(long time) {
		if (time <= 0) { return "正在揭晓"; }
		long day = time / (60 * 60 * 24);
		long hour = time / (60 * 60) % 24;
		long minute = time / 60 % 60;
		long second = time % 60;
		return String.format("%02d",hour) + " : " + String.format("%02d",minute) + " : " + String.format("%02d",second);
//		return hour + "小时" + minute + "分" + second + "秒";
	}

	/**
	 * 时间处理（还剩下多少天,如果小于1天则按小时计算）,参数填写截止时间 与当前时间差值
	 * @description：
	 * @author ldm
	 * @date 2014年12月3日 上午11:25:55
	 */
	public static String getLastDays(long time) {
		if (time <= 0) { return "已结束"; }
		long day = time / (60 * 60 * 24);
		long hour = time / (60 * 60) % 24;
		long minute = time / 60 % 60;
		long second = time % 60;
		if (day > 0) {
			return day + "天";
		} else if(hour>0){
			return hour + "小时";
		}else if(minute>0){
			return minute + "分钟";
		}else {
			return second + "秒";
		}
	}

	public static String getLastDayses(long time) {
		if(time < 3600.0) {
			return (60.0 > time ?"1分钟前":getBigDecimal(time / 60.0) + "分钟前");
		} else if(time < 3600.0 * 24.0) {
			return getBigDecimal(time / (60.0 * 60.0)) + "小时前";
		} else if(time < 3600.0 * 24.0 * 30.0) {
			return getBigDecimal(time / (60.0 * 60.0 * 24.0)) + "天前";
		} else {
			return getBigDecimal(time / (60.0 * 60.0 * 24.0 * 30.0)) + "月前";
		}
	}
	
	private static BigDecimal getBigDecimal(double time) {
		return new BigDecimal(time).setScale(0, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 获取当前日期是星期几<br>
	 *
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	public String getDateComplete() { // 得到的是一个日期：格式为：yyyy年MM月dd日 HH时mm分ss秒SSS毫秒
		this.sdf2 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒SSS毫秒");
		return this.sdf2.format(new Date());// 将当前日期进行格式化操作
	}
}
