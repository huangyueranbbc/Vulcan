package org.huangyr.project.vulcan.core;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-12-16 10:55 AM
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
 * @Description: TCP消息工具类
 ******************************************************************************/
public class MessageUtils {

    private static Logger log = LoggerFactory.getLogger(MessageUtils.class);

    /**
     * 发送socket消息
     * @param ip
     * @param port
     * @param message
     * @param charsetName
     * @param isKeepAlive
     * @return
     */
    protected static String sendSocketMessage(String ip, int port, String message, String charsetName, boolean isKeepAlive) {
        Socket socket = null;
        InputStream is = null;
        InputStreamReader isr = null;
        OutputStream dos = null;
        BufferedReader br = null;
        // default:utf-8
        charsetName = StringUtils.isEmpty(charsetName) ? "utf-8" : charsetName;
        try {
            socket = new Socket("127.0.0.1", 8888);
            if (!socket.getKeepAlive()) {
                socket.setKeepAlive(isKeepAlive);
            }
            is = socket.getInputStream();
            isr = new InputStreamReader(is, charsetName);
            // 2.获取客户端输出流
            dos = socket.getOutputStream();
            log.info("Connected to {}:{}", ip, port);
            // 3.向服务端发送消息
            dos.write("你好啊".getBytes());
            dos.flush();
            log.info("成功向服务器发送消息");
            // 4.获取输入流，并读取服务器端的响应信息
            br = new BufferedReader(isr);
            String returnInfo = br.readLine();
            log.info("服务器端返回数据为：" + returnInfo);
            return returnInfo;
        } catch (Exception e) {
            log.error("send tcp message error. ip:{} port:{} message:{} charsetName:{}", ip, port, message, charsetName, e);
            return null;
        } finally {
            // 4.释放资源
            IOUtils.closeStream(br);
            IOUtils.closeStream(isr);
            IOUtils.closeStream(is);
            IOUtils.closeStream(dos);
            IOUtils.closeStream(socket);
        }

    }

    /**
     * 发送长连接消息
     *
     * @param ip
     * @param port
     * @param message
     * @param charsetName
     * @return
     */
    public static String sendSocketKeepAliveMessage(String ip, int port, String message, String charsetName) {
        return sendSocketMessage(ip, port, message, charsetName, true);
    }

    /**
     * 发送短连接消息
     *
     * @param ip
     * @param port
     * @param message
     * @param charsetName
     * @return
     */
    public static String sendSocketNoKeepAliveMessage(String ip, int port, String message, String charsetName) {
        return sendSocketMessage(ip, port, message, charsetName, false);
    }

}