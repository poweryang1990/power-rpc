package power.rpc.server.handler;

import io.netty.channel.SimpleChannelInboundHandler;
import power.rpc.RpcMessage;

import java.util.Map;

/**
 * @Author: PowerYang
 * @Date: 2019/7/9 18:53
 */
public abstract class RpcServerHandler extends SimpleChannelInboundHandler<RpcMessage> {

    Map<String,Object> serviceMap;
    public RpcServerHandler(Map<String,Object> serviceMap){
        this.serviceMap=serviceMap;
    }
}
