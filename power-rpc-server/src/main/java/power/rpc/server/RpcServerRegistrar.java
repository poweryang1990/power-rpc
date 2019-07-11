package power.rpc.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import power.rpc.annotation.RpcService;
import power.rpc.server.configs.RpcServerConfig;

import java.util.HashMap;
import java.util.Map;


/**
 * 扫描@RpcService的中的方法 并注入服务端
 * @Author: PowerYang
 * @Date: 2019/7/4 14:50
 */
@EnableConfigurationProperties(RpcServerConfig.class)
@Slf4j
public class RpcServerRegistrar implements ApplicationContextAware, InitializingBean {

    @Autowired
    RpcServerConfig rpcServerConfig;
    /**
     * 存放 服务名称 与 服务实例 之间的映射关系
     */
    private Map<String,Object> serviceMap=new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        //获取带有@RpcService注解的类
        Map<String,Object> rpcServiceMap=applicationContext.getBeansWithAnnotation(RpcService.class);
        if(!CollectionUtils.isEmpty(rpcServiceMap)){
            for(Object serviceBean:rpcServiceMap.values()){
                Class<?> clazz = serviceBean.getClass();
                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> inter : interfaces){
                    String interfaceName = inter.getName();
                    log.info("加载服务类: {}", interfaceName);
                    serviceMap.put(interfaceName, serviceBean);
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        new TcpRpcServer(rpcServerConfig,serviceMap).start();
    }

}
