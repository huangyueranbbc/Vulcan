package org.huangyr.project.vulcan.server.common;

import lombok.Data;
import org.huangyr.project.vulcan.common.DateUtils;

/*******************************************************************************
 * @date 2020-09-11 11:11 AM
 * @author: <a href=mailto:@>黄跃然</a>
 * @Description: 租约
 *
 ******************************************************************************/
@Data
public class Lease {

    /**
     * 租约持有者
     */
    private final String holder;

    /**
     * 上一次更新租约的时间
     */
    private long latestTime = 0;

    /**
     * 租约存放的路径,文件锁
     */
    private String leasePath;

    public Lease(String holder, String lastestTime, String leasePath) {
        this.holder = holder;
        this.leasePath = leasePath;
        renew();
    }

    private void renew() {
        this.latestTime = DateUtils.now();
    }


}
