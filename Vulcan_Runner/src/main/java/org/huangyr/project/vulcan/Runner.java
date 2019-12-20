package org.huangyr.project.vulcan;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.PropertyConfigurator;
import org.huangyr.project.vulcan.common.Constants;
import org.huangyr.project.vulcan.common.StartupOption;
import org.huangyr.project.vulcan.net.client.HeartSocketClient;
import org.huangyr.project.vulcan.service.HeartService;
import org.huangyr.project.vulcan.service.LeaseManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import static org.huangyr.project.vulcan.common.ThreadPool.*;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-18 1:47 PM 
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: Runner服务
 * {@link Runner#main Runner服务启动入口}
 * {@link Runner#shutdown()} Runner服务停止}
 * {@link Runner#join()} Runner服务挂起}
 * {@link Runner#shouldRun} Runner服务是否运行信号量}
 * {@link Runner#initRunner()} 初始化Runner服务，初始化并启动心跳检测线程和租约管理线程,建立和Server的通信连接}
 * @see HeartService#run()  心跳线程
 * @see LeaseManagerService#run()  租约管理线程
 ******************************************************************************/
public class Runner implements Runnable {

    /**
     * 静态初始化日志
     */
    static {
        Properties properties = new Properties();
        try {
            properties.load(Runner.class.getClassLoader().getResourceAsStream("log4j.properties"));
            String time = FastDateFormat.getInstance("yyyy-MM-dd-HH").format(new Date());
            String logDir = System.getenv("VULCAN_LOG_DIR") + "/vulcan/runner/";
            properties.setProperty("log4j.appender.FILE_INFO.File", logDir + "Vulcan_Runner_INFO_" + time + ".LOG");
            properties.setProperty("log4j.appender.FILE_WARN.File", logDir + "Vulcan_Runner_WARN_" + time + ".LOG");
            properties.setProperty("log4j.appender.FILE_ERROR.File", logDir + "Vulcan_Runner_ERROR_" + time + ".LOG");
            properties.setProperty("log4j.appender.FILE_DEBUG.File", logDir + "Vulcan_Runner_DEBUG_" + time + ".LOG");
        } catch (IOException e) {
            System.out.println("load log4j.properties exception." + e.fillInStackTrace());
            System.exit(-1);
        }
        PropertyConfigurator.configure(properties);
    }

    private static Logger log = LoggerFactory.getLogger(Runner.class);

    /**
     * runner是否结束信号
     */
    public volatile boolean shouldRun = true;

    private Runner() {
    }

    /**
     * Runner线程处理
     */
    @Override
    public void run() {
        while (shouldRun) {
            log.info("the runner is run...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void join() {
        while (shouldRun) {
            try {
                synchronized (this) {
                    wait(6000);
                }
            } catch (InterruptedException ex) {
                log.warn("Received exception in Runner#join: " + ex);
            }
        }
    }

    private static Runner createRunner(String[] args) {
        StartupOption startOpt = StartupOption.parseArguments(args);
        if (startOpt == null) {
            printUsage();
            System.exit(-2);
        }

        Runner runner = initRunner();
        // 启动Runner线程
        startRunner(runner);
        return runner;
    }

    /**
     * 初始化Runner
     *
     * @return
     */
    private static Runner initRunner() {
        Runner runner = new Runner();
        // 创建和Server通信的Socket连接
        HeartSocketClient heartSocketClient = new HeartSocketClient(8888, "127.0.0.1", 1, false);
        heartSocketClient.start();
        // 初始化心跳请求线程
        heartThreadPool.execute(new HeartService(runner, heartSocketClient));
        lmThreadPool.execute(new LeaseManagerService(runner));
        return runner;
    }

    /**
     * 开启Runner工作线程
     *
     * @param runner
     */
    private static void startRunner(Runner runner) {
        // 启动runner线程
        for (int i = 1; i <= Constants.RUNNER_THREAD_NUM; i++) {
            runnerThreadPool.execute(runner);
            log.debug("runner is run. num:{}", i);
        }
        log.info("runner is start! runner num:{}", Constants.RUNNER_THREAD_NUM);
    }

    /**
     * 获取runner机器上当前时间
     */
    public long now() {
        return System.currentTimeMillis();
    }

    private static void printUsage() {
        System.err.println(
                "Usage: java Runner [ " +
                        StartupOption.START.getName() + " [" + StartupOption.UPGRADE.getName() +
                        " ] ]");
    }

    /**
     * stop the runner
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
            Runner runner = createRunner(args);
            if (runner != null) {
                log.info("runner is start......");
                runner.join();
            } else {
                errorCode = -1;
            }
        } catch (Throwable e) {
            log.error("Exception in secureMain", e);
            System.exit(errorCode);
        } finally {
            log.warn("Exiting runner");
            System.exit(errorCode);
        }
    }
}
