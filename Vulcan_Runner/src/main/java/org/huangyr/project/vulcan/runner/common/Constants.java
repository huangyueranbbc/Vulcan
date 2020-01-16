package org.huangyr.project.vulcan.runner.common;

/*******************************************************************************
 *
 * @date 2019-12-18 2:22 PM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description:
 ******************************************************************************/
public class Constants {

    public static String SERVER_IP = "127.0.0.1";

    public static Integer SERVER_PORT = 8888;

    /**
     * 最小退避重连睡眠时间(ms)
     */
    public static long BASE_SLEEP_TIME = 1000;

    /**
     * 最大退避重连睡眠时间(ms)
     */
    public static long MAX_SLEEP_TIME = 60 * 1000;

    /**
     * 最大重试次数
     */
    public static int MAX_RETRIES = Integer.MAX_VALUE;

    /**
     * 入库线程数
     */
    public static Integer RUNNER_THREAD_NUM = 1;

}
