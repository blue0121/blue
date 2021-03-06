package test.base.core.reflect;

import blue.base.core.reflect.BeanConstructor;
import blue.base.core.reflect.BeanMethod;
import blue.base.core.reflect.JavaBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.base.core.model.Cat;
import test.base.core.model.MoveAction;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public class AnnotationTest {
	public AnnotationTest() {
	}

	@Test
	public void test1() {
		Cat cat = new Cat();
		JavaBean catBean = JavaBean.parse(cat, cat.getClass());
		Assertions.assertNotNull(catBean.getAnnotation(MoveAction.class));
		Assertions.assertNull(catBean.getDeclaredAnnotation(MoveAction.class));
		Assertions.assertEquals(1, catBean.getAnnotations().size());
		Assertions.assertEquals(0, catBean.getDeclaredAnnotations().size());

	}

	@Test
	public void test2() {
		JavaBean catBean = JavaBean.parse(Cat.class);
		List<BeanMethod> methodList = catBean.getAllMethods();
		for (BeanMethod beanMethod : methodList) {
			System.out.println(beanMethod.getName());
		}
		Assertions.assertEquals(1, methodList.size());
		BeanMethod method = methodList.get(0);
		Assertions.assertEquals("move", method.getName());
		Assertions.assertNull(method.getRepresentField());
		Assertions.assertFalse(method.isGetter());
		Assertions.assertFalse(method.isSetter());
		Assertions.assertNull(method.getDeclaredAnnotation(MoveAction.class));
		Assertions.assertNotNull(method.getAnnotation(MoveAction.class));
	}

	@Test
	public void test3() {
		JavaBean catBean = JavaBean.parse(Cat.class);
		List<BeanConstructor> constructorList = catBean.getAllConstructors();
		Assertions.assertEquals(1, constructorList.size());
		Assertions.assertNull(catBean.getConstructor(Integer.class));

		Cat cat = (Cat) catBean.newInstanceQuietly();
		Assertions.assertNotNull(cat);
	}

}
