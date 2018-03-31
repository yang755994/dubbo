/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.config.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ModuleConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.config.spring.ServiceBean;

/**
 * DubboNamespaceHandler
 * 
 * @author william.liangf
 * @export
 */
// 1、基于dubbo.jar内的META-INF/spring.handlers配置，Spring在遇到dubbo名称空间时，会回调DubboNamespaceHandler。
// 2、所有dubbo的标签，都统一用DubboBeanDefinitionParser进行解析，基于一对一属性映射，将XML标签解析为Bean对象。
// 3、在ServiceConfig.export()或ReferenceConfig.get()初始化时，将Bean对象转换URL格式，所有Bean属性转成URL的参数。
// 4、然后将URL传给Protocol扩展点，基于扩展点的Adaptive机制，根据URL的协议头，进行不同协议的服务暴露或引用
public class DubboNamespaceHandler extends NamespaceHandlerSupport {

	static {
		Version.checkDuplicate(DubboNamespaceHandler.class);
	}

    /**
     * 用来把节点名和解析类联系起来，在配置中引用<dubbo:service等配置项时，就会用DubboBeanDefinitionParser来解析配置
     */
	public void init() {
        /**
         * 从这里也可以看到，对应的支持的标签其实不多。所有的 Parser 都封装到了DubboBeanDefinitionParser 中。
         * 对应的 class，就是传入的 beanClass。比如 application 的就是ApplicationConfig。 module 的就是 ModuleConfig。
         * 经过 Parser 的转换， provider.xml 大概可以 变成如下的样子(具体的解析不多解释了)
         <bean id="hello-world-app" class="com.alibaba.dubbo.config.ApplicationConfig"/>
         <bean id="registryConfig" class="com.alibaba.dubbo.config.RegistryConfig">
             <property name="address" value="localhost:2181"/>
             <property name="protocol" value="zookeeper"/>
         </bean>
         <bean id="dubbo" class="com.alibaba.dubbo.config.ProtocolConfig">
            <property name="port" value="20880"/>
         </bean>
         <bean id="demo.service.DemoService" class="com.alibaba.dubbo.config.spring.ServiceBean">
            <property name="interface" value="demo.service.DemoService"/>
            <property name="ref" ref="demoService"/>
         </bean>
         <bean id="demoService" class="demo.service.DemoServiceImpl" />

         */
	    registerBeanDefinitionParser("application", new DubboBeanDefinitionParser(ApplicationConfig.class, true));
        registerBeanDefinitionParser("module", new DubboBeanDefinitionParser(ModuleConfig.class, true));
        registerBeanDefinitionParser("registry", new DubboBeanDefinitionParser(RegistryConfig.class, true));
        registerBeanDefinitionParser("monitor", new DubboBeanDefinitionParser(MonitorConfig.class, true));
        registerBeanDefinitionParser("provider", new DubboBeanDefinitionParser(ProviderConfig.class, true));
        registerBeanDefinitionParser("consumer", new DubboBeanDefinitionParser(ConsumerConfig.class, true));
        registerBeanDefinitionParser("protocol", new DubboBeanDefinitionParser(ProtocolConfig.class, true));
        registerBeanDefinitionParser("service", new DubboBeanDefinitionParser(ServiceBean.class, true));
        registerBeanDefinitionParser("reference", new DubboBeanDefinitionParser(ReferenceBean.class, false));
        registerBeanDefinitionParser("annotation", new DubboBeanDefinitionParser(AnnotationBean.class, true));
    }

}