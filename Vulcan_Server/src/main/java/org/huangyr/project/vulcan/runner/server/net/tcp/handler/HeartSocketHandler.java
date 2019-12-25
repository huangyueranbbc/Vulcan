package org.huangyr.project.vulcan.runner.server.net.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.huangyr.project.vulcan.runner.server.common.DateUtils;
import org.huangyr.project.vulcan.runner.server.common.VulcanUtils;
import org.huangyr.project.vulcan.runner.server.net.config.NettyConfig;
import org.huangyr.project.vulcan.runner.server.proto.Command;
import org.huangyr.project.vulcan.runner.server.proto.ServerCommandPackage;
import org.huangyr.project.vulcan.runner.server.proto.VulcanHeartPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-16 10:55 AM
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: 心跳请求处理
 ******************************************************************************/
public class HeartSocketHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(HeartSocketHandler.class);

    /**
     * 心跳丢失次数
     */
    private int counter = 0;

    /**
     * 客户端与服务端创建连接的时候调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("channel active...");
        counter = 0;
        NettyConfig.group.add(ctx.channel());
        printCurrentActiveChannel();
    }

    /**
     * 客户端与服务端断开连接时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("channel close...");
        NettyConfig.group.remove(ctx.channel());
        printCurrentActiveChannel();
    }

    private void printCurrentActiveChannel() {
        Iterator<Channel> iterator = NettyConfig.group.iterator();
        while (iterator.hasNext()) {
            //打印出所有客户端的远程地址
            log.debug("get client:" + String.valueOf((iterator.next()).remoteAddress()));
        }
    }

    /**
     * 服务端接收客户端发送过来的数据结束之后调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        log.debug("channel read complete...");
        ctx.flush();
    }

    /**
     * 工程出现异常的时候调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("heart socket handler channel has error.", cause);
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 服务端处理客户端websocket请求的核心方法，这里接收了客户端发来的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            log.debug("get heart package data:{}", msg);

            ByteBuf buf = (ByteBuf) msg;
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            VulcanHeartPackage vulcanHeartPackage = VulcanHeartPackage.parseFrom(req);
            log.info("get heart package data:{}", vulcanHeartPackage);

            // TODO 处理心跳信息
            ServerCommandPackage.Builder heartResultBuilder = ServerCommandPackage.newBuilder();
            heartResultBuilder.setResultCode(200);
            heartResultBuilder.setMessage(VulcanUtils.getHostname() + "我收到你的心跳啦!");
            heartResultBuilder.setCommand(Command.NORMAL);
            heartResultBuilder.setResponseHeartTime(DateUtils.now());

            ByteBuf resp = Unpooled.copiedBuffer(heartResultBuilder.build().toByteArray());

            //返回给客户端
            ctx.channel().writeAndFlush(resp);

            // TODO 模拟发送关闭Runner指令
            // 服务端使用这个就能向 每个连接上来的客户端群发消息
            // ChannelGroupFuture channelFutures = NettyConfig.group.writeAndFlush(resp1);
            // log.info("send result:{}", channelFutures.isSuccess());

            // channelFuture.addListener(ChannelFutureListener.CLOSE); // 长连接 不监控该事件
            log.info("handle the node:{} heart success.", vulcanHeartPackage.getIp());
        } catch (Exception e) {
            log.error("netty channel read has error. msg:{}", msg, e);
        }
    }

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleStateEvent event = (IdleStateEvent) evt;
//            if (event.state().equals(IdleState.READER_IDLE)) {
//                // 空闲多少s之后触发 (心跳包丢失)
//                if (counter >= 3) {
//                    // 连续丢失3个心跳包 (断开连接)
//                    ctx.channel().close().sync();
//                    log.error("已与" + ctx.channel().remoteAddress() + "断开连接");
//                    System.out.println("已与" + ctx.channel().remoteAddress() + "断开连接");
//                } else {
//                    counter++;
//                    log.debug(ctx.channel().remoteAddress() + "丢失了第 " + counter + " 个心跳包");
//                    System.out.println("丢失了第 " + counter + " 个心跳包");
//                }
//            }
//
//        }
//    }

}