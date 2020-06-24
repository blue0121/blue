package blue.internal.jdbc.config;

import blue.core.common.SimpleBeanDefinitionParser;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.w3c.dom.Element;

/**
 * TransactionManager解析
 *
 * @author Jin Zheng
 * @date 2018-10-31
 */
public class TransactionManagerParser extends SimpleBeanDefinitionParser
{
	public TransactionManagerParser()
	{
		this.clazz = DataSourceTransactionManager.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parseRef(element, builder, "dataSource", "ref-data-source");
	}

}
