package org.huangyr.project.vulcan.common;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.PropertyConfigurator;
import org.huangyr.project.vulcan.common.common.StartupOption;
import org.huangyr.project.vulcan.common.net.http.HttpServer;
import org.huangyr.project.vulcan.common.net.tcp.HeartServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-16 10:57 AM 
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: Server服务端
 ******************************************************************************/
public class Server {

    /**
     * 静态初始化日志
     */
    static {
        Properties properties = new Properties();
        try {
            properties.load(Server.class.getClassLoader().getResourceAsStream("log4j.properties"));
            String time = FastDateFormat.getInstance("yyyy-MM-dd-HH").format(new Date());
            String logDir = System.getenv("VULCAN_LOG_DIR") + "/vulcan/server/";
            properties.setProperty("log4j.appender.FILE_INFO.File", logDir + "Vulcan_Server_INFO_" + time + ".LOG");
            properties.setProperty("log4j.appender.FILE_WARN.File", logDir + "Vulcan_Server_WARN_" + time + ".LOG");
            properties.setProperty("log4j.appender.FILE_ERROR.File", logDir + "Vulcan_Server_ERROR_" + time + ".LOG");
            properties.setProperty("log4j.appender.FILE_DEBUG.File", logDir + "Vulcan_Server_DEBUG_" + time + ".LOG");
        } catch (IOException e) {
            System.out.println("load log4j.properties exception." + e.fillInStackTrace());
            System.exit(-1);
        }
        PropertyConfigurator.configure(properties);
    }

    private static Logger log = LoggerFactory.getLogger(Server.class);

    /**
     * server运行信号量
     */
    private volatile boolean shouldRun = true;

    private Server() {
    }

    private Server(boolean shouldRun) {
        this.shouldRun = shouldRun;
        initServer(); // 初始化Server
        new Server();
    }

    private void join() {
        while (shouldRun) {
            try {
                synchronized (this) {
                    wait(6000);
                }
                log.debug("SERVER IS RUNNING......");
            } catch (InterruptedException ex) {
                log.warn("Received exception in Server#join: " + ex);
            }
        }
    }

    private static Server createServer(String[] args) {
        StartupOption startOpt = StartupOption.parseArguments(args);
        if (startOpt == null) {
            printUsage();
            System.exit(-2);
        }

        switch (startOpt) {
            case START:
                log.debug("server will start");
                break;
            case UPGRADE:
                log.debug("server will upgrade");
                break;
            default:
        }

        return new Server(true);
    }

    private static void initServer() {
        try {
            // TODO 参数都改为配置项
            HttpServer httpServer = new HttpServer(8080, 1, 1, false);
            httpServer.start();
            // 心跳服务
            HeartServer heartServer = new HeartServer(8888, 1, 1, false);
            heartServer.start();


        } catch (Exception e) {
            log.error("initNameNode is error");
        }

    }

    private static void printUsage() {
        System.err.println(
                "Usage: java Server [ " +
                        StartupOption.START.getName() + " [" + StartupOption.UPGRADE.getName() +
                        " ] ]");
    }

    /**
     * stop the server
     */
    public synchronized void shutdown() {
        log.info("start shutdown......");
        shouldRun = false;
    }

    /**
     * 服务初始化启动
     *
     * @param args
     */
    public static void main(String[] args) {
        // args=new String[]{"-format"};
        int errorCode = 0;
        try {
            Server server = createServer(args);
            if (server != null) {
                log.info("server is start......");
                server.join();
            } else {
                errorCode = -1;
            }
        } catch (Throwable e) {
            log.error("Exception in secureMain", e);
            System.exit(errorCode);
        } finally {
            log.warn("Exiting server");
            System.exit(errorCode);
        }

    }
}
