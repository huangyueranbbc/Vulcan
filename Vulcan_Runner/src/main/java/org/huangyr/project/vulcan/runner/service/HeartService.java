package org.huangyr.project.vulcan.runner.service;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.huangyr.project.vulcan.proto.VulcanHeartPackage;
import org.huangyr.project.vulcan.runner.Runner;
import org.huangyr.project.vulcan.common.DateUtils;
import org.huangyr.project.vulcan.runner.net.client.HeartSocketClient;
import org.huangyr.project.vulcan.runner.net.client.handler.ServerSocketCommandHandler;
import org.huangyr.project.vulcan.common.VulcanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*******************************************************************************
 *
 * @date 2019-12-18 2:45 PM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: 心跳服务
 * {@link HeartService#heartSocketClient 建立和Server的双通道长连接,用于心跳和接收Server发送的指令}
 * {@link HeartService#runner runner通过该类发送心跳}
 *
 *  @see Runner#initRunner() runner初始化并启动 {@link HeartService} 服务
 ******************************************************************************/
public class HeartService implements Runnable {

    private static Logger log = LoggerFactory.getLogger(HeartService.class);

    private Runner runner;
    private HeartSocketClient heartSocketClient;

    /**
     * 发送心跳间隔
     */
    private static long HEART_BEAT_INTERVAL = 3 * 1000L;

    /**
     * 上一次发送心跳时间
     */
    private static long LAST_HEART_BEAT = 0;

    public HeartService(Runner runner, HeartSocketClient heartSocketClient) {
        this.runner = runner;
        this.heartSocketClient = heartSocketClient;
    }

    @Override
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
     * {@link HeartService#offerservice() Runner发送心跳给Server 返回心跳结果和Server下发的指令等核心业务通过ServerSocketCommandHandler来进行处理}
     *
     * @see ServerSocketCommandHandler#channelRead(ChannelHandlerContext, Object)
     */
    private void offerservice() {

        while (runner.shouldRun) {
            try {
                long startTime = runner.now();

                // 达到心跳间隔 发送心跳
                if (startTime - LAST_HEART_BEAT > HEART_BEAT_INTERVAL) {
                    log.info("start send heart beat! now:{}", DateUtils.stampToDate(startTime));
                    LAST_HEART_BEAT = startTime;
                    // 发送心跳 心跳结果和所有Server指令再Handler里处理
                    sendHeartbeat();
                }

            } catch (Exception e) {
                log.error("offerservice has error.", e);
            }
        }

    }

    /**
     * Runner发送心跳包给Server
     *
     * @return
     */
    private void sendHeartbeat() {
        VulcanHeartPackage.Builder heartBuilder = VulcanHeartPackage.newBuilder();
        heartBuilder.setIp(VulcanUtils.getHostname());
        heartBuilder.setHeartTime(runner.now());
        heartBuilder.setMessage("我发送心跳啦!");
        VulcanHeartPackage heartPackage = heartBuilder.build();
        ByteBuf pb = Unpooled.copiedBuffer(heartPackage.toByteArray());
        heartSocketClient.sendMessage(pb);
    }
}
