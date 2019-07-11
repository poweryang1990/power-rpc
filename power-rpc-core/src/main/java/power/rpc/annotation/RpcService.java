package power.rpc.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 标识RPC服务的注解
 * @Author: PowerYang
 * @Date: 2019/7/1 20:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RpcService {
}
