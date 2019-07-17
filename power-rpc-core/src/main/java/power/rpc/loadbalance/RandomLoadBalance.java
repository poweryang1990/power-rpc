package power.rpc.loadbalance;

import power.rpc.entity.RpcServiceInfo;

import java.util.List;

/**
 * 随机算法
 * @Author: PowerYang
 * @Date: 2019/7/15 12:02
 */
public class RandomLoadBalance implements  LoadBalance {
    @Override
    public RpcServiceInfo getService(List<RpcServiceInfo> serviceInfoList, String requestIp) {
        return null;
    }
}
