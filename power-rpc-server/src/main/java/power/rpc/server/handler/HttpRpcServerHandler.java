package power.rpc.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import power.rpc.RpcMessage;

import java.util.Map;

/**
 * @Author: PowerYang
 * @Date: 2019/7/4 18:56
 */
public class HttpRpcServerHandler extends RpcServerHandler {

    public HttpRpcServerHandler(Map<String, Object> serviceMap) {
        super(serviceMap);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {

    }
}
