package org.huangyr.project.vulcan.status;

import lombok.Getter;

/*******************************************************************************
 *
 * @date 2019-06-03 15:19 
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: 通信请求协议
 ******************************************************************************/
@Getter
public enum ProtocolCode {

    /**
     * 请求成功
     **/
    SUCCESS(200, "success."),
    /**
     * 请求失败
     **/
    FAILED(400, "failed"),
    /**
     * 4XXXX参数校验失败
     **/
    PROTOCOL_CODE_40001(40001, "parametric verification failed."),
    /**
     * 5XXXX server出现异常
     **/
    /**
     * server处理消息失败
     */
    PROTOCOL_CODE_50001(50001, "server service is error."),
    /**
     * 心跳包超时拒绝
     */
    PROTOCOL_CODE_50005(50001, "the heart package is timeout. reject request.");
    /**
     * 6XXXX runner出现异常
     **/


    /**
     * 协议码
     */
    private int code;
    /**
     * 协议信息
     */
    private String message;

    ProtocolCode(int resultCode, String msg) {
        this.code = resultCode;
        this.message = msg;
    }

    public static ProtocolCode getByValue(Integer code) {
        for (ProtocolCode table : values()) {
            if (table.getCode() == code) {
                return table;
            }
        }
        return FAILED;
    }
}
