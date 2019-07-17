package power.rpc.entity;

import lombok.Data;

/**
 * 描述服务的信息
 * 用于服务注册和发现
 * @Author: PowerYang
 * @Date: 2019/7/12 19:10
 */
@Data
public class RpcServiceInfo {
    private  String instanceId;
    private  String name;
    private String host;
    private  String port;
    private  int weight=1;
}
