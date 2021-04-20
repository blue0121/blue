package blue.base.internal.spring.convert;

/**
 * @author Jin Zheng
 * @since 2020-04-12
 */
public interface DateTimeConverter {
	String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	String DATE_FORMAT = "yyyy-MM-dd";
	String TIME_FORMAT = "HH:mm:ss";

	String BLANK = " ";

	int DATE_LENGTH = 10;
	int TIME_LENGTH = 8;
}
