package blue.base.internal.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * core自定义命名空间注册处理器
 *
 * @author Jin Zheng
 * @date 2018-10-31
 */
public class CoreNamespaceHandler extends NamespaceHandlerSupport
{
	public static final String NS = "http://blue.com/schema/base";

	public CoreNamespaceHandler()
	{
	}

	@Override
	public void init()
	{
		this.registerBeanDefinitionParser("dict-post-processor", new DictPostProcessorParser());
		this.registerBeanDefinitionParser("task-executor", new TaskExecutorParser());
		this.registerBeanDefinitionParser("task-scheduler", new TaskSchedulerParser());
		this.registerBeanDefinitionParser("spring-bean", new SpringBeanParser());
		/*this.registerBeanDefinitionParser("encryption-placeholder", new EncryptionPlaceholderParser());*/
		this.registerBeanDefinitionParser("http-client", new HttpClientParser());
	}
}
