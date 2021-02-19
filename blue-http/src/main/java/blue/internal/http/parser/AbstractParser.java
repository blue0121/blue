package blue.internal.http.parser;

import blue.core.file.FilePath;
import blue.core.reflect.BeanMethod;
import blue.core.reflect.JavaBean;
import blue.internal.http.parser.parameter.ParamParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-19
 */
public abstract class AbstractParser implements Parser
{
	private static Logger logger = LoggerFactory.getLogger(AbstractParser.class);
	protected static final Set<Class<?>> PARSER_CLASS_SET = new HashSet<>();
	protected static final ParamParserFactory PARAM_PARSER_FACTORY = ParamParserFactory.getInstance();

	protected final JavaBean bean;
	protected FilePath rootPath;

	public AbstractParser(JavaBean bean)
	{
		this.bean = bean;
	}

	@Override
	public void parse()
	{
		if (PARSER_CLASS_SET.contains(bean.getTargetClass()))
		{
			logger.warn("{} has bean parsed", bean.getTargetClass().getName());
			return;
		}

		this.rootPath = FilePath.create(FilePath.SLASH + this.getRootPath());
		if (logger.isDebugEnabled())
		{
			logger.debug("Parse {}, root uri: {}", bean.getTargetClass().getName(), this.rootPath.getOriginalPath());
		}
		List<BeanMethod> methodList = bean.getAllMethods();
		for (var method : methodList)
		{
			if (!Modifier.isPublic(method.getModifiers()))
				continue;

			this.parseMethod(method);
		}

		PARSER_CLASS_SET.add(bean.getTargetClass());
	}

	protected abstract void parseMethod(BeanMethod method);

	protected abstract String getRootPath();

}
