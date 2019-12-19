import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.PropertyConfigurator;
import org.huangyr.project.vulcan.common.SocketUtils;
import org.huangyr.project.vulcan.common.Server;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-18 10:03 AM 
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description:
 ******************************************************************************/
public class T1 {
    /**
     * 静态初始化日志
     */
    static {
        Properties properties = new Properties();
        try {
            properties.load(Server.class.getClassLoader().getResourceAsStream("log4j.properties"));
            String time = FastDateFormat.getInstance("yyyy-MM-dd-HH").format(new Date());
            String logDir = System.getenv("VULCAN_LOG_DIR") + "/vulcan/server/";
            properties.setProperty("log4j.appender.FILE_INFO.File", logDir + "Vulcan_Server_INFO_" + time + ".log");
            properties.setProperty("log4j.appender.FILE_WARN.File", logDir + "Vulcan_Server_WARN_" + time + ".log");
            properties.setProperty("log4j.appender.FILE_ERROR.File", logDir + "Vulcan_Server_ERROR_" + time + ".log");
        } catch (IOException e) {
            System.out.println("load log4j.properties exception." + e.fillInStackTrace());
            System.exit(-1);
        }
        PropertyConfigurator.configure(properties);
    }

    public static void main(String[] args) {
        String response = SocketUtils.sendSocketKeepAliveMessage("127.0.0.1", 8888, "你好啊", "UTF-8");
        System.out.println(response);
    }

}
