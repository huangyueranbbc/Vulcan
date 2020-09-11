package org.huangyr.project.vulcan.server;

import lombok.extern.slf4j.Slf4j;
import org.huangyr.project.vulcan.common.ConfigUtils;
import org.huangyr.project.vulcan.common.Global;
import org.huangyr.project.vulcan.runner.Runner;
import org.huangyr.project.vulcan.server.common.Constants;
import org.huangyr.project.vulcan.server.common.StartupOption;
import org.huangyr.project.vulcan.server.dag.DAG;
import org.huangyr.project.vulcan.server.net.http.HttpServer;
import org.huangyr.project.vulcan.server.net.tcp.HeartServer;
import org.huangyr.project.vulcan.server.service.LeaseManagerService;

import static org.huangyr.project.vulcan.runner.common.ThreadPool.lmThreadPool;

/*******************************************************************************
 *
 * @date 2019-12-16 10:57 AM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: Server服务端
 * Server服务启动入口{@link Server#main}
 * Server服务停止{@link Server#shutdown()}}
 * Server服务挂起{@link Server#join()}}
 * Server服务是否运行信号量{@link Server#shouldRun}
 * 初始化Server服务，启动Server线程，启动Socket心跳和http服务{@link Server#initServer()}
 * @see HttpServer http服务,主要用于web通信{@link Server#httpServer}
 * @see HeartServer socket服务,主要用于处理心跳，汇总runner运行状况,以及根据连接下发指令给Runner{@link Server#heartServer}
 * @see DAG 通过DAG有向无环图进行任务扫描算法
 * @see Runner 服务
 * @see LeaseManagerService#run()  租约管理线程
 *
 * 公用全局变量放到{@link Global}
 * 私有全局变量放到{@link Constants}
 *******************vulcan***********************************************************/
@Slf4j
public class Server {

    /*
     * 日志初始化
     */
    static {
        ConfigUtils.initLog4j2Config();
    }

    private static HttpServer httpServer;

    private static HeartServer heartServer;

    /**
     * server运行信号量
     */
    private volatile boolean shouldRun = true;

    private Server() {
    }

    private Server(boolean shouldRun) {
        this.shouldRun = shouldRun;
        initServer(); // 初始化Server
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
            httpServer = new HttpServer(8080, 1, 1, false);
            httpServer.start();
            // 心跳服务
            heartServer = new HeartServer(8888, 1, 1, false);
            heartServer.start();

            // 租约管理线程
            // TODO 1、TreeSet存放每个Lease(<holder,leaseinfo>
            // TODO 2、定时器不断统计每个服务的lease状况，如果超时，移除服务(removed)
            // TODO 3、当服务连接时，根据服务租约使用情况来更新租约。 每个服务也会拿到自己的租约情况，根据软超时和硬超时来确认自己的服务状态是否有效。
            // TODO 4、如果软超时，Runner会进行重新加入。如果硬超时，Runner会知道自己已经被移除出服务，会关闭自身。(暂定)
            lmThreadPool.execute(new LeaseManagerService());
            // TODO JOB扫描任务

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
