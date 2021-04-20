package test.base.core.id;

import blue.base.core.id.MachineIdProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

/**
 * @author zhengjin
 * @since 1.0 2018年09月12日
 */
public class MachineIdProviderTest {
	private MachineIdProvider provider;

	public MachineIdProviderTest() {
		InputStream is = MachineIdProviderTest.class.getResourceAsStream("/machineid.properties");
		provider = new MachineIdProvider();
		provider.read(is);
	}

	@Test
	public void test() {
		Integer id = provider.getMachineId();
		System.out.println(id);
		Assertions.assertEquals(0, id.intValue(), "机器ID错误");
	}

	@Test
	public void test2() {
		MachineIdProvider provider = new MachineIdProvider("/machineid.properties");
		Integer id = provider.getMachineId();
		System.out.println(id);
		Assertions.assertEquals(0, id.intValue(), "机器ID错误");
	}

	@Test
	public void testMap() {
		Map<String, Integer> map = provider.getMachineIdMap();
		Assertions.assertEquals(2, map.size(), "个数错误");

		Integer id = map.get("127.0.0.1");
		Assertions.assertTrue(id != null);
		Assertions.assertEquals(0, id.intValue(), "机器ID错误");

		id = map.get("192.168.31.1");
		Assertions.assertTrue(id != null);
		Assertions.assertEquals(1, id.intValue(), "机器ID错误");
	}

}
