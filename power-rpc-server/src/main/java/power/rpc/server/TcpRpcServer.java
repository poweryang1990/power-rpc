package power.rpc.server;


import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import power.rpc.codec.RpcMessageDecoder;
import power.rpc.codec.RpcMessageEncoder;
import power.rpc.serialization.Serializer;
import power.rpc.server.configs.RpcServerConfig;
import power.rpc.server.handler.TcpRpcServerHandler;

import java.util.Map;

/**
 * 基于TCP的RPC服务端
 * @Author: PowerYang
 * @Date: 2019/7/3 11:07
 */
@Slf4j
public class TcpRpcServer extends  RpcServer {


    public TcpRpcServer(RpcServerConfig rpcServerConfig,Map<String,Object> serviceMap) {
        super(rpcServerConfig,serviceMap);
    }


    @Override
    public void start() {
        try {
            //启动RPC服务
            serverBootstrap.group(bossGroup,workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .handler(new LoggingHandler(LogLevel.INFO));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline=channel.pipeline();
                    //获取序列化方式
                    Serializer serializer=rpcServerConfig.getSerializeType().getSerializer();
                    //编码RPC请求
                    pipeline.addLast(new RpcMessageEncoder(serializer));
                    //解码RPC请求
                    pipeline.addLast(new RpcMessageDecoder(serializer));
                    //处理RPC请求
                    pipeline.addLast(new TcpRpcServerHandler(serviceMap));
                }
            });

            //同步启动，RPC服务器启动完毕后才执行后续代码
            ChannelFuture future=serverBootstrap.bind(rpcServerConfig.getPort()).sync();
            log.info("server started,listening on {}",rpcServerConfig.getPort());

            //启动后注册服务


            //释放资源
            future.channel().closeFuture().sync();
        }catch (Exception e){
            log.error("server exception",e);
        }finally {
            //关闭RPC服务
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
