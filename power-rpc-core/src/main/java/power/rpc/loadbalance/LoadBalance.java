package power.rpc.loadbalance;

import power.rpc.entity.RpcServiceInfo;

import java.util.List;

/**
 * @Author: PowerYang
 * @Date: 2019/7/15 11:56
 */
public interface LoadBalance {

    RpcServiceInfo  getService(List<RpcServiceInfo> serviceInfoList,String requestIp);
}
