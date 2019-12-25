package org.huangyr.project.vulcan.runner.server.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-19 1:48 PM 
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: 工具包 不知道取啥名 就先叫这个
 ******************************************************************************/
public class VulcanUtils {

    /**
     * Return hostname without throwing exception.
     *
     * @return hostname
     */
    public static String getHostname() {
        try {
            return "" + InetAddress.getLocalHost();
        } catch (UnknownHostException uhe) {
            return "" + uhe;
        }
    }

}
