package test.base.spring.dict;

import blue.base.core.dict.DictParser;
import blue.base.core.dict.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author Jin Zheng
 * @since 2019-04-21
 */
@SpringJUnitConfig(locations = {"classpath:/spring/dict.xml"})
public class DictPostProcessorTest {
	private static DictParser dictParser = DictParser.getInstance();

	public DictPostProcessorTest() {
	}

	@Test
	public void testState() {
		State normal = dictParser.getFromIndex(State.class, 0);
		Assertions.assertEquals(normal, State.NORMAL);
		State delete = dictParser.getFromIndex(State.class, 1);
		Assertions.assertEquals(delete, State.DELETE);

		Assertions.assertEquals(0, dictParser.getFromObject(State.NORMAL).intValue());
		Assertions.assertEquals(1, dictParser.getFromObject(State.DELETE).intValue());
	}

	@Test
	public void testTest() {
		test.base.spring.dict.Test one = dictParser.getFromIndex(test.base.spring.dict.Test.class, 1);
		Assertions.assertEquals(one, test.base.spring.dict.Test.ONE);

		Assertions.assertEquals(1, dictParser.getFromObject(test.base.spring.dict.Test.ONE).intValue());
	}

}
