package power.rpc.client;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import power.rpc.client.configs.RpcClientConfig;

/**
 * 代理bean注入
 * @Author: PowerYang
 * @Date: 2019/7/3 16:52
 */
@Data
public class RpcProxyFactoryBean<T> implements FactoryBean<T>, InitializingBean, ApplicationContextAware {

    private String interfaceClassName;
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(interfaceClassName,"rpcRequest must be not null");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Override
    public T getObject() throws Exception {

        RpcClientConfig rpcClientConfig= null;
        try {
            rpcClientConfig = this.applicationContext.getBean(RpcClientConfig.class);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        return RpcProxy.create(getObjectType(),rpcClientConfig);
    }

    @Override
    public Class<?> getObjectType() {
        if (interfaceClassName==null){
            return  null;
        }
        try {
            return Class.forName(interfaceClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }


}
