package test.test.jdbc.dao;

import blue.jdbc.core.Expression;
import blue.jdbc.core.OrderBy;
import blue.jdbc.core.QueryDao;
import org.springframework.stereotype.Repository;
import test.test.jdbc.model.UserGroup;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-24
 */
@Repository
public class UserGroupDao extends QueryDao<UserGroup, UserGroup>
{
	public UserGroupDao()
	{
	}

	@Override
	protected StringBuilder selectCount()
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from \"user\" a");
		return sql;
	}

	@Override
	protected StringBuilder select()
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select a.*, g.\"name\" group_name from \"user\" a" +
				" left join \"group\" g on a.\"group_id\"=g.\"id\"");
		return sql;
	}

	@Override
	protected void query(Expression exp, UserGroup param)
	{
		if (param.getGroupId() != null)
		{
			exp.add("a.\"group_id\"=:groupId");
		}
		if (param.getName() != null && !param.getName().isEmpty())
		{
			exp.add("a.\"name\" like :name");
			param.setName("%" + param.getName() + "%");
		}
	}

	@Override
	protected void orderBy(OrderBy order, UserGroup param)
	{
		order.add("a.\"id\" desc");
	}
}
