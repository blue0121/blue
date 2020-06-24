package blue.jdbc.core;

import blue.core.util.BeanUtil;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.ParserCache;
import blue.internal.jdbc.util.ObjectUtil;
import blue.jdbc.exception.JdbcException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-23
 */
public abstract class QueryDao<T, R> implements InitializingBean
{
	public static final String COUNT_TPL = "select count(*) from %s a";
	public static final String SELECT_TPL = "select a.* from %s a";

	protected JdbcObjectTemplate jdbcObjectTemplate;

	protected JdbcTemplate jdbcTemplate;
	protected NamedParameterJdbcTemplate nJdbcTemplate;
	protected Class<T> paramClazz;
	protected Class<R> targetClazz;
	protected ParserCache parserCache;

	@SuppressWarnings("unchecked")
	public QueryDao()
	{
		Type[] types = ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments();
		if (types.length == 2)
		{
			this.paramClazz = (Class<T>) types[0];
			this.targetClazz = (Class<R>) types[1];
		}
		else if (types.length == 1)
		{
			this.paramClazz = (Class<T>) types[0];
			this.targetClazz = (Class<R>) types[0];
		}
		else
		{
			throw new JdbcException("泛型类型参数错误");
		}

		this.parserCache = ParserCache.getInstance();
	}

	/**
	 * 列出在数据库中的所有对象，不分页
	 *
	 * @param param 查询参数
	 * @return 对象列表
	 */
	public List<R> list(T param)
	{
		StringBuilder sql = this.select();
		T change = this.where(sql, param);
		this.orderBy(sql, param);
		List<R> list = jdbcObjectTemplate.list(targetClazz, sql.toString(), change);
		return list;
	}

	/**
	 * 列出在数据库中的对象，必须分页
	 *
	 * @param param 查询参数，若为 null，则查询所有记录
	 * @param start 起始行号，0开始
	 * @param size 最多记录数，不能小于1
	 * @return
	 */
	public List<R> listPage(T param, int start, int size)
	{
		StringBuilder sql = this.select();
		T change = this.where(sql, param);
		this.orderBy(sql, param);
		List<R> list = jdbcObjectTemplate.list(targetClazz, sql.toString(), change, start, size);
		return list;
	}

	/**
	 * 列出在数据库中的对象，必须分页
	 *
	 * @param param 查询参数
	 * @param page 数据库分页对象
	 * @return 数据库分页对象，不创建先的分页对象
	 */
	public Page listPage(T param, Page page)
	{
		if (page == null)
			page = new Page();

		int rows = this.getRows(param);
		page.setRows(rows);

		List<R> list = this.listPage(param, page.getStartRowNo(), page.getSize());
		page.setObjectList(list);

		return page;
	}

	/**
	 * 查询对象的记录数
	 *
	 * @param param 查询参数
	 * @return 记录数
	 */
	public int getRows(T param)
	{
		StringBuilder sql = this.selectCount();
		T change = this.where(sql, param);
		return jdbcObjectTemplate.queryForInt(sql.toString(), change);
	}

	private T where(StringBuilder sql, T param)
	{
		T change = BeanUtil.createBean(paramClazz, param);
		Expression exp = Expression.and();
		this.query(exp, change);
		String strExp = exp.toString();
		if (!strExp.isEmpty())
		{
			sql.append(" where ").append(strExp);
		}
		return change;
	}

	private void orderBy(StringBuilder sql, T param)
	{
		OrderBy order = Expression.orderBy();
		this.orderBy(order, param);
		String strOrder = order.toString();
		if (!strOrder.isEmpty())
		{
			sql.append(" order by ").append(strOrder);
		}
	}

	/**
	 * <p>创建 select count(*) 查询语句，<b>子类通过覆盖该方法来自定义查询</b></p>
	 *
	 * @return select 查询语句
	 */
	protected StringBuilder selectCount()
	{
		CacheEntity cacheEntity = ObjectUtil.checkCacheEntity(targetClazz);
		StringBuilder sql = new StringBuilder(String.format(COUNT_TPL, cacheEntity.getEscapeTable()));
		return sql;
	}

	/**
	 * <p>创建 select 查询语句，<b>子类通过覆盖该方法来自定义投影</b></p>
	 *
	 * @return select 查询语句
	 */
	protected StringBuilder select()
	{
		CacheEntity cacheEntity = ObjectUtil.checkCacheEntity(targetClazz);
		StringBuilder sql = new StringBuilder(String.format(SELECT_TPL, cacheEntity.getEscapeTable()));
		return sql;
	}

	/**
	 * <p>创建查询 WHERE 子句，<b>子类通过覆盖该方法来自定义查询</b></p>
	 * <b>注：父类该方法并没作任何查询
	 *
	 * @param exp 拼接SQL语句表达式
	 * @param param 查询参数
	 */
	protected void query(Expression exp, T param)
	{
	}


	protected void orderBy(OrderBy order, T param)
	{
		if (paramClazz != targetClazz)
			return;

		CacheEntity cacheEntity = ObjectUtil.checkCacheEntity(paramClazz);
		CacheId cacheId = cacheEntity.getId();
		if (cacheId == null)
			return;

		order.add("a." + cacheId.getEscapeColumn() + " desc");
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		this.jdbcTemplate = jdbcObjectTemplate.getJdbcTemplate();
		this.nJdbcTemplate = jdbcObjectTemplate.getnJdbcTemplate();
	}

	@Autowired
	public void setJdbcObjectTemplate(JdbcObjectTemplate jdbcObjectTemplate)
	{
		if (this.jdbcObjectTemplate != null)
		{
			return;
		}
		this.jdbcObjectTemplate = jdbcObjectTemplate;
	}
}
