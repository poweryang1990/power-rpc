package power.rpc.servicediscovery;

import power.rpc.entity.RpcServiceInfo;

import java.util.List;

/**
 * @Author: PowerYang
 * @Date: 2019/7/12 19:20
 */
public interface ServiceDiscovery {

   List<RpcServiceInfo> getServiceInstances(String  serviceName);
}
