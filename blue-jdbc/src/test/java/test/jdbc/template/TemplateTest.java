package test.jdbc.template;

import blue.internal.jdbc.core.JdbcObjectTemplate;
import blue.internal.jdbc.dialect.MySQLDialect;
import blue.internal.jdbc.parser.Parser;
import blue.internal.jdbc.sql.SqlHandlerFactory;
import blue.jdbc.core.Dialect;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import test.jdbc.model.Group;
import test.jdbc.model.User;
import test.jdbc.model.UserGroup;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-06
 */
@ExtendWith(MockitoExtension.class)
public abstract class TemplateTest
{
	@Mock
	protected JdbcTemplate jdbcTemplate;
	@Mock
	protected NamedParameterJdbcTemplate nJdbcTemplate;
	@Spy
	protected Dialect dialect = new MySQLDialect();
	@Spy
	private SqlHandlerFactory factory;

	@InjectMocks
	protected JdbcObjectTemplate jdbcObjectTemplate;

	public TemplateTest()
	{
		factory = SqlHandlerFactory.init(dialect);
		Parser.getInstance().parse(User.class);
		Parser.getInstance().parse(Group.class);
		Parser.getInstance().parse(UserGroup.class);
		factory.init();
	}

}
