package org.huangyr.project.vulcan.runner.common;

import io.netty.bootstrap.Bootstrap;

import java.util.UUID;

/*******************************************************************************
 *
 * @date 2019-12-18 2:22 PM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description:
 ******************************************************************************/
public class Constants {

    /**
     * 配置中配置的runner name. TODO
      */
    public static final String RUNNER_NAME = "RUNNER_" + UUID.randomUUID().toString();

    public static String SERVER_IP = "10.8.8.228";

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

    public static Bootstrap client;

}
