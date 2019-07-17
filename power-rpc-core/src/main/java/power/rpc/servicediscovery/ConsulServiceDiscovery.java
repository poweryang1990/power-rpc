package power.rpc.servicediscovery;

import power.rpc.entity.RpcServiceInfo;

import java.util.List;

/**
 * @Author: PowerYang
 * @Date: 2019/7/12 19:26
 */
public class ConsulServiceDiscovery implements ServiceDiscovery {
    @Override
    public List<RpcServiceInfo> getServiceInstances(String serviceName) {
        return null;
    }
}
