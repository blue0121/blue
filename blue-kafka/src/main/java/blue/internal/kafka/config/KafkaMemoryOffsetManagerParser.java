package blue.internal.kafka.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.kafka.offset.MemoryOffsetManager;

/**
 * Kafka 内存 Offset 管理解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-11
 */
public class KafkaMemoryOffsetManagerParser extends SimpleBeanDefinitionParser
{
	public KafkaMemoryOffsetManagerParser()
	{
		this.clazz = MemoryOffsetManager.class;
	}

}
