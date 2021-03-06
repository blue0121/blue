package test.core.convert;

import blue.core.convert.ConvertService;
import blue.core.convert.ConvertServiceFactory;
import blue.core.dict.DictParser;
import blue.core.dict.State;
import blue.core.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author Jin Zheng
 * @since 2020-04-12
 */
public class ConvertServiceTest
{
	private ConvertService service = ConvertServiceFactory.getConvertService();
	private DictParser dictParser = DictParser.getInstance();

	public ConvertServiceTest()
	{
		dictParser.parse(State.class);
	}

	@Test
	public void testDate()
	{
		String strDate = "2019-01-01 20:00:00";
		Date date = service.convert(strDate, Date.class);
		Assertions.assertEquals(strDate, DateUtil.formatDateTime(date));

		strDate = "2020-01-10";
		date = service.convert(strDate, Date.class);
		Assertions.assertEquals(strDate + " 00:00:00", DateUtil.formatDateTime(date));
	}

	@Test
	public void testLocalDateTime()
	{
		String strDate = "2019-01-01 20:00:00";
		LocalDateTime dateTime = service.convert(strDate, LocalDateTime.class);
		Assertions.assertEquals(strDate, dateTime.format(DateUtil.DEFAULT_FORMATTER));

		LocalDate date = service.convert(strDate, LocalDate.class);
		Assertions.assertEquals(strDate.substring(0, 10), date.format(DateUtil.DATE_FORMATTER));

		LocalTime time = service.convert(strDate, LocalTime.class);
		Assertions.assertEquals(strDate.substring(strDate.length() - 8), time.format(DateUtil.TIME_FORMATTER));
	}

	@Test
	public void testSqlDate()
	{
		String strDate = "2019-01-01 20:00:00";
		java.sql.Date date = service.convert(strDate, java.sql.Date.class);
		Assertions.assertEquals(strDate.substring(0, 10), DateUtil.formatDate(date));

		Time time = service.convert(strDate, Time.class);
		Assertions.assertEquals(strDate.substring(strDate.length() - 8), DateUtil.formatTime(time));

		Timestamp timestamp = service.convert(strDate, Timestamp.class);
		Assertions.assertEquals(strDate, DateUtil.formatDateTime(timestamp));
	}

	@Test
	public void testDictionary()
	{
		State state = service.convert("0", State.class);
		Assertions.assertEquals(State.NORMAL, state);

		state = service.convert("NORMAL", State.class);
		Assertions.assertEquals(State.NORMAL, state);

		state = service.convert(0, State.class);
		Assertions.assertEquals(State.NORMAL, state);
	}

}
