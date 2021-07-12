package blue.base.core.convert;

import blue.base.core.cache.Singleton;
import blue.base.internal.core.convert.DefaultConvertService;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
public class ConvertServiceFactory {

	private ConvertServiceFactory() {
	}

	public static ConvertService getConvertService() {
		return Singleton.get(ConvertService.class, k -> new DefaultConvertService());
	}

}
