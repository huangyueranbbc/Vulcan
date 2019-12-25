package org.huangyr.project.vulcan.runner.server.service;

import org.huangyr.project.vulcan.runner.server.Runner;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-18 3:50 PM 
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
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
    public void run() {

    }
}
