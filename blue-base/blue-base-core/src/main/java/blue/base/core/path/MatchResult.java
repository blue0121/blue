package blue.base.core.path;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public interface MatchResult {

	/**
	 * get original route
	 *
	 * @return
	 */
	String getRoute();

	/**
	 * get pattern
	 *
	 * @return
	 */
	String getPattern();

	/**
	 * get param value
	 *
	 * @return
	 */
	Object getParam();

	/**
	 * get path variables
	 *
	 * @return
	 */
	Map<String, String> getVars();

}
