package blue.internal.http.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.filter.TokenWebSocketFilter;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-02
 */
public class TokenWebSocketFilterParser extends SimpleBeanDefinitionParser
{
	public TokenWebSocketFilterParser()
	{
		this.clazz = TokenWebSocketFilter.class;
	}

}
