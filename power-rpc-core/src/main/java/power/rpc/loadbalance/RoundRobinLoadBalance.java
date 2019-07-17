package power.rpc.loadbalance;

import power.rpc.entity.RpcServiceInfo;

import java.util.List;

/**
 * 轮询算法
 * @Author: PowerYang
 * @Date: 2019/7/15 12:02
 */
public class RoundRobinLoadBalance implements LoadBalance {
    @Override
    public RpcServiceInfo getService(List<RpcServiceInfo> serviceInfoList, String requestIp) {
        return null;
    }
}
