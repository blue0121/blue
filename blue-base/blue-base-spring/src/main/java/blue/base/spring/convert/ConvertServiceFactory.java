package blue.base.spring.convert;

import blue.base.internal.spring.convert.DefaultConvertService;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
public class ConvertServiceFactory {
	private static volatile ConvertService convertService;

	private ConvertServiceFactory() {
	}

	public static ConvertService getConvertService() {
		if (convertService == null) {
			synchronized (ConvertServiceFactory.class) {
				if (convertService == null) {
					convertService = new DefaultConvertService();
				}
			}
		}
		return convertService;
	}

}
