package blue.internal.http.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * HttpConfig解析
 *
 * @author Jin Zheng
 * @date 2018-10-25
 */
public class HttpConfigParser extends FilterConfigDefinitionParser
{
	public HttpConfigParser()
	{
		this.clazz = HttpConfig.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		Map<Object, Object> pathMap = this.getBeanMap(element, parserContext, builder, HttpNamespaceHandler.NS, "path-map");
		builder.addPropertyValue("pathMap", pathMap);

		this.parseFilterConfig(element, builder.getRawBeanDefinition());

		builder.addPropertyValue("path", element.getAttribute("path"));
		builder.addPropertyValue("ftlRoot", element.getAttribute("ftl-root"));
		builder.addPropertyValue("maxUploadSize", element.getAttribute("max-upload-size"));
	}

}
