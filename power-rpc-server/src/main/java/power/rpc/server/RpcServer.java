package power.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import power.rpc.RpcProtocol;
import power.rpc.server.configs.RpcServerConfig;

import javax.annotation.PreDestroy;
import java.util.Map;


/**
 * @Author: PowerYang
 * @Date: 2019/7/3 10:14
 */
@Slf4j
public abstract class RpcServer {



    RpcServerConfig rpcServerConfig;
    Map<String,Object> serviceMap;
    /**
     * 创建bootstrap
     */
     ServerBootstrap serverBootstrap = new ServerBootstrap();
    /**
     * BOSS
     */
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    /**
     * Worker
     */
    EventLoopGroup workGroup = new NioEventLoopGroup();
    public  RpcServer(RpcServerConfig serverConfig,Map<String,Object> serviceMap){
        this.rpcServerConfig=serverConfig;
        this.serviceMap=serviceMap;
    }
    /**
     * 启动服务
     */
    public abstract void  start();

    /**
     * 关闭服务器方法
     */
    @PreDestroy
    public void close() {
        log.info("关闭服务器....");
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
