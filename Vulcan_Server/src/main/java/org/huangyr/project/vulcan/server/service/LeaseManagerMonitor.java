package org.huangyr.project.vulcan.server.service;


import org.huangyr.project.vulcan.server.common.Constants;

/*******************************************************************************
 *
 * @date 2019-12-18 3:50 PM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: 租约管理服务
 ******************************************************************************/
public class LeaseManagerMonitor implements Runnable {

    private long softLimit = Constants.LEASE_SOFTLIMIT_PERIOD;

    private long hardLimit = Constants.LEASE_HARDLIMIT_PERIOD;

    /**
     * 租约管理
     */
    @Override
    public void run() {

    }
}
