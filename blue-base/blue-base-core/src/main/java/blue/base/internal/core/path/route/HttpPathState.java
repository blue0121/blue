package blue.base.internal.core.path.route;

/**
 * @author Jin Zheng
 * @since 2021-06-05
 */
public enum HttpPathState {

	START, SLASH, LETTER,

	BRACE_START, BRACE_VARIABLE, BRACE_END,

	REGEX_START, REGEX_LETTER,

}
