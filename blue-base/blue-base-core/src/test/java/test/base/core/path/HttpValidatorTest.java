package test.base.core.path;

import blue.base.internal.core.path.route.HttpValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public class HttpValidatorTest {
	public HttpValidatorTest() {
	}

	@Test
	public void testEmpty() {
		Assertions.assertThrows(NullPointerException.class, () -> new HttpValidator(null));
		Assertions.assertThrows(NullPointerException.class, () -> new HttpValidator(""));
	}

	@Test
	public void testValid1() {
		HttpValidator v1 = new HttpValidator("//test");
		Assertions.assertTrue(v1.validate());
		Assertions.assertEquals("/test", v1.getTrimPath());

		HttpValidator v2 = new HttpValidator("//test///id");
		Assertions.assertTrue(v2.validate());
		Assertions.assertEquals("/test/id", v2.getTrimPath());

		HttpValidator v3 = new HttpValidator("//");
		Assertions.assertTrue(v3.validate());
		Assertions.assertEquals("/", v3.getTrimPath());

		HttpValidator v4 = new HttpValidator("test/");
		Assertions.assertTrue(v4.validate());
		Assertions.assertEquals("/test", v4.getTrimPath());
	}

	@Test
	public void testValid2() {
		HttpValidator v1 = new HttpValidator("//{test}");
		Assertions.assertTrue(v1.validate());
		Assertions.assertEquals("/{test}", v1.getTrimPath());

		HttpValidator v2 = new HttpValidator("//test///*");
		Assertions.assertTrue(v2.validate());
		Assertions.assertEquals("/test/*", v2.getTrimPath());

		HttpValidator v3 = new HttpValidator("//test/**");
		Assertions.assertTrue(v3.validate());
		Assertions.assertEquals("/test/**", v3.getTrimPath());

		HttpValidator v4 = new HttpValidator("//test/**///test");
		Assertions.assertTrue(v4.validate());
		Assertions.assertEquals("/test/**/test", v4.getTrimPath());

		HttpValidator v5 = new HttpValidator("//test/*/test/**");
		Assertions.assertTrue(v5.validate());
		Assertions.assertEquals("/test/*/test/**", v5.getTrimPath());

		HttpValidator v6 = new HttpValidator("//**");
		Assertions.assertTrue(v6.validate());
		Assertions.assertEquals("/**", v6.getTrimPath());

		HttpValidator v7 = new HttpValidator("//*");
		Assertions.assertTrue(v7.validate());
		Assertions.assertEquals("/*", v7.getTrimPath());
	}

	@Test
	public void testInValid1() {
		HttpValidator v1 = new HttpValidator("//{test");
		Assertions.assertFalse(v1.validate());

		HttpValidator v2 = new HttpValidator("//{/");
		Assertions.assertFalse(v2.validate());

		HttpValidator v3 = new HttpValidator("//{test/");
		Assertions.assertFalse(v3.validate());

		HttpValidator v4 = new HttpValidator("}");
		Assertions.assertFalse(v4.validate());

		HttpValidator v5 = new HttpValidator("//}/");
		Assertions.assertFalse(v5.validate());

		HttpValidator v6 = new HttpValidator("//{test/}/");
		Assertions.assertFalse(v6.validate());

		HttpValidator v7 = new HttpValidator("{");
		Assertions.assertFalse(v7.validate());

		HttpValidator v8 = new HttpValidator("/test/{{test");
		Assertions.assertFalse(v8.validate());

		HttpValidator v9 = new HttpValidator("/test/{{test}");
		Assertions.assertFalse(v9.validate());

		HttpValidator v10 = new HttpValidator("/test/{{test}}");
		Assertions.assertFalse(v10.validate());
	}

	@Test
	public void testInvalid2() {
		HttpValidator v1 = new HttpValidator("/***");
		Assertions.assertFalse(v1.validate());

		HttpValidator v2 = new HttpValidator("***");
		Assertions.assertFalse(v2.validate());

		HttpValidator v3 = new HttpValidator("/{test*}");
		Assertions.assertFalse(v3.validate());

		HttpValidator v4 = new HttpValidator("/{test**}");
		Assertions.assertFalse(v4.validate());

		HttpValidator v5 = new HttpValidator("/{test}*");
		Assertions.assertFalse(v5.validate());

		HttpValidator v6 = new HttpValidator("/{test}**");
		Assertions.assertFalse(v6.validate());

	}

}
