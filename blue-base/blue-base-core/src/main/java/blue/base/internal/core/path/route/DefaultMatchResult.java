package blue.base.internal.core.path.route;

import blue.base.core.path.MatchResult;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public class DefaultMatchResult implements MatchResult {
	private final String route;
	private final String pattern;
	private final Object param;
	private final Map<String, String> vars;

	public DefaultMatchResult(String route, String pattern, Object param, Map<String, String> vars) {
		this.route = route;
		this.pattern = pattern;
		this.param = param;
		if (vars == null || vars.isEmpty()) {
			this.vars = Map.copyOf(vars);
		} else {
			this.vars = Map.of();
		}
	}

	@Override
	public String getRoute() {
		return route;
	}

	@Override
	public String getPattern() {
		return pattern;
	}

	@Override
	public Object getParam() {
		return param;
	}

	@Override
	public Map<String, String> getVars() {
		return vars;
	}
}
