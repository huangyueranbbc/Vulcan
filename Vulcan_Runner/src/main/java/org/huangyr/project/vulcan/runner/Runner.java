package org.huangyr.project.vulcan.runner;

import lombok.extern.slf4j.Slf4j;
import org.huangyr.project.vulcan.common.ConfigUtils;
import org.huangyr.project.vulcan.common.Global;
import org.huangyr.project.vulcan.runner.common.Constants;
import org.huangyr.project.vulcan.runner.common.StartupOption;
import org.huangyr.project.vulcan.runner.net.client.HeartSocketClient;
import org.huangyr.project.vulcan.runner.service.HeartService;
import org.huangyr.project.vulcan.runner.service.LeaseMonitor;

import static org.huangyr.project.vulcan.runner.common.ThreadPool.*;

/*******************************************************************************
 *
 * @date 2019-12-18 1:47 PM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: Runner服务
 * Runner服务启动入口{@link Runner#main}
 * Runner服务停止{@link Runner#shutdown()}
 * Runner服务挂起{@link Runner#join()}
 * Runner服务是否运行信号量{@link Runner#shouldRun}
 * 初始化Runner服务，初始化并启动心跳检测线程和租约管理线程,建立和Server的通信连接{@link Runner#initRunner()}
 * @see HeartService#run()  心跳线程
 * @see LeaseMonitor#run()  租约管理线程
 *
 * 公用全局变量放到{@link Global}
 * 私有全局变量放到{@link Constants}
 ******************************************************************************/
@Slf4j
public class Runner implements Runnable {

    /*
     * 日志初始化
     */
    static {
        ConfigUtils.initLog4j2Config();
    }

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
        HeartSocketClient heartSocketClient = new HeartSocketClient(Constants.SERVER_IP, Constants.SERVER_PORT, 1, Constants.BASE_SLEEP_TIME, Constants.MAX_RETRIES, Constants.MAX_SLEEP_TIME, false);
        heartSocketClient.start();
        // 初始化心跳请求线程
        heartThreadPool.execute(new HeartService(runner, heartSocketClient));
        // 租约监听,如果到期,程序会退出服务。
        lmThreadPool.execute(new LeaseMonitor(runner));
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
