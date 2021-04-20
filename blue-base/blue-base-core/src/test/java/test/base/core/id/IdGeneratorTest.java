package test.base.core.id;

import blue.base.core.id.IdGenerator;
import blue.base.core.id.SnowflakeId;
import blue.base.core.id.SnowflakeIdFactory;
import org.junit.jupiter.api.Test;

public class IdGeneratorTest {
	public IdGeneratorTest() {
	}

	@Test
	public void testUUid32bit() {
		String uuid = IdGenerator.uuid32bit();
		System.out.println(uuid + " | " + uuid.length());
		uuid = IdGenerator.uuid32bit();
		System.out.println(uuid + " | " + uuid.length());
		uuid = IdGenerator.uuid32bit();
		System.out.println(uuid + " | " + uuid.length());
	}

	@Test
	public void testUUid12bit() {
		String uuid = IdGenerator.uuid12bit();
		System.out.println(uuid + " | " + uuid.length());
		uuid = IdGenerator.uuid12bit();
		System.out.println(uuid + " | " + uuid.length());
		uuid = IdGenerator.uuid12bit();
		System.out.println(uuid + " | " + uuid.length());

		SnowflakeId snowflakeId = SnowflakeIdFactory.getSingleSnowflakeId();
		long id = snowflakeId.nextId();
		System.out.println(id);
		id = snowflakeId.nextId();
		System.out.println(id);
		id = snowflakeId.nextId();
		System.out.println(id);
		id = snowflakeId.nextId();
		System.out.println(id);
	}

}
