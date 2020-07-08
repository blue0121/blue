package blue.core.http;

import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @date 2020-07-08
 */
public interface Response<T>
{
	String CONCAT = ",";

	/**
	 * Http status code
	 * @return
	 */
	int getCode();

	/**
	 * Http headers
	 * @return
	 */
	Map<String, String> getHeaders();

	/**
	 * Http header map
	 * @return
	 */
	Map<String, List<String>> getMap();

	/**
	 * Http body
	 * @return
	 */
	T getBody();

}
