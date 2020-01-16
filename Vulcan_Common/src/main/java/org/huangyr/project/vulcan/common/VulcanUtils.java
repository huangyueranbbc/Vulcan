package org.huangyr.project.vulcan.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*******************************************************************************
 *
 * @date 2019-12-19 1:48 PM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
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
