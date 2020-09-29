package org.huangyr.project.vulcan.status;

import lombok.Data;

/*******************************************************************************
 * @date 2020-08-28 9:20 AM
 * @author: <a href=mailto:@>黄跃然</a>
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
