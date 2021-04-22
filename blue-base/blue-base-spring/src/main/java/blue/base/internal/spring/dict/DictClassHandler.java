package blue.base.internal.spring.dict;

import blue.base.core.dict.DictParser;
import blue.base.core.dict.Dictionary;
import blue.base.core.path.ClassHandler;

/**
 * 扫描字典配置
 *
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public class DictClassHandler implements ClassHandler {
	public DictClassHandler() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(Class<?> clazz) {
		if (Dictionary.class.isAssignableFrom(clazz)) {
			Class<Dictionary> dictClazz = (Class<Dictionary>) clazz;
			DictParser.getInstance().parse(dictClazz);
		}

	}

}
