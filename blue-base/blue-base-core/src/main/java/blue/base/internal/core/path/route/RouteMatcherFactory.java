package blue.base.internal.core.path.route;

import blue.base.core.path.RouteMatcher;
import blue.base.core.path.RouteType;
import blue.base.core.util.AssertUtil;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public class RouteMatcherFactory {
	private RouteMatcherFactory() {
	}

	public static RouteMatcher create(RouteType type) {
		AssertUtil.notNull(type, "RouteType");
		switch (type) {
			case HTTP:
				return new HttpRouteMatcher();
			default:
				throw new UnsupportedOperationException("Unsupported RouteType: " + type);
		}
	}

}
