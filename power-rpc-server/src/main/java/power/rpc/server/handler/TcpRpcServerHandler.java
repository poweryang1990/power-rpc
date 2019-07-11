package power.rpc.server.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import power.rpc.RpcMessage;
import power.rpc.entity.RpcRequest;
import power.rpc.entity.RpcResponse;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: PowerYang
 * @Date: 2019/7/4 18:56
 */
@Slf4j
public class TcpRpcServerHandler extends RpcServerHandler {

    public TcpRpcServerHandler(Map<String, Object> serviceMap) {
        super(serviceMap);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {

        RpcRequest rpcRequest=(RpcRequest)rpcMessage.getMessageBody();
        RpcResponse rpcResponse=new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getId());
        try {
            Object result= handle(rpcRequest);
            rpcResponse.setResult(result);

        } catch (Exception e) {
            // 异常捕获
            rpcResponse.setErrorMsg(e.getMessage());
            rpcResponse.setException(e);
            log.error("handle result failure", e);
        }
        rpcMessage.setMessageBody(rpcResponse);
        //是响应请求
        rpcMessage.setRequest(false);
        channelHandlerContext.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server caught exception", cause);
        ctx.close();
    }

    private Object handle(RpcRequest request) throws Exception {
        log.info("request invoke handle");
        // 获取服务实例
        String serviceName = request.getInterfaceClassName();
        Object serviceBean = serviceMap.get(serviceName);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("can not find service bean by key: %s", serviceName));
        }
        // 获取反射调用所需的变量
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        log.info(methodName);
        Class<?>[] parameterTypes = request.getParameterTypes();
        log.info(parameterTypes[0].getName());
        Object[] parameters = request.getParameters();
        // 执行反射调用
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }
}
