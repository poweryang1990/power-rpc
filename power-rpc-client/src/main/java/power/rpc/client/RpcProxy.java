package power.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import power.rpc.RpcMessage;
import power.rpc.client.configs.RpcClientConfig;
import power.rpc.client.handler.RpcClientHandler;
import power.rpc.codec.RpcMessageDecoder;
import power.rpc.codec.RpcMessageEncoder;
import power.rpc.entity.RpcRequest;
import power.rpc.entity.RpcResponse;
import power.rpc.serialization.SerializeType;
import power.rpc.serialization.Serializer;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: PowerYang
 * @Date: 2019/7/10 19:29
 */
@Slf4j
public class RpcProxy {

    /**
     * 存放请求编号与响应对象的映射关系
     */
    private static ConcurrentMap<Long, RpcMessage> rpcMessageMap=new ConcurrentHashMap<>();

    public static <T> T create(final Class<?> interfaceClass,RpcClientConfig clientConfig){
        //创建动态代理对象
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
                    RpcMessage rpcMessage=new RpcMessage();
                    rpcMessage.setRequest(true);
                    //创建RPC请求对象
                    RpcRequest rpcRequest=new RpcRequest();
                    rpcRequest.setInterfaceClassName(method.getDeclaringClass().getName());
                    rpcRequest.setMethodName(method.getName());
                    rpcRequest.setParameterTypes(method.getParameterTypes());
                    rpcRequest.setParameters(args);
                    rpcRequest.setId(rpcMessage.getInvokeId());
                    //TODO 服务发现

                    //TODO 负载均衡



                    //发送RPC请求
                    RpcResponse rpcResponse=send(rpcMessage,clientConfig.getHost(),clientConfig.getPort(),clientConfig.getSerializeType());
                    //获取响应结果
                    if(rpcResponse==null){
                        log.error("send request failure",new IllegalStateException("response is null"));
                        return null;
                    }
                    if(rpcResponse.getException()!=null){
                        log.error("response has exception",rpcResponse.getException());
                        return null;
                    }

                    return rpcResponse.getResult();
                }
        );
    }


    private static RpcResponse send(RpcMessage rpcMessage, String host, int port,SerializeType serializeType){
        log.info("send begin: "+host+":"+port);
        EventLoopGroup group=new NioEventLoopGroup(1);
        try {
            //创建RPC连接
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline=channel.pipeline();
                    Serializer serializer=serializeType.getSerializer();
                    pipeline.addLast(new RpcMessageDecoder(serializer))
                            .addFirst(new RpcMessageEncoder(serializer))
                            .addLast(new RpcClientHandler(rpcMessageMap));
                }
            });
            ChannelFuture future=bootstrap.connect(host,port).sync();
            Channel channel=future.channel();
            log.info("invokeId: "+rpcMessage.getInvokeId());
            //写入RPC请求对象
            channel.writeAndFlush(rpcMessage).sync();
            channel.closeFuture().sync();
            log.info("send end");
            //获取RPC响应对象
            return (RpcResponse) rpcMessageMap.get(rpcMessage.getInvokeId()).getMessageBody();
        }catch (Exception e){
            log.error("client exception",e);
            return null;
        }finally {
            group.shutdownGracefully();
            //移除请求编号和响应对象直接的映射关系
            rpcMessageMap.remove(rpcMessage.getInvokeId());
        }

    }

}
