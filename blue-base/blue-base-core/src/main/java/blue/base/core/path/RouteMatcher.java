package blue.base.core.path;

import blue.base.internal.core.path.route.RouteMatcherFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public interface RouteMatcher {
	String SLASH = "/";
	char CHAR_SLASH = SLASH.charAt(0);


	/**
	 * create RouteMatcher instance
	 *
	 * @param type
	 * @return
	 */
	static RouteMatcher create(RouteType type) {
		return RouteMatcherFactory.create(type);
	}

	/**
	 * validate route or pattern
	 *
	 * @param path
	 * @return
	 */
	boolean validate(String path);

	/**
	 * add pattern with param
	 *
	 * @param pattern
	 * @param param
	 * @return
	 */
	boolean add(String pattern, Object param);

	/**
	 * match pattern
	 *
	 * @param route
	 * @return
	 */
	MatchResult match(String route);

}
