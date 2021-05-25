package blue.kafka.internal.spring.bean;

import blue.base.spring.common.ConsumerConfig;
import blue.kafka.core.OffsetManager;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class KafkaConsumerConfig extends ConsumerConfig {
	private Integer count;
	private Integer duration;
	private String group;
	private OffsetManager offsetManager;

	public KafkaConsumerConfig() {
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public OffsetManager getOffsetManager() {
		return offsetManager;
	}

	public void setOffsetManager(OffsetManager offsetManager) {
		this.offsetManager = offsetManager;
	}
}
