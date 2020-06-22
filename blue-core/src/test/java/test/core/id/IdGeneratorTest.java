package test.core.id;

import blue.core.id.IdGenerator;
import blue.core.id.SingleSnowflakeId;
import org.junit.jupiter.api.Test;

public class IdGeneratorTest
{
	public IdGeneratorTest()
	{
	}
	
	@Test
	public void testUUid32bit()
	{
		String uuid = IdGenerator.uuid32bit();
		System.out.println(uuid + " | " + uuid.length());
		uuid = IdGenerator.uuid32bit();
		System.out.println(uuid + " | " + uuid.length());
		uuid = IdGenerator.uuid32bit();
		System.out.println(uuid + " | " + uuid.length());
	}
	
	@Test
	public void testUUid12bit()
	{
		String uuid = IdGenerator.uuid12bit();
		System.out.println(uuid + " | " + uuid.length());
		uuid = IdGenerator.uuid12bit();
		System.out.println(uuid + " | " + uuid.length());
		uuid = IdGenerator.uuid12bit();
		System.out.println(uuid + " | " + uuid.length());
		
		long id = SingleSnowflakeId.getInstance().nextId();
		System.out.println(id);
		id = SingleSnowflakeId.getInstance().nextId();
		System.out.println(id);
		id = SingleSnowflakeId.getInstance().nextId();
		System.out.println(id);
		id = SingleSnowflakeId.getInstance().nextId();
		System.out.println(id);
	}

}
