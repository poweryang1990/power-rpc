package power.rpc.server;

import power.rpc.server.configs.RpcServerConfig;

import java.util.Map;

/**
 * RPC 服务工厂
 * @Author: PowerYang
 * @Date: 2019/7/9 16:12
 */
public class RpcServerFactory {

    /**
     * 创建服务端
     * @param serverConfig 服务配置
     * @param serviceMap 需要注册的服务集合
     * @return
     */
    public  static RpcServer   createRpcServer(RpcServerConfig serverConfig,Map<String,Object> serviceMap)
    {
        switch (serverConfig.getProtocol()){
            case TCP:
            default:
                return  new TcpRpcServer(serverConfig,serviceMap);
            case HTTP:
                return new HttpRpcServer(serverConfig,serviceMap);
        }
    }
}
