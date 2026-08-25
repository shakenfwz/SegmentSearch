package dao;

import domain.User;

public interface UserDao {

	void add(User user);

	User find(String id);

	User find(String username, String password);

	/** 按用户名查询用户（密码校验由 service 层用 PasswordUtils 完成）。 */
	User findByName(String username);

	/** 更新用户密码（用于明文密码透明迁移为哈希）。 */
	void updatePassword(User user);
}