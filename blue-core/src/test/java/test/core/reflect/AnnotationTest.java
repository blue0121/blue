package test.core.reflect;

import blue.core.reflect.JavaBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.core.model.Cat;
import test.core.model.MoveAction;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public class AnnotationTest
{
	public AnnotationTest()
	{
	}

	@Test
	public void test1()
	{
		Cat cat = new Cat();
		JavaBean catBean = JavaBean.parse(cat, cat.getClass());
		Assertions.assertNotNull(catBean.getAnnotation(MoveAction.class));
		Assertions.assertNull(catBean.getDeclaredAnnotation(MoveAction.class));
		Assertions.assertEquals(1, catBean.getAnnotations().size());
		Assertions.assertEquals(0, catBean.getDeclaredAnnotations().size());
	}

}
