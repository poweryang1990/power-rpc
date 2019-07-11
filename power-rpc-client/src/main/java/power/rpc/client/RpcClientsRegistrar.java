package power.rpc.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import power.rpc.client.annotation.EnableRpcClients;
import power.rpc.client.annotation.RpcClient;
import power.rpc.client.configs.RpcClientConfig;
import power.rpc.entity.RpcRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @Author: PowerYang
 * @Date: 2019/7/3 13:56
 */
@Slf4j
public class RpcClientsRegistrar  implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader=resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        this.registerRpcClients(annotationMetadata, beanDefinitionRegistry);
    }

    public void registerRpcClients(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = this.getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(RpcClient.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        Object basePackages = this.getBasePackages(metadata);

        Iterator var17 = ((Set)basePackages).iterator();

        while(var17.hasNext()) {
            String basePackage = (String)var17.next();
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            Iterator var21 = candidateComponents.iterator();

            while(var21.hasNext()) {
                BeanDefinition candidateComponent = (BeanDefinition)var21.next();
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition)candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    Assert.isTrue(annotationMetadata.isInterface(), "@RpcClient can only be specified on an interface");
                    Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(RpcClient.class.getCanonicalName());

                    this.registerRpcClient(registry, annotationMetadata, attributes);
                }
            }
        }

    }

    private void registerRpcClient(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(RpcProxyFactoryBean.class);

        definition.addPropertyValue("interfaceClassName", className);

        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();

//        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, null);
//        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);

    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation()) {
                    isCandidate = true;
                }

                return isCandidate;
            }
        };
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableRpcClients.class.getCanonicalName());
        Set<String> basePackages = new HashSet();
        String[] var4 = (String[])((String[])attributes.get("value"));
        int var5 = var4.length;

        int var6;
        String pkg;
        for(var6 = 0; var6 < var5; ++var6) {
            pkg = var4[var6];
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        var4 = (String[])((String[])attributes.get("basePackages"));
        var5 = var4.length;

        for(var6 = 0; var6 < var5; ++var6) {
            pkg = var4[var6];
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        return basePackages;
    }

}
