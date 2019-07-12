package power.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import power.rpc.RpcMessage;
import power.rpc.entity.MessageBody;
import power.rpc.entity.RpcRequest;
import power.rpc.entity.RpcResponse;
import power.rpc.exception.RpcResponseException;
import power.rpc.serialization.Serializer;

import java.util.List;

/**
 * @Author: PowerYang
 * @Date: 2019/7/9 19:47
 */
@Data
@AllArgsConstructor
@Slf4j
public class RpcMessageDecoder extends ByteToMessageDecoder {

    /**
     * 序列化方法
     */
    Serializer serializer;

    private static final int MAX_BODY_SIZE = 1024 * 1024 * 5;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //获取消息内容长度
        boolean isRequest=byteBuf.readBoolean();
        long invokeId=byteBuf.readLong();
        int bodyLength = byteBuf.readInt();
        checkBodyLength(bodyLength);

        byte[] bytes=new byte[bodyLength];
        byteBuf.readBytes(bytes);

        Class messageBodyClass=isRequest? RpcRequest.class:RpcResponse.class;
        MessageBody messageBody=(MessageBody)serializer.deserialize(bytes,messageBodyClass);
        RpcMessage rpcMessage=new RpcMessage();
        rpcMessage.setRequest(isRequest);
        rpcMessage.setInvokeId(invokeId);
        rpcMessage.setMessageBodyLength(bodyLength);
        rpcMessage.setMessageBody(messageBody);
        list.add(rpcMessage);

    }

    private void checkBodyLength(int bodyLength)   {
        if (bodyLength > MAX_BODY_SIZE) {
            throw new RpcResponseException("body of request is bigger than limit value "+ MAX_BODY_SIZE);
        }
    }
}
