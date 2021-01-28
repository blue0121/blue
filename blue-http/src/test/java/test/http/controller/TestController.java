package test.http.controller;

import blue.http.annotation.Http;
import blue.http.annotation.HttpMethod;
import blue.http.annotation.PathVariable;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
@Http(url = "/test/{name}", method = HttpMethod.GET)
public interface TestController
{

	String test(@PathVariable("name") String name);

}
