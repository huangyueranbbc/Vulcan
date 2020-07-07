package org.huangyr.project.vulcan.runner.net.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.huangyr.project.vulcan.proto.ServerCommandPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*******************************************************************************
 *
 * @date 2019-12-16 10:55 AM
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: Socket长连接处理器 处理Server下发的命令
 ******************************************************************************/
@Slf4j
public class ServerSocketCommandHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("runner to server channel connection finish.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TODO 远程调用 上报心跳 datanode和namenode唯一通信方式
        //                DatanodeCommand[] datanodeCommands = nameNode.sendHeartbeat(datanodeRegInfo);
        //
        //                // 处理namenode下达的指令 失败则等待下一次心跳通信
        //                if (!processCommand(datanodeCommands)) {
        //                    continue;
        //                }

        log.info("客户端收到消息：" + msg);
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        ServerCommandPackage heartResultPackage = ServerCommandPackage.parseFrom(bytes);
        log.info("send heart success. get heart response package:{}", heartResultPackage.toBuilder());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        log.debug("channel read complete.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("runner to socket channel is close...");
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("client handler channel has error.", cause);
        if (null != cause) cause.printStackTrace();
        if (null != ctx) ctx.close();
    }

}
