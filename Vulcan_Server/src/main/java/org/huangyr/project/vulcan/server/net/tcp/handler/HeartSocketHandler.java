package org.huangyr.project.vulcan.server.net.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.huangyr.project.vulcan.common.CommonUtils;
import org.huangyr.project.vulcan.common.DateUtils;
import org.huangyr.project.vulcan.common.VulcanUtils;
import org.huangyr.project.vulcan.proto.Command;
import org.huangyr.project.vulcan.proto.RunnerNodeInfo;
import org.huangyr.project.vulcan.proto.ServerCommandPackage;
import org.huangyr.project.vulcan.proto.VulcanHeartPackage;
import org.huangyr.project.vulcan.runner.Runner;
import org.huangyr.project.vulcan.server.Server;
import org.huangyr.project.vulcan.server.common.Constants;
import org.huangyr.project.vulcan.server.net.config.NettyConfig;
import org.huangyr.project.vulcan.status.ProtocolCode;
import org.huangyr.project.vulcan.status.RunnerInfo;

import java.net.InetSocketAddress;
import java.util.Iterator;

/*******************************************************************************
 *
 * @date 2019-12-16 10:55 AM
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: 心跳请求处理
 ******************************************************************************/
@Slf4j
public class HeartSocketHandler extends ChannelInboundHandlerAdapter {

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
            long heartTime = DateUtils.now();
            log.debug("get heart package data:{}", msg);
            ByteBuf buf = (ByteBuf) msg;
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            VulcanHeartPackage vulcanHeartPackage = VulcanHeartPackage.parseFrom(req);
            log.info("get heart package data:{}", vulcanHeartPackage.toBuilder());

            // 获取发送端的ip和端口
            InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

            ByteBuf resp = handlerHeart(vulcanHeartPackage, heartTime, insocket);

            //返回给客户端
            ctx.channel().writeAndFlush(resp);
            // FIXME 模拟发送关闭Runner指令
            // 服务端使用这个就能向 每个连接上来的客户端群发消息
            // ChannelGroupFuture channelFutures = NettyConfig.group.writeAndFlush(resp1);
            // log.info("send result:{}", channelFutures.isSuccess());

            // channelFuture.addListener(ChannelFutureListener.CLOSE); // 长连接 不监控该事件
            log.info("handle the node:{} heart success.", vulcanHeartPackage.getIp());
        } catch (Exception e) {
            log.error("netty channel read has error. msg:{}", msg, e);
        }
    }

    /**
     * 处理心跳
     *
     * @param vulcanHeartPackage
     * @param heartTime
     * @param insocket
     */
    private ByteBuf handlerHeart(VulcanHeartPackage vulcanHeartPackage, long heartTime, InetSocketAddress insocket) {
        try {
            // runner ip and port.
            String ip = insocket.getAddress().getHostAddress();
            int port = insocket.getPort();

            String packageIp = vulcanHeartPackage.getIp();
            // package heart time.
            long packageHeartTime = vulcanHeartPackage.getHeartTime();
            String message = vulcanHeartPackage.getMessage();
            String nodename = vulcanHeartPackage.getNodename();
            RunnerNodeInfo runnerNodeInfo = vulcanHeartPackage.getRunnerNodeInfo();

            // 更新集群的runner节点信息
            RunnerInfo runnerInfo = Constants.runnerInfoMap.getOrDefault(nodename, new RunnerInfo());

            if (Constants.runnerInfoMap.containsKey(nodename)) {
                // 首次连接
                // TODO 给Runner分配租约Lease
            }

            // 更新runner信息
            if (heartTime >= packageHeartTime && runnerInfo.getLastHeartTime() < packageHeartTime) {
                // 包接收的时间在接收范围内
                runnerInfo.setLastHeartTime(heartTime);
            } else {
                log.warn("the runner heart package is timeout. ip:{} name:{}", ip, nodename);
                // 非正常心跳 过滤
                ServerCommandPackage.Builder errorPackage = ServerCommandPackage.newBuilder();
                errorPackage.setResultCode(ProtocolCode.PROTOCOL_CODE_50005.getCode());
                errorPackage.setMessage(ProtocolCode.PROTOCOL_CODE_50005.getMessage());
                return Unpooled.copiedBuffer(errorPackage.build().toByteArray());
            }
            runnerInfo.setName(nodename);
            runnerInfo.setIdentifying(CommonUtils.getIdentifying(nodename, ip, port));
            runnerInfo.setIp(ip);
            runnerInfo.setPort(port);
            Constants.runnerInfoMap.putIfAbsent(nodename, runnerInfo);
            log.info("current connecting runner:{}", Constants.runnerInfoMap);

            // TODO 更新租约

            // TODO 处理心跳信息
            ServerCommandPackage.Builder heartResultBuilder = ServerCommandPackage.newBuilder();
            heartResultBuilder.setResultCode(ProtocolCode.SUCCESS.getCode());
            heartResultBuilder.setMessage(VulcanUtils.getHostname() + "我收到你的心跳啦!");
            heartResultBuilder.setCommand(Command.NORMAL);
            heartResultBuilder.setResponseHeartTime(DateUtils.now());
            return Unpooled.copiedBuffer(heartResultBuilder.build().toByteArray());
        } catch (Exception e) {
            log.error("handler heart errot.", e);
            ServerCommandPackage.Builder errorPackage = ServerCommandPackage.newBuilder();
            errorPackage.setResultCode(ProtocolCode.PROTOCOL_CODE_50001.getCode());
            errorPackage.setMessage("server can not process the message.");
            return Unpooled.copiedBuffer(errorPackage.build().toByteArray());
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