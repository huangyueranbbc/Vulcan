package org.huangyr.project.vulcan.server.service;


import org.huangyr.project.vulcan.server.common.Constants;
import org.huangyr.project.vulcan.server.common.Lease;

import java.util.SortedSet;
import java.util.TreeSet;

/*******************************************************************************
 *
 * @date 2019-12-18 3:50 PM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: 租约管理服务
 ******************************************************************************/
public class LeaseManager {

    private long softLimit = Constants.LEASE_SOFTLIMIT_PERIOD;

    private long hardLimit = Constants.LEASE_HARDLIMIT_PERIOD;

    private SortedSet<Lease> sortedLeases = new TreeSet<Lease>();


}
