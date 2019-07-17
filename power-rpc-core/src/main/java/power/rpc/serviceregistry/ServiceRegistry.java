package power.rpc.serviceregistry;

import power.rpc.entity.RpcServiceInfo;

/**
 * @Author: PowerYang
 * @Date: 2019/7/12 19:20
 */
public interface ServiceRegistry {

    void register(RpcServiceInfo serviceInfo);
    void deregister(RpcServiceInfo serviceInfo);
}
