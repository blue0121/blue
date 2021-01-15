package blue.internal.core.dict;

import blue.core.file.ClassHandler;
import blue.core.file.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析Dict
 *
 * @author zhengjin
 * @since 1.0 2018年12月03日
 */
public class DictPostProcessor implements InitializingBean
{
	private static final String BASE_PACKAGE = "blue.core4j.dict";
	private static Logger logger = LoggerFactory.getLogger(DictPostProcessor.class);

	private List<String> packageList;

	public DictPostProcessor()
	{
	}

	public void setScanPackages(List<String> packageList)
	{
		this.packageList = packageList;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (packageList == null)
			packageList = new ArrayList<>();

		if (!packageList.contains(BASE_PACKAGE))
		{
			packageList.add(BASE_PACKAGE);
		}
		logger.info("扫描包：{}", packageList);

		ClassHandler classHandler = new DictClassHandler();
		ClassScanner scanner = ClassScanner.create(classHandler);
		scanner.scan(packageList);
	}
}
