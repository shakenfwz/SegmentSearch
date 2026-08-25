# SegmentSearch 部署操作文档

## 一、环境要求

| 组件 | 最低版本 | 推荐版本 | 说明 |
|------|---------|---------|------|
| JDK | 1.8 | 1.8 / 11 / 17 | 编译目标 Java 8，兼容高版本 |
| Maven | 3.6+ | 3.9+ | 构建 WAR 包 |
| MySQL | 5.7 | 8.0+ | 需启用 `utf8mb4` 字符集 |
| Tomcat | 9.0 | 9.0.121+ | 支持 Servlet 3.0+ |

## 二、构建 WAR 包

```bash
# 在项目根目录执行
mvn clean package
```

构建成功后，WAR 包位于 `target/SegmentSearch.war`。

> 构建过程会自动执行单元测试（`mvn test`），全部通过后才会打包。

## 三、数据库初始化

### 3.1 创建数据库

```sql
CREATE DATABASE segmentdb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### 3.2 导入表结构

```bash
mysql -u root -p segmentdb < database/db.sql
```

`db.sql` 包含以下表及初始数据：

| 表名 | 说明 |
|------|------|
| `user` | 系统用户（含默认账号 admin/admin） |
| `patientInfo` | 受检者基本信息 |
| `sampleinfo` | 样本信息 |
| `variationSites` | 变异位点（染色体区间） |
| `dict_category` | 临床字典-类别 |
| `dict_detail` | 临床字典-明细 |
| `report` | 检测报告 |
| `pubmed_doc` | PubMed 文档 |

### 3.3 修改默认密码（必须）

默认账号 `admin/admin` 为明文，首次登录后系统会自动迁移为 PBKDF2 哈希。但**生产环境必须立即修改**：

```sql
-- 方式一：直接更新为 PBKDF2 哈希（需先用工具生成）
UPDATE user SET passwd='pbkdf2$65536$<saltHex>$<hashHex>' WHERE username='admin';

-- 方式二：登录后自动迁移（推荐，首次用 admin/admin 登录即可）
```

## 四、配置数据库连接

### 4.1 创建 db.properties（推荐方式）

```bash
# 复制模板
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

编辑 `src/main/resources/db.properties`：

```properties
# JDBC 驱动类（MySQL 8.x 用 com.mysql.cj.jdbc.Driver）
db.driver=com.mysql.cj.jdbc.Driver

# 数据库连接 URL
db.url=jdbc:mysql://<host>:3306/segmentdb?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true

# 数据库账号密码
db.username=<username>
db.password=<password>

# DBCP2 连接池参数
db.maxTotal=100
db.maxIdle=30
db.maxWaitMillis=10000
```

> **注意**：`db.properties` 含数据库密码，已在 `.gitignore` 中排除，**切勿提交到版本库**。

### 4.2 使用 JNDI 数据源（备选方式）

若未提供 `db.properties`，系统回退到容器 JNDI 数据源 `jdbc/segmentdb`。需在 Tomcat 的 `context.xml` 中配置：

**Tomcat 全局配置**（`$CATALINA_HOME/conf/context.xml`）或 **应用级配置**（WAR 包内 `META-INF/context.xml`）：

```xml
<Resource name="jdbc/segmentdb" auth="Container" type="javax.sql.DataSource"
    maxTotal="100" maxIdle="30" maxWaitMillis="10000"
    username="<username>" password="<password>"
    driverClassName="com.mysql.cj.jdbc.Driver"
    url="jdbc:mysql://<host>:3306/segmentdb?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true" />
```

> **注意**：
> - MySQL 8.x 驱动类为 `com.mysql.cj.jdbc.Driver`（非 `com.mysql.jdbc.Driver`）
> - DBCP2 使用 `maxTotal`/`maxWaitMillis`（非 `maxActive`/`maxWait`）
> - URL 中 `allowPublicKeyRetrieval=true` 仅在开发环境使用，生产环境应配置 SSL

### 4.3 配置优先级

```
db.properties（classpath）  >  JNDI 数据源（jdbc/segmentdb）
```

## 五、部署到 Tomcat

### 5.1 部署 WAR 包

```bash
# 将 WAR 包复制到 Tomcat webapps 目录
cp target/SegmentSearch.war $CATALINA_HOME/webapps/
```

### 5.2 启动 Tomcat

```bash
# Linux
$CATALINA_HOME/bin/startup.sh

# Windows
$CATALINA_HOME\bin\startup.bat
```

### 5.3 验证部署

```bash
# 检查应用是否启动
curl -I http://localhost:8080/SegmentSearch/login.jsp
# 期望返回 HTTP 200

# 检查安全响应头
curl -I http://localhost:8080/SegmentSearch/login.jsp | grep -E "X-Frame-Options|Content-Security-Policy|X-Content-Type-Options"
```

### 5.4 访问地址

| 页面 | URL |
|------|-----|
| 登录页（JSP） | `http://<host>:8080/SegmentSearch/login.jsp` |
| 登录页（HTML） | `http://<host>:8080/SegmentSearch/pages/login.html` |
| 首页 | `http://<host>:8080/SegmentSearch/index.html` |
| 验证码 | `http://<host>:8080/SegmentSearch/Captcha` |

## 六、安全配置（生产环境必须）

### 6.1 Cookie HttpOnly

在 Tomcat `conf/context.xml` 中添加：

```xml
<CookieProcessor className="org.apache.tomcat.util.http.Rfc6265CookieProcessor"
    useHttpOnly="true" />
```

### 6.2 HTTPS（TLS）

生产环境**必须**启用 HTTPS，防止密码明文传输。配置 Tomcat `server.xml`：

```xml
<Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
    SSLEnabled="true"
    keystoreFile="conf/keystore.jks"
    keystorePass="changeit"
    scheme="https"
    secure="true">
    <SSLHostConfig>
        <Certificate certificateKeystoreFile="conf/keystore.jks"
            type="RSA" />
    </SSLHostConfig>
</Connector>
```

### 6.3 安全响应头

应用已内置 `SecurityHeaderFilter`，自动输出以下响应头：

```
X-Content-Type-Options: nosniff
X-Frame-Options: SAMEORIGIN
Referrer-Policy: same-origin
Content-Security-Policy: default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:; frame-ancestors 'self'
```

### 6.4 CSRF 防护

应用已内置 `CsrfFilter`（Synchronizer Token 模式）：
- GET 请求自动生成 Token，通过 `X-CSRF-Token` 响应头输出
- POST 请求校验表单参数 `_csrf` 或请求头 `X-CSRF-Token`
- 登录/验证码等匿名接口在白名单中放行

### 6.5 登录安全

| 机制 | 说明 |
|------|------|
| 服务端验证码 | `CaptchaServlet` 生成图片，码值存 Session，一次性校验 |
| 密码哈希 | PBKDF2WithHmacSHA256，65536 次迭代，16 字节随机盐 |
| 登录锁定 | 按用户名+IP，连续 5 次失败锁定 15 分钟 |
| 会话固定防护 | 登录成功后 invalidate 旧 Session，生成新 JSESSIONID |
| 审计日志 | 登录成功/失败、字典变更、报告保存、文档上传均写审计日志 |

## 七、日志配置

### 7.1 log4j2 配置

应用使用 log4j2 2.17.2（已修复 Log4Shell CVE），配置文件位于 `src/main/resources/log4j2.xml`：

- **控制台输出**：所有日志
- **文件输出**：`logs/segmentsearch.log`，按天滚动，单文件 10MB，保留 10 个
- **审计日志**：独立 logger `audit`，记录关键操作

### 7.2 日志位置

```
$CATALINA_HOME/logs/segmentsearch.log          # 当前日志
$CATALINA_HOME/logs/segmentsearch-2026-08-22-1.log.gz  # 历史日志（压缩）
```

## 八、回滚操作

```bash
# 1. 停止 Tomcat
$CATALINA_HOME/bin/shutdown.sh

# 2. 移除新 WAR
rm $CATALINA_HOME/webapps/SegmentSearch.war
rm -rf $CATALINA_HOME/webapps/SegmentSearch/

# 3. 部署旧版本 WAR
cp SegmentSearch-<old-version>.war $CATALINA_HOME/webapps/

# 4. 启动 Tomcat
$CATALINA_HOME/bin/startup.sh
```

## 九、常见问题

### Q1: 登录时提示"数据库连接失败"

**原因**：`db.properties` 未配置或配置错误。

**排查**：
```bash
# 检查 db.properties 是否存在
ls src/main/resources/db.properties

# 检查数据库是否可达
mysql -h <host> -P 3306 -u <username> -p -e "SELECT 1"

# 检查 URL 参数（MySQL 8.x 必须包含 serverTimezone）
```

### Q2: 静态资源（CSS/JS）无法加载，MIME 类型错误

**原因**：`CharactorEncodingFilter` 中 `response.setContentType("text/html")` 覆盖了静态资源的 MIME 类型。

**解决**：已修复，仅保留 `setCharacterEncoding("UTF-8")`，不再强制设置 Content-Type。

### Q3: 验证码图片不显示

**排查**：
```bash
# 直接访问验证码接口
curl -I http://localhost:8080/SegmentSearch/Captcha
# 期望返回 Content-Type: image/jpeg
```

### Q4: 上传 PDF 失败，提示"文件内容不是有效的 PDF"

**原因**：文件头魔数校验失败。系统要求 PDF 文件以 `%PDF` 开头。

**排查**：
```bash
# 检查文件头
head -c 5 file.pdf | xxd
# 期望输出: 25 50 44 46 2d  (%PDF-)
```

### Q5: 登录提示"登录失败次数过多，已锁定"

**说明**：同一用户名+IP 连续 5 次密码错误后锁定 15 分钟，属正常安全机制。

**解决**：等待 15 分钟后重试，或联系管理员重置。

## 十、部署检查清单

- [ ] JDK 版本 ≥ 1.8
- [ ] Maven 构建成功（`mvn clean package` 无错误）
- [ ] 数据库已创建（`segmentdb`，`utf8mb4` 字符集）
- [ ] 表结构已导入（`database/db.sql`）
- [ ] `db.properties` 已配置（或 JNDI 数据源已配置）
- [ ] 默认密码 `admin/admin` 已修改
- [ ] Tomcat 已部署 WAR 包
- [ ] 安全响应头已验证（`curl -I` 检查）
- [ ] CSRF 防护已验证（无 Token 的 POST 返回 403）
- [ ] 生产环境已启用 HTTPS
- [ ] Cookie HttpOnly 已配置
- [ ] 日志目录可写（`logs/`）
- [ ] 上传目录可写（`/upload/`）
