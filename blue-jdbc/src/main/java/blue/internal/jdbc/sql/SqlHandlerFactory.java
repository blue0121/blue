package blue.internal.jdbc.sql;

import blue.internal.jdbc.parser.ParserCache;
import blue.jdbc.core.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2019-11-24
 */
public class SqlHandlerFactory
{
	private static Logger logger = LoggerFactory.getLogger(SqlHandlerFactory.class);
	private static volatile SqlHandlerFactory factory;
	private volatile boolean isInit = false;

	private Map<SqlType, SqlHandler> handlerMap = new HashMap<>();

	private SqlHandlerFactory(Dialect dialect)
	{
		handlerMap.put(SqlType.ESCAPE, new EscapeSqlHandler(dialect));
		handlerMap.put(SqlType.DEFAULT, new DefaultSqlHandler());
		handlerMap.put(SqlType.INSERT, new InsertSqlHandler());
		handlerMap.put(SqlType.UPDATE, new UpdateSqlHandler());
		handlerMap.put(SqlType.DELETE, new DeleteSqlHandler());
		handlerMap.put(SqlType.INC, new IncSqlHandler());
		handlerMap.put(SqlType.PARTIAL_UPDATE, new PartialUpdateSqlHandler());
		handlerMap.put(SqlType.PARTIAL_INSERT, new PartialInsertSqlHandler());
		handlerMap.put(SqlType.EXIST, new ExistSqlHandler());
		handlerMap.put(SqlType.GET_FIELD, new GetFieldSqlHandler());
		handlerMap.put(SqlType.GET, new GetSqlHandler());
		handlerMap.put(SqlType.COUNT, new CountSqlHandler());
	}

	public static SqlHandlerFactory init(Dialect dialect)
	{
		if (factory == null)
		{
			synchronized (SqlHandlerFactory.class)
			{
				if (factory == null)
				{
					factory = new SqlHandlerFactory(dialect);
				}
			}
		}
		return factory;
	}

	public static SqlHandlerFactory getInstance()
	{
		return factory;
	}

	public void init()
	{
		this.init(true);
	}

	public void init(boolean escape)
	{
		if (!isInit)
		{
			synchronized (this)
			{
				if (!isInit)
				{
					Set<Class<?>> clazzSet = ParserCache.getInstance().allClazz();
					for (Class<?> clazz : clazzSet)
					{
						SqlParam param = new SqlParam(clazz);
						if (escape)
						{
							this.handle(SqlType.ESCAPE, param);
						}
						this.handle(SqlType.DEFAULT, param);
					}
					isInit = true;
				}
			}
		}
	}

	public String handleTarget(SqlType type, Object target, Object...args)
	{
		SqlParam param = new SqlParam(target, args);
		return this.handle(type, param);
	}

	public String handleMap(SqlType type, Class<?> clazz, Map<String, Object> map, Object...args)
	{
		SqlParam param = new SqlParam(clazz, map, args);
		return this.handle(type, param);
	}

	public String handleField(SqlType type, Class<?> clazz, String field, Map<String, Object> map)
	{
		SqlParam param = new SqlParam(clazz, field, map);
		return this.handle(type, param);
	}

	private String handle(SqlType type, SqlParam param)
	{
		SqlHandler handler = handlerMap.get(type);
		if (handler == null)
		{
			logger.error("SqlHandler is null, type: {}", type);
			return null;
		}
		try
		{
			logger.debug("handle factory, type: {}, class: {}", type, param.getClazz());
			return handler.sql(param);
		}
		catch (Exception e)
		{
			logger.error("Error, ", e);
			return null;
		}
	}

	public Map<SqlType, SqlHandler> getHandlerMap()
	{
		return handlerMap;
	}
}
