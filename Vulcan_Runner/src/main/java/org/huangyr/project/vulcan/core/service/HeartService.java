package org.huangyr.project.vulcan.core.service;

import org.huangyr.project.vulcan.core.MessageUtils;
import org.huangyr.project.vulcan.core.Runner;
import org.huangyr.project.vulcan.core.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-18 2:45 PM 
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: 心跳服务
 ******************************************************************************/
public class HeartService implements Runnable {

    private static Logger log = LoggerFactory.getLogger(HeartService.class);

    private Runner runner;

    /**
     * 发送心跳间隔
     */
    private long heartBeatInterval = 3 * 1000L;

    /**
     * 上一次发送心跳时间
     */
    private long lastHeartbeat = 0;

    public HeartService(Runner runner) {
        this.runner = runner;
    }

    public void run() {
        log.info("HeartService is running......");
        // TODO 核心业务 上报心跳、处理rpc请求、处理数据......

        while (runner.shouldRun) {
            try {
                // runner 核心处理逻辑
                offerservice();
            } catch (Exception e) {
                log.error("runner run has error.", e);
                runner.shutdown(); // stop the runner
            }
        }

    }

    /**
     * 核心处理
     */
    private void offerservice() {

        while (runner.shouldRun) {
            try {
                long startTime = runner.now();

                // 达到心跳间隔 发送心跳
                if (startTime - lastHeartbeat > heartBeatInterval) {
                    log.info("start send heart beat! now:{}", startTime);
                    lastHeartbeat = startTime;
                    // TODO 命令模式 发送心跳
                    String response = sendHeartbeat();
                    log.info("get response:{}", response);
//                // TODO 远程调用 上报心跳 datanode和namenode唯一通信方式
//                DatanodeCommand[] datanodeCommands = nameNode.sendHeartbeat(datanodeRegInfo);
//
//                // 处理namenode下达的指令 失败则等待下一次心跳通信
//                if (!processCommand(datanodeCommands)) {
//                    continue;
//                }
                }

            } catch (Exception e) {
                log.error("offerservice has error.", e);
            }
        }

    }

    private String sendHeartbeat() {
        return MessageUtils.sendSocketKeepAliveMessage(Constants.SERVER_IP, Constants.SERVER_PORT, "啊啊啊", "UTF-8");
    }
}
