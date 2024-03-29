package org.huangyr.project.vulcan.server.net.http.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.huangyr.project.vulcan.server.net.config.NettyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaderNames.*;

/*******************************************************************************
 *
 * @date 2019-12-16 10:55 AM
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: 请求处理
 ******************************************************************************/
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 客户端与服务端创建连接的时候调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("channel active...");
        NettyConfig.group.add(ctx.channel());
    }

    /**
     * 客户端与服务端断开连接时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("channel close...");
        NettyConfig.group.remove(ctx.channel());
    }

    /**
     * 服务端接收客户端发送过来的数据结束之后调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        log.info("channel read complete.");
        ctx.flush();
    }

    /**
     * 工程出现异常的时候调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("http server handler channel has error.", cause);
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        String responseContent = "ok";

        try {
            if (msg instanceof FullHttpRequest) {
                //参数解析部分
                FullHttpRequest request = (FullHttpRequest) msg;

                log.info("msg:{}", request);
                writeResponse(request, ctx.channel(), responseContent);
            } else {
                log.info("http server handler exception");
                //异常请求。直接返回
                writeErrorResponse(ctx.channel());
            }
        } catch (Exception e) {
            log.error("pipeline close exception!", e);
        }

    }

    private void writeResponse(FullHttpRequest request, Channel channel, String responseContent) {
        ByteBuf buf = copiedBuffer(responseContent, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        doResponseOptions(response);
        response.headers().set(CONTENT_TYPE, "text/json;charset=utf-8");
        response.headers().set(CONTENT_LENGTH, buf.readableBytes());
        ChannelFuture lastContentFuture = channel.writeAndFlush(response);
        if (!HttpUtil.isKeepAlive(request)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void doResponseOptions(FullHttpResponse response) {
        response.headers().set(CONTENT_TYPE, "text/json;charset=utf-8");
        response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type,Accept");
    }

    private void writeErrorResponse(Channel channel) {
        //其他请求直接返回
        channel.writeAndFlush("{error:404}").addListener(ChannelFutureListener.CLOSE);
    }

}