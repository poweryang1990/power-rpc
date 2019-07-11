package power.rpc.entity;

import lombok.Data;

/**
 * @Author: PowerYang
 * @Date: 2019/7/1 20:23
 */
@Data
public class RpcResponse implements MessageBody{
    private long requestId;
    private int code;
    /**
     * RPC返回结果
     */
    private Object result;
    private String errorMsg;
    private Exception exception;
}
