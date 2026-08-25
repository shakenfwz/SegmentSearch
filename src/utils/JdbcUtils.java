package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * 数据源工具类，按以下顺序获取数据源：
 * <ol>
 * <li>优先读取 classpath 下的 db.properties 配置文件（推荐方式，参见
 * src/main/resources/db.properties.example），使用 DBCP2 连接池；</li>
 * <li>未找到配置文件时，回退到容器 JNDI 数据源 jdbc/segmentdb（参见
 * WebContent/META-INF/context.xml）。</li>
 * </ol>
 */
public class JdbcUtils {

	private static final String CONFIG_FILE = "db.properties";
	private static final String JNDI_NAME = "jdbc/segmentdb";

	private static DataSource ds;

	static {
		InputStream in = null;
		try {
			in = JdbcUtils.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
			if (in != null) {
				Properties props = new Properties();
				props.load(in);
				String url = props.getProperty("db.url");
				if (url == null || url.trim().length() == 0) {
					throw new IllegalStateException(CONFIG_FILE + " 缺少 db.url 配置");
				}
				BasicDataSource bds = new BasicDataSource();
				bds.setDriverClassName(props.getProperty("db.driver", "com.mysql.jdbc.Driver"));
				bds.setUrl(url);
				bds.setUsername(props.getProperty("db.username"));
				bds.setPassword(props.getProperty("db.password"));
				bds.setMaxTotal(Integer.parseInt(props.getProperty("db.maxTotal", "100")));
				bds.setMaxIdle(Integer.parseInt(props.getProperty("db.maxIdle", "30")));
				bds.setMaxWaitMillis(Integer.parseInt(props.getProperty("db.maxWaitMillis", "10000")));
				ds = bds;
			} else {
				// 未提供 db.properties，回退到容器 JNDI 数据源
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				ds = (DataSource) envCtx.lookup(JNDI_NAME);
			}
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	public static DataSource getDataSource() {
		return ds;
	}

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
