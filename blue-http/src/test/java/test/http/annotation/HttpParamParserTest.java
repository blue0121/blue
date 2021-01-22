package test.http.annotation;

import blue.http.annotation.BodyJson;
import blue.http.annotation.HttpMethod;
import blue.http.annotation.Multipart;
import blue.http.message.UploadFile;
import blue.internal.http.annotation.HttpConfigCache;
import blue.internal.http.annotation.HttpUrlKey;
import blue.internal.http.annotation.HttpUrlParamConfig;
import blue.internal.http.annotation.RequestParamConfig;
import blue.internal.http.parser.HttpMethodResult;
import blue.validation.group.SaveModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.http.BaseTest;
import test.http.model.User;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-19
 */
public class HttpParamParserTest extends BaseTest
{
	private HttpConfigCache parserCache = HttpConfigCache.getInstance();

	public HttpParamParserTest()
	{
	}

	@Test
	public void testMultiPart()
	{
		HttpUrlKey key = new HttpUrlKey("/upload/index2", HttpMethod.POST);
		HttpMethodResult result = parserCache.getConfig(key);
		Assertions.assertNotNull(result);
		List<RequestParamConfig> paramList = result.getParamList();
		Assertions.assertEquals(1, paramList.size());
		RequestParamConfig uploadParam = paramList.get(0);
		Assertions.assertEquals(Multipart.class, uploadParam.getParamAnnotation().annotationType());
		Assertions.assertEquals(UploadFile.class, uploadParam.getParamClazz());
		Assertions.assertEquals("file", uploadParam.getParamAnnotation());
		Assertions.assertTrue(uploadParam.isRequired());
		Assertions.assertFalse(uploadParam.isValidated());
	}

	@Test
	public void testValidated()
	{
		HttpUrlKey key = new HttpUrlKey("/echo/validate", HttpMethod.POST);
		HttpMethodResult result = parserCache.getConfig(key);
		Assertions.assertNotNull(result);
		List<HttpUrlParamConfig> paramList = result.getParamList();
		Assertions.assertEquals(1, paramList.size());
		HttpUrlParamConfig uploadParam = paramList.get(0);
		Assertions.assertEquals(BodyJson.class, uploadParam.getParamAnnotation().annotationType());
		Assertions.assertEquals(User.class, uploadParam.getParamClazz());
		Assertions.assertEquals("$.user", uploadParam.getValue());
		Assertions.assertTrue(uploadParam.isRequired());
		Assertions.assertTrue(uploadParam.isValidated());
		Assertions.assertArrayEquals(new Class<?>[] {SaveModel.class}, uploadParam.getValidatedGroups());
	}

}
