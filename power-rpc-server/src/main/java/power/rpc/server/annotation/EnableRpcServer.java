package power.rpc.server.annotation;

import org.springframework.context.annotation.Import;
import power.rpc.server.RpcServerRegistrar;

import java.lang.annotation.*;

/**
 * 启用RPC服务端的注解
 * @Author: PowerYang
 * @Date: 2019/7/1 20:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RpcServerRegistrar.class)
public @interface EnableRpcServer {
}
