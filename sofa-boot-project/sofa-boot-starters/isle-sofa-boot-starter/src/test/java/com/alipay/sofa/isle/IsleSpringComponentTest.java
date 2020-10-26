package com.alipay.sofa.isle;

import com.alipay.sofa.isle.stage.ModelCreatingStage;
import com.alipay.sofa.runtime.api.component.ComponentName;
import com.alipay.sofa.runtime.spi.component.ComponentManager;
import com.alipay.sofa.runtime.spi.component.SofaRuntimeContext;
import com.alipay.sofa.runtime.spi.spring.SofaRuntimeContextAware;
import com.alipay.sofa.runtime.spi.util.ComponentNameFactory;
import com.alipay.sofa.runtime.spring.SpringComponent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author <a href="mailto:guaner.zzx@alipay.com">Alaneuler</a>
 * Created on 2020/10/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.application.name=IsleSpringComponentTest")
public class IsleSpringComponentTest implements SofaRuntimeContextAware {
    private SofaRuntimeContext sofaRuntimeContext;

    @Test
    public void test() {
        ComponentManager componentManager = sofaRuntimeContext.getComponentManager();
        ComponentName componentName1 = ComponentNameFactory.createComponentName(
                SpringComponent.SPRING_COMPONENT_TYPE, "com.alipay.sofa.isle.module1");
        ComponentName componentName2 = ComponentNameFactory.createComponentName(
                SpringComponent.SPRING_COMPONENT_TYPE, "com.alipay.sofa.isle.module2");
        Assert.assertNotNull(componentManager.getComponentInfo(componentName1));
        Assert.assertNotNull(componentManager.getComponentInfo(componentName2));
    }

    @Override
    public void setSofaRuntimeContext(SofaRuntimeContext sofaRuntimeContext) {
        this.sofaRuntimeContext = sofaRuntimeContext;
    }

    @Configuration
    @EnableAutoConfiguration
    static class IsleSpringComponentTestConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public ModelCreatingStage modelCreatingStage(ApplicationContext applicationContext) {
            return new TestModelCreatingStage((AbstractApplicationContext) applicationContext, "module1", "module2");
        }
    }
}
