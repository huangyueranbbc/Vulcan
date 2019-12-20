package org.huangyr.project.vulcan.net.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheObjectAggregator;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.huangyr.project.vulcan.net.tcp.handler.HeartSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ThreadFactory;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-16 10:55 AM
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: netty服务 socket通信服务
 ******************************************************************************/
public class HeartServer extends Thread {
    private static Logger log = LoggerFactory.getLogger(HeartServer.class);
    private int port;
    private int bossGroupThreadNum = 0;
    private int workGroupThreadNum = 0;
    private boolean userEPoll = true;
    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    private ServerBootstrap server = null;

    public HeartServer(int port, int bossGroupThreadNum, int workGroupThreadNum, boolean userEPoll) {
        this.port = port;
        this.bossGroupThreadNum = bossGroupThreadNum;
        this.workGroupThreadNum = workGroupThreadNum;
        this.userEPoll = userEPoll;
    }

    private void init() {
        if (userEPoll) {
            createEPollServer();
        } else {
            createNIOServer();
        }
    }

    @Override
    public void run() {
        init();
        //server启动
        try {
            server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new BinaryMemcacheObjectAggregator(1024 * 1024));
                            pipeline.addLast(new HeartSocketHandler());
                        }
                    });
            initChannelOption();
            ChannelFuture future = server.bind(port).sync();
            if (future.isSuccess()) {
                log.info("heart socket server start success，address is localhost:{}", port);
            } else {
                log.error("heart socket server start fail.");
            }
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("heart socket server start error.");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void initChannelOption() {
        if (userEPoll) {
            server.channel(EpollServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 511)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //保持连接数
                    .option(ChannelOption.SO_BACKLOG, 300)
                    //保持连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        } else {
            server.channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 511)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //保持连接数
                    .option(ChannelOption.SO_BACKLOG, 300)
                    //保持连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }
    }

    private void createEPollServer() {
        if (bossGroup == null) {
            EpollEventLoopGroup epollEventLoopGroup = new EpollEventLoopGroup(getBossGoupThreadNum(), getBossGroupThreadFactory());
            epollEventLoopGroup.setIoRatio(100);
            bossGroup = epollEventLoopGroup;
        }
        if (workerGroup == null) {
            EpollEventLoopGroup epollEventLoopGroup = new EpollEventLoopGroup(getWorkGroupThreadNum(), getWorkGroupThreadFactory());
            epollEventLoopGroup.setIoRatio(80);
            workerGroup = epollEventLoopGroup;
        }
    }

    private void createNIOServer() {
        if (bossGroup == null) {
            NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(getBossGoupThreadNum(), getBossGroupThreadFactory());
            nioEventLoopGroup.setIoRatio(100);
            bossGroup = nioEventLoopGroup;
        }
        if (workerGroup == null) {
            NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(getWorkGroupThreadNum(), getWorkGroupThreadFactory(), getSelectorProvider());
            nioEventLoopGroup.setIoRatio(80);
            workerGroup = nioEventLoopGroup;
        }
    }

    private SelectorProvider getSelectorProvider() {
        return SelectorProvider.provider();
    }

    private int getBossGoupThreadNum() {
        //做成可以配置的
        return bossGroupThreadNum;
    }

    private int getWorkGroupThreadNum() {
        //做成可以配置的
        return workGroupThreadNum;
    }

    private ThreadFactory getBossGroupThreadFactory() {
        return new DefaultThreadFactory("VULCAN_HEART_SERVER_GROUP");
    }

    private ThreadFactory getWorkGroupThreadFactory() {
        return new DefaultThreadFactory("VULCAN_HEART_SERVER_GROUP");
    }
}
