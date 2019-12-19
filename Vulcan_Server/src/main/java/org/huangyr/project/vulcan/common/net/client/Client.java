package org.huangyr.project.vulcan.common.net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-16 10:55 AM
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: Socket长连接客户端
 ******************************************************************************/
public class Client extends Thread {

    private static Logger log = LoggerFactory.getLogger(Client.class);

    private int port;
    private String host;
    private SocketChannel socketChannel;

    public Client(int port, String host) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                // 保持连接
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 有数据立即发送
                .option(ChannelOption.TCP_NODELAY, true)
                // 绑定处理group
                .group(eventLoopGroup).remoteAddress(host, port)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 初始化编码器，解码器，处理器
                        socketChannel.pipeline().addLast(
                                new ClientHandler());
                    }
                });
        // 进行连接
        ChannelFuture future;
        try {
            future = bootstrap.connect(host, port).sync();
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
            log.error("Client start fail!", e);
        } finally {
            //优雅地退出，释放相关资源
            eventLoopGroup.shutdownGracefully();
        }
    }

    public void sendMessage(Object msg) {
        if (socketChannel != null) {
            socketChannel.writeAndFlush(msg);
        }
    }
}
