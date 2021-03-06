package blue.base.internal.core.convert;

import blue.base.core.util.DateUtil;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @author Jin Zheng
 * @since 2020-04-23
 */
public class LocalDateToDateConverter implements Converter<TemporalAccessor, Date>
{
	public LocalDateToDateConverter()
	{
	}

	@Override
	public Date convert(TemporalAccessor source)
	{
		if (source instanceof LocalDate)
			return DateUtil.fromLocalDate((LocalDate)source);

		if (source instanceof LocalTime)
			return DateUtil.fromLocalTime((LocalTime)source);

		if (source instanceof LocalDateTime)
			return DateUtil.fromLocalDateTime((LocalDateTime)source);

		throw new IllegalArgumentException("Unsupported type: " + source.getClass().getName());
	}
}
