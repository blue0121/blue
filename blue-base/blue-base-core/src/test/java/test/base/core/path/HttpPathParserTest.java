package test.base.core.path;

import blue.base.internal.core.path.route.HttpPathParser;
import blue.base.internal.core.path.route.MatchType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2021-06-07
 */
public class HttpPathParserTest {
    public HttpPathParserTest() {
    }

    @Test
    public void testEmpty() {
        Assertions.assertThrows(NullPointerException.class, () -> new HttpPathParser(null));
        Assertions.assertThrows(NullPointerException.class, () -> new HttpPathParser(""));
    }

    @Test
    public void testValid1() {
        HttpPathParser p1 = new HttpPathParser("//test");
        Assertions.assertTrue(p1.parse());
        Assertions.assertEquals("/test", p1.getTrimPath());
        Assertions.assertEquals(MatchType.EXACTLY, p1.getMatchType());

        HttpPathParser p2 = new HttpPathParser("//test///id");
        Assertions.assertTrue(p2.parse());
        Assertions.assertEquals("/test/id", p2.getTrimPath());
        Assertions.assertEquals(MatchType.EXACTLY, p2.getMatchType());

        HttpPathParser p3 = new HttpPathParser("//");
        Assertions.assertTrue(p3.parse());
        Assertions.assertEquals("/", p3.getTrimPath());
        Assertions.assertEquals(MatchType.EXACTLY, p3.getMatchType());

        HttpPathParser p4 = new HttpPathParser("test/");
        Assertions.assertTrue(p4.parse());
        Assertions.assertEquals("/test", p4.getTrimPath());
        Assertions.assertEquals(MatchType.EXACTLY, p4.getMatchType());
    }

    @Test
    public void testValid2() {
        HttpPathParser p1 = new HttpPathParser("//{test}");
        Assertions.assertTrue(p1.parse());
        Assertions.assertEquals("/{test}", p1.getTrimPath());
        Assertions.assertEquals(MatchType.VARIABLE, p1.getMatchType());

        HttpPathParser p2 = new HttpPathParser("/{test}a{p1}");
        Assertions.assertTrue(p2.parse());
        Assertions.assertEquals("/{test}a{p1}", p2.getTrimPath());
        Assertions.assertEquals(MatchType.VARIABLE, p2.getMatchType());
    }

    @Test
    public void testInValid1() {
        HttpPathParser p1 = new HttpPathParser("//{test");
        Assertions.assertFalse(p1.parse());

        HttpPathParser p2 = new HttpPathParser("//{/");
        Assertions.assertFalse(p2.parse());

        HttpPathParser p3 = new HttpPathParser("//{test/");
        Assertions.assertFalse(p3.parse());

        HttpPathParser p4 = new HttpPathParser("}");
        Assertions.assertFalse(p4.parse());

        HttpPathParser p5 = new HttpPathParser("//}/");
        Assertions.assertFalse(p5.parse());

        HttpPathParser p6 = new HttpPathParser("//{test/}/");
        Assertions.assertFalse(p6.parse());

        HttpPathParser p7 = new HttpPathParser("{");
        Assertions.assertFalse(p7.parse());

        HttpPathParser p8 = new HttpPathParser("/test/{{test");
        Assertions.assertFalse(p8.parse());

        HttpPathParser p9 = new HttpPathParser("/test/{{test}");
        Assertions.assertFalse(p9.parse());

        HttpPathParser p10 = new HttpPathParser("/test/{{test}}");
        Assertions.assertFalse(p10.parse());

        HttpPathParser p11 = new HttpPathParser("/test/{test}{p1}");
        Assertions.assertFalse(p11.parse());
    }
}
