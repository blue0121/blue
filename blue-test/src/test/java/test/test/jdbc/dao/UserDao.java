package test.test.jdbc.dao;

import blue.jdbc.core.BaseDao;
import org.springframework.stereotype.Repository;
import test.test.jdbc.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-16
 */
@Repository
public class UserDao extends BaseDao<User>
{
	public UserDao()
	{
	}

}
