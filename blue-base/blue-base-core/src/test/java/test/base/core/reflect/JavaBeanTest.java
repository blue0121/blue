package test.base.core.reflect;

import blue.base.core.reflect.BeanField;
import blue.base.core.reflect.JavaBean;
import blue.base.core.reflect.JavaBeanCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.base.core.model.CoreTest;
import test.base.core.model.TestModel;
import test.base.core.model.TestModel2;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2020-07-26
 */
public class JavaBeanTest {
	public JavaBeanTest() {
	}

	@Test
	public void test1() {
		TestModel model = new TestModel();
		JavaBean bean = JavaBean.parse(model, TestModel.class);
		Assertions.assertEquals(TestModel.class, bean.getTargetClass());
		Assertions.assertEquals(model, bean.getTarget());
		Assertions.assertNull(bean.getAnnotation(CoreTest.class));
		Map<String, BeanField> fieldMap = bean.getAllFields();
		Assertions.assertEquals(2, fieldMap.size());
		BeanField field1 = fieldMap.get("username");
		Assertions.assertNotNull(field1);
		Assertions.assertEquals("getUsername", field1.getGetterMethod().getName());
		Assertions.assertEquals("setUsername", field1.getSetterMethod().getName());
		Assertions.assertEquals("username", field1.getColumnName());
		Assertions.assertNotNull(field1.getSetterAnnotation(CoreTest.class));
		BeanField field2 = fieldMap.get("password");
		Assertions.assertNotNull(field2);
		Assertions.assertEquals("getPassword", field2.getGetterMethod().getName());
		Assertions.assertEquals("setPassword", field2.getSetterMethod().getName());
		Assertions.assertEquals("password", field2.getColumnName());
		Assertions.assertNotNull(field2.getSetterAnnotation(CoreTest.class));
		field1.setFieldValue(null, "name");
		field2.setFieldValue(null, "pass");
		Assertions.assertEquals("name", model.getUsername());
		Assertions.assertEquals("pass", model.getPassword());
		Assertions.assertEquals("name", field1.getFieldValue(null));
		Assertions.assertEquals("pass", field2.getFieldValue(null));
	}

	@Test
	public void test2() {
		TestModel2 model = new TestModel2();
		JavaBean bean = JavaBean.parse(model, TestModel2.class);
		Assertions.assertEquals(TestModel2.class, bean.getTargetClass());
		Assertions.assertEquals(model, bean.getTarget());
		Assertions.assertNull(bean.getAnnotation(CoreTest.class));
		Map<String, BeanField> fieldMap = bean.getAllFields();
		Assertions.assertEquals(2, fieldMap.size());
		BeanField field1 = fieldMap.get("username");
		Assertions.assertNotNull(field1);
		Assertions.assertEquals("getUsername", field1.getGetterMethod().getName());
		Assertions.assertEquals("setUsername", field1.getSetterMethod().getName());
		Assertions.assertNotNull(field1.getAnnotation(CoreTest.class));
		BeanField field2 = fieldMap.get("password");
		Assertions.assertNotNull(field2);
		Assertions.assertEquals("getPassword", field2.getGetterMethod().getName());
		Assertions.assertEquals("setPassword", field2.getSetterMethod().getName());
		Assertions.assertNotNull(field2.getAnnotation(CoreTest.class));
		field1.setFieldValue(null, "name");
		field2.setFieldValue(null,"pass");
		Assertions.assertEquals("name", model.getUsername());
		Assertions.assertEquals("pass", model.getPassword());
		Assertions.assertEquals("name", field1.getFieldValue(null));
		Assertions.assertEquals("pass", field2.getFieldValue(null));
	}

	@Test
	public void testCache() {
		JavaBean bean1 = JavaBeanCache.getJavaBean(TestModel2.class);
		JavaBean bean2 = JavaBeanCache.getJavaBean(TestModel2.class);
		Assertions.assertTrue(bean1 == bean2);
	}

}
