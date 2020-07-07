import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.huangyr.project.vulcan.common.ConfigUtils;
import org.huangyr.project.vulcan.common.DateUtils;
import org.huangyr.project.vulcan.common.VulcanUtils;
import org.huangyr.project.vulcan.proto.VulcanHeartPackage;
import org.huangyr.project.vulcan.runner.net.client.HeartSocketClient;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import static org.huangyr.project.vulcan.runner.common.Constants.*;


/*******************************************************************************
 *
 * @date 2019-12-18 10:03 AM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description:
 ******************************************************************************/
@Slf4j
public class T1 {

    /*
     * 日志初始化
     */
    static {
        ConfigUtils.initLog4j2Config();
    }

    public static void main(String[] args) {

        // RunnerSocket服务客户端 接收并处理Server下发的指令
        HeartSocketClient bootstrap = new HeartSocketClient(SERVER_IP, SERVER_PORT, 1, BASE_SLEEP_TIME, MAX_RETRIES, MAX_SLEEP_TIME, false);
        bootstrap.start();

        while (true) {
            try {
                VulcanHeartPackage.Builder heartBuilder = VulcanHeartPackage.newBuilder();
                heartBuilder.setIp(VulcanUtils.getHostname());
                heartBuilder.setHeartTime(DateUtils.now());
                heartBuilder.setMessage("我发送心跳啦!");
                VulcanHeartPackage heartPackage = heartBuilder.build();

                ByteBuf resp = Unpooled.copiedBuffer(heartPackage.toByteArray());
                bootstrap.sendMessage(resp);
                Thread.sleep(10);

                // test1();

            } catch (Exception e) {
                log.error("has error .", e);
            }


        }

//    private static void test1() {
//        VulcanHeartPackage.Builder heartBuilder = VulcanHeartPackage.newBuilder();
//        heartBuilder.setIp(org.huangyr.project.common.VulcanUtils.getHostname());
//        heartBuilder.setHeartTime(org.huangyr.project.common.VulcanUtils.now());
//        heartBuilder.setMessage("我发送心跳啦!");
//        VulcanHeartPackage heartPackage = heartBuilder.build();
//
//
//        Socket socket = null;
//        InputStream input = null;
//        OutputStream dos = null;
//        ByteArrayOutputStream byteArrayOutputStream = null;
//
//        try {
//            socket = new Socket("127.0.0.1", 8888);
//            if (!socket.getKeepAlive()) {
//                socket.setKeepAlive(true);
//            }
//
//
//            while (true){
//                input = socket.getInputStream();
//                // 2.获取客户端输出流
//                dos = socket.getOutputStream();
//                // 3.向服务端发送消息
//                dos.write(heartPackage.toByteArray());
//                dos.flush();
//                log.debug("成功向服务器发送消息");
//                // 4.获取输入流，并读取服务器端的响应信息
//                byteArrayOutputStream = new ByteArrayOutputStream();
//
//                byte[] buffer = new byte[1024 * 64];
//                int n = 0;
//                while (-1 != (n = input.read(buffer))) {
//                    byteArrayOutputStream.write(buffer, 0, n);
//                }
//
//                ServerCommandPackage heartResultPackage = ServerCommandPackage.parseFrom(byteArrayOutputStream.toByteArray());
//                System.out.println("send heart success. get heart response package:" + heartResultPackage.toBuilder());
//
//                Thread.sleep(5000);
//            }
//
//        } catch (Exception e) {
//            log.error("send tcp message error. ip:{} port:{} message:{}", "127.0.0.1", 8888, "aaa", e);
//        } finally {
////            // 4.释放资源
////            org.huangyr.project.common.IOUtils.closeStream(byteArrayOutputStream);
////            org.huangyr.project.common.IOUtils.closeStream(input);
////            org.huangyr.project.common.IOUtils.closeStream(dos);
////            org.huangyr.project.common.IOUtils.closeStream(socket);
//        }
//    }
    }

}
