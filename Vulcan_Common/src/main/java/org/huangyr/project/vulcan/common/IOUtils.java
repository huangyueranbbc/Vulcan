package org.huangyr.project.vulcan.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*******************************************************************************
 *
 * @date 2019-10-17 3:54 PM 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: IO工具类
 ******************************************************************************/

public class IOUtils {


    /**
     * Closes the stream ignoring {@link IOException}.
     * Must only be called in cleaning up from exception handlers.
     *
     * @param stream the Stream to close
     */
    public static void closeStream(java.io.Closeable stream) {
        cleanup(null, stream);
    }

    /**
     * Closes a URLConnection.
     *
     * @param conn the connection to close.
     * @since 2.4
     */
    public static void closeConnection(final URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }


    /**
     * Close the Closeable objects and <b>ignore</b> any {@link IOException} or
     * null pointers. Must only be used for cleanup in exception handlers.
     *
     * @param log        the log to record problems to at debug level. Can be null.
     * @param closeables the objects to close
     */
    public static void cleanup(Log log, java.io.Closeable... closeables) {
        for (java.io.Closeable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException e) {
                    if (log != null && log.isDebugEnabled()) {
                        log.debug("Exception in closing " + c, e);
                    }
                }
            }
        }
    }

    /**
     * 读取 InputStream 到 String字符串中
     */
    public static String readStream(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        String content = baos.toString();
        in.close();
        baos.close();
        return content;
    }

}
