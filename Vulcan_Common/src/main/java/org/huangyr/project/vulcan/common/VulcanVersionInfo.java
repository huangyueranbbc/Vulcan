package org.huangyr.project.vulcan.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******************************************************************************
 *
 * @date 2019-06-06 16:05
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: DataCenterService版本信息
 ******************************************************************************/
public class VulcanVersionInfo {

    private static final Logger logger = LoggerFactory.getLogger(VulcanVersionInfo.class);

    private static final String VERSION = "version:";
    private static final String DATATIME = "datetime:";
    private static final String VCSVERSION = "vcs-version:";

    private static String versionInfo;

    private static String version_str = "";
    private static String date_time_str = "";
    private static String vcs_version_str = "";

    static {
        try {
            versionInfo = getVersionInfo();
            if (versionInfo != null) {
                versionInfo = versionInfo.trim();
                versionInfo = versionInfo.replaceAll("\n", "");
                Pattern version = Pattern.compile(VERSION);
                Pattern datetime = Pattern.compile(DATATIME);
                Pattern vcs_version = Pattern.compile(VCSVERSION);
                Matcher version_matcher = version.matcher(versionInfo);
                Matcher datetime_matcher = datetime.matcher(versionInfo);
                Matcher vcs_version_matcher = vcs_version.matcher(versionInfo);

                if (version_matcher.find() && datetime_matcher.find() && vcs_version_matcher.find()) {
                    version_str = versionInfo.substring(version_matcher.end(), datetime_matcher.start());
                    date_time_str = versionInfo.substring(datetime_matcher.end(), vcs_version_matcher.start());
                    vcs_version_str = versionInfo.substring(vcs_version_matcher.end());
                }
            }
        } catch (Exception e) {
            // don't need print exception
            logger.warn("FAILED TO GET VERSION INFO.");
        }
    }

    /**
     * get project version
     *
     * @return
     */
    public static String getProjectVersion() {
        return !version_str.isEmpty() ? version_str : "Unknown";
    }

    /**
     * get compiled time
     *
     * @return
     */
    public static String getCompiledTime() {
        return !date_time_str.isEmpty() ? date_time_str : "Unknown";
    }

    /**
     * Get the git revision number for the root directory
     *
     * @return the revision number, eg. "451451"
     */
    public static String getVCSVersion() {
        return !vcs_version_str.isEmpty() ? vcs_version_str : "Unknown";
    }

    private static String getVersionInfo() throws IOException {
        InputStream versionInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("version_info.txt");
        return readStream(versionInputStream);
    }

    /**
     * 读取 InputStream 到 String字符串中
     */
    public static String readStream(InputStream in) throws IOException {
            //<1>创建字节数组输出流，用来输出读取到的内容
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //<2>创建缓存大小
            byte[] buffer = new byte[1024]; // 1KB
            //每次读取到内容的长度
            int len = -1;
            //<3>开始读取输入流中的内容
            while ((len = in.read(buffer)) != -1) { //当等于-1说明没有数据可以读取了
                baos.write(buffer, 0, len);   //把读取到的内容写到输出流中
            }
            //<4> 把字节数组转换为字符串
            String content = baos.toString();
            //<5>关闭输入流和输出流
            in.close();
            baos.close();
            //<6>返回字符串结果
            return content;
    }

}
