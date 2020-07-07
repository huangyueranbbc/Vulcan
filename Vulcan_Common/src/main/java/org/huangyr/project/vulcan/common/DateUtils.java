package org.huangyr.project.vulcan.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*******************************************************************************
 *
 * @date 2019-12-20 2:14 PM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description:
 ******************************************************************************/
public class DateUtils {

    /**
     * 日期默认格式化
     */
    private static ThreadLocal<SimpleDateFormat> dateFormats = new ThreadLocal<SimpleDateFormat>();

    /**
     * 默认的格式
     */
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前运行机器上当前时间
     */
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * 按照 yyyy-MM-dd HH:mm:ss 格式化字符串
     *
     * @param date
     * @return
     */
    public static String dateFormat(Date date) {
        return dateFormat(date, DEFAULT_PATTERN);
    }

    /**
     * 格式化字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateFormat(Date date, String pattern) {
        SimpleDateFormat format = dateFormats.get();
        if (format == null) {
            format = new SimpleDateFormat();
            dateFormats.set(format);
        }
        format.applyPattern(pattern);
        String resultDate = format.format(date);
        dateFormats.remove(); // 防止内存泄漏
        return resultDate;
    }

    /**
     * 按照 yyyy-MM-dd HH:mm:ss 格式化字符串
     *
     * @param date
     * @return
     */
    public static String dateToTimeStamp(String date) throws ParseException {
        return dateToTimeStamp(date, DEFAULT_PATTERN);
    }

    /**
     * 按照 yyyy-MM-dd HH:mm:ss 格式化字符串
     *
     * @param date
     * @return
     */
    public static String timeStampToDate(Long date) {
        return timeStampToDate(date, DEFAULT_PATTERN);
    }

    /**
     * 根据格式化时间字符串返回时间戳
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToTimeStamp(String date, String pattern) throws ParseException {
        SimpleDateFormat format = dateFormats.get();
        if (format == null) {
            format = new SimpleDateFormat();
            dateFormats.set(format);
        }
        format.applyPattern(pattern);
        Date dateResult = format.parse(date);
        long ts = dateResult.getTime();
        String resTimeStamp = String.valueOf(ts);
        dateFormats.remove(); // 防止内存泄漏
        return resTimeStamp;
    }

    /**
     * 将时间戳转换为时间
     * @param timestamp
     * @return
     */
    public static String timeStampToDate(Long timestamp,String pattern) {
        SimpleDateFormat format = dateFormats.get();
        if (format == null) {
            format = new SimpleDateFormat();
            dateFormats.set(format);
        }
        format.applyPattern(pattern);
        String res;
        Date date = new Date(timestamp);
        res = format.format(date);
        return res;
    }

}
