package org.huangyr.project.vulcan.server.net.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.huangyr.project.vulcan.server.net.http.handler.HttpServerHandler;
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
 * @Description: netty服务 http通信服务 阻塞式，所以启动需要另开线程
 ******************************************************************************/
public class HttpServer extends Thread {
    private static Logger log = LoggerFactory.getLogger(HttpServer.class);
    private int port;
    private int bossGroupThreadNum = 1;
    private int workGroupThreadNum = 0;
    private boolean userEPoll = true;
    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    ServerBootstrap server = null;

    public HttpServer(int port, int bossGroupThreadNum, int workGroupThreadNum, boolean userEPoll) {
        this.port = port;
        this.bossGroupThreadNum = bossGroupThreadNum;
        this.workGroupThreadNum = workGroupThreadNum;
        this.userEPoll = userEPoll;
    }

    public HttpServer(int port) {
        this.port = port;
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
                            // request解密
                            pipeline.addLast(new HttpRequestDecoder());
                            //将多个http对象聚合成单一的FullHttpRequest
                            pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
                            // response 加密
                            pipeline.addLast(new HttpResponseEncoder());
                            //数据压缩
                            pipeline.addLast(new HttpContentCompressor());
                            // 调用我们的方法
                            pipeline.addLast(new HttpServerHandler());
                        }
                    });
            initChannelOption();
            ChannelFuture future = server.bind(port).sync();
            if (future.isSuccess()) {
                log.info("http server start success，address is http://localhost:{}", port);
            } else {
                log.error("http server start fail.");
            }
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("http server start error!", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void initChannelOption() {
        if (userEPoll) {
            server.channel(EpollServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, false)
                    //阻塞 最大排队长度
                    .option(ChannelOption.SO_BACKLOG, 511)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        } else {
            server.channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, false)
                    //阻塞 最大排队长度
                    .option(ChannelOption.SO_BACKLOG, 511)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
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
        return new DefaultThreadFactory("VULCAN_HTTP_GROUP");
    }

    private ThreadFactory getWorkGroupThreadFactory() {
        return new DefaultThreadFactory("VULCAN_HTTP_GROUP");
    }
}
