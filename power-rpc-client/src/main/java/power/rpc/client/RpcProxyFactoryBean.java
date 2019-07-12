package power.rpc.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import power.rpc.RpcMessage;
import power.rpc.client.configs.RpcClientConfig;
import power.rpc.client.handler.RpcClientHandler;
import power.rpc.codec.RpcMessageDecoder;
import power.rpc.codec.RpcMessageEncoder;
import power.rpc.entity.RpcRequest;
import power.rpc.entity.RpcResponse;
import power.rpc.exception.RpcResponseException;
import power.rpc.serialization.SerializeType;
import power.rpc.serialization.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 代理bean注入
 * @Author: PowerYang
 * @Date: 2019/7/3 16:52
 */
@Slf4j
@Data
public class RpcProxyFactoryBean<T> implements FactoryBean<T>, InitializingBean, ApplicationContextAware {

    @Nullable
    private Class<?> clazz;

    private ApplicationContext applicationContext;


    private T rpcProxy;
    /**
     * 存放请求编号与响应对象的映射关系
     */
    private ConcurrentMap<Long, RpcMessage> rpcMessageMap=new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(clazz,"rpc clazz must be not null");

        RpcClientConfig rpcClientConfig = this.applicationContext.getBean(RpcClientConfig.class);
        rpcProxy=this.create(clazz,rpcClientConfig);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
    @Nullable
    @Override
    public T getObject() throws Exception {
        return rpcProxy;
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    private  T create(final Class<?> interfaceClass,RpcClientConfig clientConfig){
        //创建动态代理对象
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // TODO  不知为何 默认在自动注入时 默认会 执行toString方法
                        if (method.toString().contains("toString")){
                            return null;
                        }
                        RpcMessage rpcMessage=new RpcMessage();
                        rpcMessage.setRequest(true);
                        //创建RPC请求对象
                        RpcRequest rpcRequest=new RpcRequest();
                        rpcRequest.setInterfaceClassName(method.getDeclaringClass().getName());
                        rpcRequest.setMethodName(method.getName());
                        rpcRequest.setParameterTypes(method.getParameterTypes());
                        rpcRequest.setParameters(args);
                        rpcRequest.setId(rpcMessage.getInvokeId());

                        rpcMessage.setMessageBody(rpcRequest);
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
                        //TODO 因为在JSON序列化的时候 返回的Result结果是JSONObject， 转成Java对象失败，JSONArray都可以后续跟进， 暂时采用这种临时方案代替
                        if (clientConfig.getSerializeType()==SerializeType.JSON){
                            if (rpcResponse.getResult() instanceof JSONObject){
                                return JSON.toJavaObject((JSONObject)rpcResponse.getResult(),method.getReturnType()) ;
                            }
                        }
                        return  rpcResponse.getResult();
                    }
                }
        );
    }


    private  RpcResponse send(RpcMessage rpcMessage, String host, int port,SerializeType serializeType){
        log.info("send begin: "+host+":"+port);
        EventLoopGroup group=new NioEventLoopGroup(1);
        try {
            //创建RPC连接
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline=channel.pipeline();
                    Serializer serializer=serializeType.getSerializer();
                    pipeline .addLast(new RpcMessageEncoder(serializer))
                            .addLast(new RpcMessageDecoder(serializer))
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
            throw  new RpcResponseException("远程请求异常",e);
        }finally {
            group.shutdownGracefully();
            //移除请求编号和响应对象直接的映射关系
            rpcMessageMap.remove(rpcMessage.getInvokeId());
        }

    }


}
