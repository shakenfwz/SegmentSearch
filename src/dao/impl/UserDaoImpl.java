package dao.impl;

import org.apache.commons.dbutils.QueryRunner;
import dao.UserDao;
import dao.UserParser;
import domain.User;
import utils.JdbcUtils;

public class UserDaoImpl implements UserDao {

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

	public User find(String id) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "select * from user where id=?";
			return runner.query(sql, new UserParser(), id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public User find(String username, String password) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "select * from user where username=? and passwd=?";
			Object params[] = { username, password };
			return runner.query(sql, new UserParser(), params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public User findByName(String username) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "select * from user where username=?";
			return runner.query(sql, new UserParser(), username);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updatePassword(User user) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "update user set passwd=? where id=?";
			runner.update(sql, user.getPassword(), user.getId());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
