package power.rpc.exception;

/**
 * @Author: PowerYang
 * @Date: 2019/7/1 20:25
 */
public class RpcResponseException extends  RuntimeException {
    public RpcResponseException(String message) {
        super(message);
    }
    public RpcResponseException(String message, Throwable cause) {
        super(message, cause);
    }

}
