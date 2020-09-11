package org.huangyr.project.vulcan.server.common;

import org.huangyr.project.vulcan.status.RunnerInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*******************************************************************************
 *
 * @date 2019-12-25 10:07 AM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description:
 ******************************************************************************/
public class Constants {

    /**
     * 软超时
     */
    public static final long LEASE_SOFTLIMIT_PERIOD = 60 * 1000;;
    /**
     * 硬超时
     */
    public static final long LEASE_HARDLIMIT_PERIOD =60 * LEASE_SOFTLIMIT_PERIOD;

    /**
     * 存放连接的runner信息
     */
    public static Map<Object, RunnerInfo> runnerInfoMap = new ConcurrentHashMap<>();

}
