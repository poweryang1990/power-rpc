package power.rpc.client.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import power.rpc.client.RpcClientsRegistrar;

import java.lang.annotation.*;

/**
 * @Author: PowerYang
 * @Date: 2019/7/3 14:04
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({RpcClientsRegistrar.class})
public @interface EnableRpcClients {
    @AliasFor("basePackages")
    String[] value() default {};

    @AliasFor("value")
    String[] basePackages() default {};

}
