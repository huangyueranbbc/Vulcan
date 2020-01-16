package org.huangyr.project.vulcan.common;

/*******************************************************************************
 *
 * @date 2019-06-03 15:19 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: 通信请求协议
 ******************************************************************************/
public enum ProtocolCode {

    /**
     * 2XX
     **/
    SUCCESS(200, "success."),

    /**
     * 4XXXX
     **/
    PROTOCOLCODE_40001(40001, "parametric verification failed."), // 参数校验失败
    PROTOCOLCODE_40002(40002, "table name or table columns parse error."), // 表名类型解析异常

    /**
     * 5XXXX
     **/
    PROTOCOLCODE_50001(50001, "server service is error."); // 服务出现异常


    private int code; // 协议码
    private String message; // 协议信息

    ProtocolCode(int resultCode, String msg) {
        this.code = resultCode;
        this.message = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public static ProtocolCode getByValue(Integer code) {
        for (ProtocolCode table : values()) {
            if (table.getCode() == code) {
                return table;
            }
        }
        return null;
    }
}
