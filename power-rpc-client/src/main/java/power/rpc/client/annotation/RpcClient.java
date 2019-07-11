package power.rpc.client.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author: PowerYang
 * @Date: 2019/7/3 12:02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RpcClient {
    /**
     * 服务发现的服务名
     * @return
     */
    @AliasFor("name")
    String value() default "";
    @AliasFor("value")
    String name() default "";

}
