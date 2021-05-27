package blue.jms.internal.spring.bean;

import blue.base.core.message.ProducerListener;
import blue.jms.core.JmsClient;
import blue.jms.core.JmsProducer;
import blue.jms.core.options.JmsProducerOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-27
 */
public class JmsProducerFactoryBean implements FactoryBean<JmsProducer>, InitializingBean {
	private String id;
	private ProducerListener producerListener;
	private Executor executor;

	private JmsClient jmsClient;
	private JmsProducer producer;

	public JmsProducerFactoryBean() {
	}

	@Override
	public JmsProducer getObject() throws Exception {
		return producer;
	}

	@Override
	public Class<?> getObjectType() {
		return JmsProducer.class;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		JmsProducerOptions options = new JmsProducerOptions();
		options.setId(id).setProducerListener(producerListener);
		options.setExecutor(executor);
		this.producer = jmsClient.createProducer(options);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProducerListener(ProducerListener producerListener) {
		this.producerListener = producerListener;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public void setJmsClient(JmsClient jmsClient) {
		this.jmsClient = jmsClient;
	}
}
