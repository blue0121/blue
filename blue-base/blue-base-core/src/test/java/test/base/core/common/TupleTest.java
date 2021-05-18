package test.base.core.common;

import blue.base.core.common.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-17
 */
public class TupleTest {
	public TupleTest() {
	}

	@Test
	public void test1() {
        Tuple tuple = new Tuple(1, 2);
        Assertions.assertEquals(Integer.valueOf(1), tuple.getFirst());
        Assertions.assertEquals(Integer.valueOf(2), tuple.getSecond());
        Assertions.assertEquals(Integer.valueOf(1), tuple.getN(1));
        Assertions.assertEquals(Integer.valueOf(2), tuple.getN(2));
        Assertions.assertNull(tuple.getThird());
        Assertions.assertNull(tuple.getN(3));
        Assertions.assertNull(tuple.getN(4));
    }

}
