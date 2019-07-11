package power.rpc.server.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import power.rpc.RpcProtocol;
import power.rpc.serialization.SerializeType;

/**
 * @Author: PowerYang
 * @Date: 2019/7/3 10:56
 */
@Configuration
@ConfigurationProperties(prefix = "rpc.server")
@Data
public class RpcServerConfig {
    /**
     * 端口
     */
    private int port=8000;

    /**
     * 协议类型 默认TCP
     * @return
     */
    private RpcProtocol protocol= RpcProtocol.TCP;


    private SerializeType serializeType= SerializeType.JSON;

}
