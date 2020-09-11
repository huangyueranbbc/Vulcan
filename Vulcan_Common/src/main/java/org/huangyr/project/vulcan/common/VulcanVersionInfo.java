package org.huangyr.project.vulcan.common;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class VulcanVersionInfo {

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
            log.warn("FAILED TO GET VERSION INFO.");
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
        return IOUtils.readStream(versionInputStream);
    }



}
