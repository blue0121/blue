package blue.internal.http.parser.parameter;

import blue.internal.http.annotation.RequestParamConfig;

import java.lang.annotation.Annotation;

/**
 * 参数解析器
 *
 * @author Jin Zheng
 * @since 2021-01-26
 */
public interface ParamParser<T extends Annotation>
{

	/**
	 * 解析方法参数
	 *
	 * @param config
	 * @param annotation
	 */
	void parse(RequestParamConfig config, T annotation);

}
