package org.huangyr.project.vulcan.status;

import lombok.Data;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2020-08-28 9:20 AM 
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: Server中存放的Runner的信息，根据ip和端口进行区分 TODO 暂定
 ******************************************************************************/
@Data
public class RunnerInfo {

    String name;

    String ip;

    Integer port;

    /**
     * identifying,sha1加密
     */
    String identifying;

    /**
     * 最新的心跳时间
     */
    long lastHeartTime = 0;

    RunnerStatus runnerStatus= RunnerStatus.DEAD;

}
