package blue.jms.internal.spring.bean;

import blue.base.spring.common.ConsumerFactoryBean;
import blue.jms.core.JmsClient;
import blue.jms.core.JmsConsumer;
import blue.jms.core.JmsException;
import blue.jms.core.JmsTopic;
import blue.jms.core.JmsType;
import blue.jms.core.options.JmsConsumerOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-27
 */
public class JmsConsumerFactoryBean extends ConsumerFactoryBean
		implements FactoryBean<JmsConsumer>, InitializingBean {
	private JmsType defaultType;

	private JmsClient jmsClient;
	private List<JmsConsumerConfig> configList;
	private JmsConsumer jmsConsumer;
	private List<JmsConsumer> consumerList = new ArrayList<>();

	public JmsConsumerFactoryBean() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (configList == null || configList.isEmpty()) {
			throw new JmsException("JMS consumer listener config is empty");
		}

		JmsConsumerOptions options = new JmsConsumerOptions();
		this.setConsumerOptions(options);
		options.setDefaultType(defaultType);
		this.jmsConsumer = jmsClient.createConsumer(options);
		this.createJmsConsumers();
	}

	private void createJmsConsumers() {
		for (var config : configList) {
			JmsConsumerOptions options = new JmsConsumerOptions();
			this.setConsumerOptions(options);
			this.setConsumerOptions(options, config);
			if (config.getType() == null) {
				config.setType(options.getDefaultType());
			}

			JmsConsumer consumer = jmsClient.createConsumer(options);
			consumer.subscribe(new JmsTopic(config.getTopic(), config.getType()), config.getListener());
			consumerList.add(consumer);
		}
	}

	@Override
	public JmsConsumer getObject() throws Exception {
		return jmsConsumer;
	}

	@Override
	public Class<?> getObjectType() {
		return JmsConsumer.class;
	}

	public void setDefaultType(JmsType defaultType) {
		this.defaultType = defaultType;
	}

	public void setJmsClient(JmsClient jmsClient) {
		this.jmsClient = jmsClient;
	}

	public void setConfigList(List<JmsConsumerConfig> configList) {
		this.configList = configList;
	}
}
