package test.base.core.id;

import blue.base.core.id.Metadata;
import blue.base.core.id.SnowflakeId;
import blue.base.core.id.SnowflakeIdFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhengjin
 * @since 1.0 2018年05月15日
 */
public class SnowflakeIdTest {
	private SnowflakeId snowflakeId;

	public SnowflakeIdTest() {
	}

	@BeforeEach
	public void setUp() {
		snowflakeId = SnowflakeIdFactory.getSingleSnowflakeId();
	}

	@Test
	public void testTimestamp() {
		print(LocalDate.of(2017, 1, 1));
		print(LocalDate.of(2020, 1, 1));
	}

	@Test
	public void testNextId() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		long id = snowflakeId.nextId();
		Metadata metadata = snowflakeId.getMetadata(id);
		System.out.println(metadata.getMachineId());
		System.out.println(metadata.getSequence());
		System.out.println(metadata.getDateTime());
		System.out.println(now);
		Assertions.assertEquals(0, metadata.getMachineId());
		Assertions.assertEquals(0, metadata.getSequence());
		Assertions.assertEquals(now.format(formatter), metadata.getDateTime().format(formatter));
	}

	private void print(LocalDate date) {
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		System.out.printf("================= %s =================\n", date);
		System.out.printf("毫秒：%s\n", instant.toEpochMilli());
		System.out.printf("秒：%s\n", instant.getEpochSecond());
	}

	@Test
	public void now() {
		Instant now = Instant.parse("2017-01-01T00:00:00.00Z");
		System.out.println(now);
		System.out.println(now.toEpochMilli());
	}

	@Test
	public void test() {
		Set<Long> set = new HashSet<>();
		int count = 1000000;
		long start = System.currentTimeMillis();
		while (set.size() <= count) {
			long val = snowflakeId.nextId();
			if (set.contains(val)) {
				System.out.println("重复：" + val);
			}
			set.add(val);
		}
		System.out.printf("生成 %d 个，用时：%d 毫秒", count, System.currentTimeMillis() - start);
	}

	@Test
	public void testMask() {
		System.out.println(Integer.toBinaryString(~(-1 << 10)));
		System.out.println(Integer.toBinaryString(-1 ^ (-1 << 10)));
	}

}
