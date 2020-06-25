package test.jdbc.sql;

import blue.internal.jdbc.dialect.MySQLDialect;
import blue.internal.jdbc.parser.Parser;
import blue.internal.jdbc.sql.SqlHandlerFactory;
import blue.jdbc.core.Dialect;
import test.jdbc.model.Group;
import test.jdbc.model.User;
import test.jdbc.model.UserGroup;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-05
 */
public abstract class SqlHandlerTest
{
	protected Dialect dialect;
	protected SqlHandlerFactory factory;

	public SqlHandlerTest()
	{
		this.dialect = new MySQLDialect();
		this.factory = SqlHandlerFactory.init(dialect);
		Parser.getInstance().parse(User.class);
		Parser.getInstance().parse(Group.class);
		Parser.getInstance().parse(UserGroup.class);
		factory.init();
	}



}
