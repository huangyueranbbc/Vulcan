package org.huangyr.project.vulcan.runner.net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.huangyr.project.vulcan.runner.common.Constants;
import org.huangyr.project.vulcan.common.Global;
import org.huangyr.project.vulcan.runner.net.client.handler.ReconnectHandler;
import org.huangyr.project.vulcan.runner.net.client.handler.ServerSocketCommandHandler;
import org.huangyr.project.vulcan.runner.net.common.ExponentialBackOffRetry;
import org.huangyr.project.vulcan.runner.net.common.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;

/*******************************************************************************
 *
 * @date 2019-12-16 10:55 AM
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: Socket长连接客户端
 *
 * 公用全局变量放到{@link Global}
 * 私有全局变量放到{@link Constants}
 ******************************************************************************/
@Slf4j
public class HeartSocketClient {

    private int port;
    private String host;
    private SocketChannel socketChannel;
    private boolean userEPoll = true;
    private Bootstrap client;
    private EventLoopGroup eventLoopGroup;
    private int eventLoopGroupThreadNum = 0;

    private ReconnectHandler reconnectHandler;

    private long baseSleepTime;
    private int maxRetries;
    private long maxSleepTime;

    /**
     * 指数退避重连算法
     */
    private RetryPolicy retryPolicy;

    public HeartSocketClient(String host, int port, int eventLoopGroupThreadNum, long baseSleepTime, int maxRetries, long maxSleepTime, boolean userEPoll) {
        this.port = port;
        this.host = host;
        this.eventLoopGroupThreadNum = eventLoopGroupThreadNum;
        this.userEPoll = userEPoll;
        this.baseSleepTime = baseSleepTime;
        this.maxRetries = maxRetries;
        this.maxSleepTime = maxSleepTime;
        reconnectHandler = new ReconnectHandler(this);
        retryPolicy = new ExponentialBackOffRetry(this.baseSleepTime, this.maxRetries, this.maxSleepTime);
    }

    public void start() {
        init();
        client = new Bootstrap();
        try {
            // 绑定处理group
            client.group(eventLoopGroup).remoteAddress(host, port)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            // 初始化Runner处理器 处理Server下发的命令
                            socketChannel.pipeline().addLast(reconnectHandler)
                                    .addLast(new ServerSocketCommandHandler());
                        }
                    });
            initChannelOption();
            // 进行连接
            connect();
        } catch (Exception e) {
            log.error("heartsocketclient start has error.",e);
        }
    }

    public void connect() {
        ChannelFuture future;
        future = client.connect(host, port);
        future.addListener(getConnectionListener());
        socketChannel = (SocketChannel) future.channel();
    }

    private ChannelFutureListener getConnectionListener() {
        return new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    future.channel().pipeline().fireChannelInactive();
                }
            }
        };
    }

    private void initChannelOption() {
        if (userEPoll) {
            client.channel(EpollSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    //保持连接
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        } else {
            client.channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    //保持连接
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }
    }

    private void init() {
        if (userEPoll) {
            createEPollClient();
        } else {
            createNIOClient();
        }
    }

    private void createEPollClient() {
        if (eventLoopGroup == null) {
            EpollEventLoopGroup epollEventLoopGroup = new EpollEventLoopGroup(getEventLoopGroupThreadNum(), getEventLoopGroupThreadFactory());
            epollEventLoopGroup.setIoRatio(100);
            eventLoopGroup = epollEventLoopGroup;
        }
    }

    private void createNIOClient() {
        if (eventLoopGroup == null) {
            NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(getEventLoopGroupThreadNum(), getEventLoopGroupThreadFactory());
            nioEventLoopGroup.setIoRatio(100);
            eventLoopGroup = nioEventLoopGroup;
        }
    }


    public void sendMessage(Object msg) {
        if (socketChannel != null) {
            // 如果管道关闭 重建连接
            if (!socketChannel.isActive()) {
                log.error("the socket channel is not active!");
            }
            socketChannel.writeAndFlush(msg);
        }
    }

    public int getEventLoopGroupThreadNum() {
        return eventLoopGroupThreadNum;
    }

    private ThreadFactory getEventLoopGroupThreadFactory() {
        return new DefaultThreadFactory("VULCAN_CLIENT_GROUP");
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }
}
