package org.huangyr.project.vulcan.runner.server.net.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import org.huangyr.project.vulcan.runner.server.net.client.HeartSocketClient;
import org.huangyr.project.vulcan.runner.server.net.common.RetryPolicy;

import java.util.concurrent.TimeUnit;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-18 2:45 PM
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: 重连处理器 当检测到channel断开，会启动重连策略进行重连
 *
 * {@link RetryPolicy 重试策略接口}
 ******************************************************************************/
@ChannelHandler.Sharable
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

    private int retries = 0;
    private RetryPolicy retryPolicy;

    private HeartSocketClient heartSocketClient;

    public ReconnectHandler(HeartSocketClient heartSocketClient) {
        this.heartSocketClient = heartSocketClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Successfully established a connection to the server.");
        retries = 0;
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (retries == 0) {
            System.err.println("Lost the TCP connection with the server.");
            ctx.close();
        }

        boolean allowRetry = getRetryPolicy().allowRetry(retries);
        if (allowRetry) {

            long sleepTimeMs = getRetryPolicy().getSleepTimeMs(retries);

            System.out.println(String.format("Try to reconnect to the server after %dms. Retry count: %d.", sleepTimeMs, ++retries));

            final EventLoop eventLoop = ctx.channel().eventLoop();
            eventLoop.schedule(() -> {
                System.out.println("Reconnecting ...");
                heartSocketClient.connect();
            }, sleepTimeMs, TimeUnit.MILLISECONDS);
        }
        ctx.fireChannelInactive();
    }


    private RetryPolicy getRetryPolicy() {
        if (this.retryPolicy == null) {
            this.retryPolicy = heartSocketClient.getRetryPolicy();
        }
        return this.retryPolicy;
    }
}

