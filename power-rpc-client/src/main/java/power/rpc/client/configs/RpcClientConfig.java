package power.rpc.client.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import power.rpc.RpcProtocol;
import power.rpc.serialization.SerializeType;

/**
 * @Author: PowerYang
 * @Date: 2019/7/3 11:56
 */
@Configuration
@ConfigurationProperties(prefix = "rpc")
@Data
public class RpcClientConfig {
    private RpcProtocol protocol=RpcProtocol.TCP;
    private  String host="127.0.0.1";
    private  int port=8000;
    private SerializeType serializeType= SerializeType.JSON;
}
