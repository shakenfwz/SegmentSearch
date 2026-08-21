package dao.impl;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import dao.UserDao;
import domain.User;
import utils.JdbcUtils;

public class UserDaoImpl implements UserDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.impl.UserDao#add(domain.User)
	 */
	public void add(User user) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "insert into user(id,username,passwd,phone,cellphone,address,email) values(?,?,?,?,?,?,?)";
			Object params[] = { user.getId(), user.getUsername(), user.getPassword(), user.getPhone(),
					user.getCellphone(), user.getAddress(), user.getEmail() };
			runner.update(sql, params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.impl.UserDao#find(java.lang.String)
	 */
	public User find(String id) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "select * from user where id=?";
			User foundUser; 
			foundUser = runner.query(sql, new BeanHandler(User.class),id);
			return foundUser;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.impl.UserDao#find(java.lang.String, java.lang.String)
	 */
	public User find(String username, String password) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "select * from user where username=? and passwd=?";
			Object params[] = { username, password };
			return (User) runner.query(sql, new BeanHandler(User.class),params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
