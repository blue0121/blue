package blue.base.internal.core.id;

import blue.base.core.id.Metadata;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-28
 */
public class DefaultSnowflakeIdMetadata implements Metadata {
	private LocalDateTime dateTime;
	private long sequence;
	private long machineId;

	public DefaultSnowflakeIdMetadata(long timestamp, long sequence, long machineId) {
		LocalDateTime dateTime = Instant.ofEpochMilli(timestamp)
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		this.dateTime = dateTime;
		this.sequence = sequence;
		this.machineId = machineId;
	}

	public DefaultSnowflakeIdMetadata(LocalDateTime dateTime, long sequence, long machineId) {
		this.dateTime = dateTime;
		this.sequence = sequence;
		this.machineId = machineId;
	}

	@Override
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	@Override
	public long getSequence() {
		return sequence;
	}

	@Override
	public long getMachineId() {
		return machineId;
	}
}
