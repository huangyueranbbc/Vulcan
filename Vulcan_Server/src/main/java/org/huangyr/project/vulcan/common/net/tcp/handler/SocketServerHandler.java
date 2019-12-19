package org.huangyr.project.vulcan.common.net.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.huangyr.project.vulcan.common.VulcanUtils;
import org.huangyr.project.vulcan.common.net.config.NettyConfig;
import org.huangyr.project.vulcan.proto.Command;
import org.huangyr.project.vulcan.proto.HeartResultPackage;
import org.huangyr.project.vulcan.proto.VulcanHeartPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-16 10:55 AM
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: 请求处理
 ******************************************************************************/
public class SocketServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(SocketServerHandler.class);


    /**
     * 客户端与服务端创建连接的时候调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("客户端与服务端连接开始...");
        NettyConfig.group.add(ctx.channel());
    }

    /**
     * 客户端与服务端断开连接时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("客户端与服务端连接关闭...");
        NettyConfig.group.remove(ctx.channel());
    }

    /**
     * 服务端接收客户端发送过来的数据结束之后调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        log.debug("信息接收完毕...");
    }

    /**
     * 工程出现异常的时候调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 服务端处理客户端websocket请求的核心方法，这里接收了客户端发来的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object info) {
        try {
            log.debug("get heart package data:{}", info);

            ByteBuf buf = (ByteBuf) info;
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            VulcanHeartPackage vulcanHeartPackage = VulcanHeartPackage.parseFrom(req);
            log.info("get heart package data:{}", vulcanHeartPackage);

            // TODO 处理心跳信息
            HeartResultPackage.Builder heartResultBuilder = HeartResultPackage.newBuilder();
            heartResultBuilder.setResultCode(200);
            heartResultBuilder.setMessage(VulcanUtils.getHostname() + "我收到你的心跳啦!");
            heartResultBuilder.setCommand(Command.NORMAL);
            heartResultBuilder.setSendHeartTime(vulcanHeartPackage.getHeartTime());
            heartResultBuilder.setReceiveHeartTime(VulcanUtils.now());
            heartResultBuilder.setResponseHeartTime(VulcanUtils.now());

            ByteBuf resp = Unpooled.copiedBuffer(heartResultBuilder.build().toByteArray());

            //返回给客户端
            ChannelFuture channelFuture = ctx.channel().writeAndFlush(resp);
            channelFuture.addListener(ChannelFutureListener.CLOSE);


            //服务端使用这个就能向 每个连接上来的客户端群发消息
            //NettyConfig.group.writeAndFlush(info);
//        Iterator<Channel> iterator = NettyConfig.group.iterator();
//        while(iterator.hasNext()){
//            //打印出所有客户端的远程地址
//            log.debug((iterator.next()).remoteAddress());
//        }
            log.info("handle the node:{} heart success.", vulcanHeartPackage.getIp());
        } catch (Exception e) {
            log.error("netty channel read has error. msg:{}", info, e);
        }
    }

}