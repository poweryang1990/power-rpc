package power.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import power.rpc.RpcMessage;
import power.rpc.serialization.Serializer;


/**
 * @Author: PowerYang
 * @Date: 2019/7/9 19:47
 */
@Data
@AllArgsConstructor
@Slf4j
public class RpcMessageEncoder  extends MessageToByteEncoder<RpcMessage> {

    /**
     * 序列化方法
     */
    Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) throws Exception {
        byte[] messageBody=serializer.serialize(rpcMessage);
        byteBuf.writeBoolean(rpcMessage.isRequest())
                .writeLong(rpcMessage.getInvokeId())
                .writeInt(messageBody.length)
                .writeBytes(messageBody);
        log.info("write messge end");
    }
}
