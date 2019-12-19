package org.huangyr.project.vulcan.common;

import org.slf4j.Logger;

import java.io.IOException;

/*******************************************************************************
 * 版权信息：安信证券股份有限公司
 * Copyright: Copyright (c) 2019安信证券股份有限公司,Inc.All Rights Reserved.
 *
 * @date 2019-10-17 3:54 PM 
 * @author: <a href=mailto:@essence.com.cn>黄跃然</a>
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
     * Close the Closeable objects and <b>ignore</b> any {@link IOException} or
     * null pointers. Must only be used for cleanup in exception handlers.
     *
     * @param log the log to record problems to at debug level. Can be null.
     * @param closeables the objects to close
     */
    public static void cleanup(Logger log, java.io.Closeable... closeables) {
        for (java.io.Closeable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch(IOException e) {
                    if (log != null && log.isDebugEnabled()) {
                        log.debug("Exception in closing " + c, e);
                    }
                }
            }
        }
    }
}
