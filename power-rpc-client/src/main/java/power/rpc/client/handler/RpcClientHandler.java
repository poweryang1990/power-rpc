package power.rpc.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import power.rpc.RpcMessage;

import java.util.concurrent.ConcurrentMap;

/**
 * 存储响应结果
 * 因为是异步 所以这里将结果 通过请求ID作为Key 将数据存入Map中  然后就可以从这一个Map中取数据
 * @Author: PowerYang
 * @Date: 2019/7/10 19:42
 */
@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcMessage> {

    //响应结果存储
    private ConcurrentMap<Long,RpcMessage> rpcMessageMap;

    public  RpcClientHandler(ConcurrentMap<Long,RpcMessage> rpcMessageMap){
        this.rpcMessageMap=rpcMessageMap;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {
        log.info("read a Response,invokeId: "+rpcMessage.getInvokeId());
        rpcMessageMap.put(rpcMessage.getInvokeId(),rpcMessage);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client caught exception",cause);
        ctx.close();
    }
}
