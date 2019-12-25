package org.huangyr.project.vulcan.runner.server.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-20 2:14 PM 
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description:
 ******************************************************************************/
public class DateUtils {

    /**
     * 获取当前运行机器上当前时间
     */
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * 将时间转换为时间戳
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static String dateToStamp(String dateStr) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(dateStr);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 将时间戳转换为时间
     * @param timestamp
     * @return
     */
    public static String stampToDate(Long timestamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timestamp);
        res = simpleDateFormat.format(date);
        return res;
    }
}
