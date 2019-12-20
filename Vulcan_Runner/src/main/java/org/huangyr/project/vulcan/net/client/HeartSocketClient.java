package org.huangyr.project.vulcan.net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-16 10:55 AM
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: Socket长连接客户端
 ******************************************************************************/
public class HeartSocketClient extends Thread {

    private static Logger log = LoggerFactory.getLogger(HeartSocketClient.class);

    private int port;
    private String host;
    private SocketChannel socketChannel;
    private boolean userEPoll = true;
    private Bootstrap client;
    private EventLoopGroup eventLoopGroup;
    private int eventLoopGroupThreadNum = 0;

    public HeartSocketClient(int port, String host, int eventLoopGroupThreadNum, boolean userEPoll) {
        this.port = port;
        this.host = host;
        this.eventLoopGroupThreadNum = eventLoopGroupThreadNum;
        this.userEPoll = userEPoll;
    }

    @Override
    public void run() {
        init();
        client = new Bootstrap();
        try {
            // 绑定处理group
            client.group(eventLoopGroup).remoteAddress(host, port)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            // 初始化Runner处理器 处理Server下发的命令
                            socketChannel.pipeline().addLast(new ServerSocketCommandHandler());
                        }
                    });
            initChannelOption();
            // 进行连接
            ChannelFuture future;

            future = client.connect(host, port).sync();
            // 判断是否连接成功
            if (future.isSuccess()) {
                // 得到管道，便于通信
                socketChannel = (SocketChannel) future.channel();
                log.info("netty client start success.");
            } else {
                log.error("netty client start fail.");
            }
            // 等待客户端链路关闭，就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("HeartSocketClient start fail!", e);
        } finally {
            //优雅地退出，释放相关资源
            eventLoopGroup.shutdownGracefully();
        }
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


    public boolean sendMessage(Object msg) {
        if (socketChannel != null) {
            ChannelFuture channelFuture = socketChannel.writeAndFlush(msg);
            return channelFuture.isSuccess();
        }
        return false;
    }

    public int getEventLoopGroupThreadNum() {
        return eventLoopGroupThreadNum;
    }

    private ThreadFactory getEventLoopGroupThreadFactory() {
        return new DefaultThreadFactory("VULCAN_CLIENT_GROUP");
    }

}
