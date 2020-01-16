package org.huangyr.project.vulcan.runner.service;


import org.huangyr.project.vulcan.runner.Runner;

/*******************************************************************************
 *
 * @date 2019-12-18 3:50 PM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: 租约管理服务
 ******************************************************************************/
public class LeaseManagerService implements Runnable {

    private Runner runner;

    public LeaseManagerService(Runner runner) {
        this.runner = runner;
    }

    /**
     * 租约管理
     */
    @Override
    public void run() {

    }
}
