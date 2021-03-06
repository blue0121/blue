package blue.jms.internal.core.consumer;

import blue.base.core.message.Topic;
import blue.base.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author Jin Zheng
 * @since 1.0 2019-08-02
 */
public class DefaultMessageListener implements MessageListener {
	private static Logger logger = LoggerFactory.getLogger(DefaultMessageListener.class);

	private final Topic topic;
	private final JmsListenerConfig listenerConfig;

	public DefaultMessageListener(Topic topic, JmsListenerConfig listenerConfig) {
		this.topic = topic;
		this.listenerConfig = listenerConfig;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message message) {
		Object object = null;
		try {
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				object = JsonUtil.fromString(textMessage.getText(), listenerConfig.getClazz());
				if (listenerConfig.isMultiThread()) {
					Object tmp = object;
					listenerConfig.getExecutor().execute(() -> listenerConfig.getListener().onReceive(topic, tmp));
				}
				else {
					listenerConfig.getListener().onReceive(topic, object);
				}
			}
			else {
				logger.warn("Not text message");
			}
		}
		catch (Exception e) {
			listenerConfig.getExceptionHandler().onError(topic, object, e);
		}
	}

}
