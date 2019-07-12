package power.rpc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import power.rpc.entity.MessageBody;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义 RPC消息传输实体
 * @Author: PowerYang
 * @Date: 2019/7/9 19:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcMessage {

    /**
     * 用原子递增的方式来获取不重复invokeId
     */
    private static final AtomicLong invokeIdGnerator=new AtomicLong(0L);
    /**
     * 每一个请求的唯一识别id（由于采用异步通讯的方式，用来把请求request和返回的response对应上）
     */
    private long invokeId=invokeIdGnerator.getAndIncrement();

    /**
     * 区分请求和响应
     */
    private  boolean isRequest;
    /**
     * 消息体长度
     */
    private int messageBodyLength;
    /**
     * 消息体 可以是 方法本身 请求和相应内容
     */
    private MessageBody messageBody;

}
