package org.huangyr.project.vulcan.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.net.InetAddress;

/*******************************************************************************
 * @date 2020-02-20 12:53 PM
 * @author: <a href=mailto:@>黄跃然</a>
 * @Description: 工具类
 ******************************************************************************/
@Slf4j
public class CommonUtils {

    /**
     * 根据环境变量中的路径信息加载文件
     *
     * @param envName
     * @return
     */
    public static File getEnvFile(String envName) {
        //读取文件的路径
        String filePath = String.valueOf(System.getProperty(envName));
        return new File(filePath);
    }

    /**
     * 获取服务器IP信息
     */
    public static String getServerIp() {
        String ip = "Unknown";
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            ip = localHost.getHostAddress();
        } catch (Exception e) {
            log.warn("get local host failed.");
        }
        return ip;
    }

    public static String md5String(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String sha1String(String str) {
        return DigestUtils.sha1Hex(str);
    }

    /**
     * 获取机器的标识
     */
    public static String getIdentifying(String nodeName, String ip, int port) {
        return sha1String(nodeName + ip + port);
    }
}
