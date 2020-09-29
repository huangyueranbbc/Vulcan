package org.huangyr.project.vulcan.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/*******************************************************************************
 * @date 2020-02-20 1:29 PM
 * @author: <a href=mailto:@>黄跃然</a>
 * @Description: 项目配置文件初始化工具 封装配置相关方法
 ******************************************************************************/
@Slf4j
public class ConfigUtils {

    /**
     * 初始化项目日志文件
     */
    public static void initLog4j2Config() {
        log.info("start init log4j......");
        //读取Log4J2配置的路径
        File file = CommonUtils.getEnvFile("log4j.config");
        if (file.isFile()) {
            log.info("start load log4j external configuration. path:{}", file.getPath());
            // 如果存在该配置,进行加载，否则加载包内默认配置.
            URL url;
            try {
                url = file.toURI().toURL();
                //改变系统参数
                System.setProperty("log4j.configurationFile", url.toString());
                //重新初始化Log4j2的配置上下文
                LoggerContext context = (LoggerContext) LogManager.getContext(false);
                context.reconfigure();
                log.info("load external configuration finished. url:{}", url.toString());
            } catch (MalformedURLException e) {
                log.error("load log4j confi file error.");
            }
        } else {
            log.info("init default log4j config.");
        }
        log.info("log4j init finished.");
    }

}
