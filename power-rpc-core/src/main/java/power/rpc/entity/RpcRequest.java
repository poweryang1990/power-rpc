package power.rpc.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: PowerYang
 * @Date: 2019/7/1 20:22
 */
@Data
public class RpcRequest implements MessageBody{
    private long id;
    private String interfaceClassName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
}
