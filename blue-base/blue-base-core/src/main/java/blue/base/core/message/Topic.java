package blue.base.core.message;

import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 2019-06-29
 */
public class Topic {
	protected final String topic;

	public Topic(String topic) {
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}

	@Override
	public String toString() {
		return String.format("Topic[topic: %s]", topic);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Topic topic1 = (Topic) o;
		return topic.equals(topic1.topic);
	}

	@Override
	public int hashCode() {
		return Objects.hash(topic);
	}

}
