# SegmentSearch — 基因片段检索分析系统

基于 Java Servlet + JSP + MySQL 的 Web 应用，用于人染色体基因片段的录入、检索、变异分析与第三方资料库联动。前端采用 Bootstrap 3.3.7 管理后台模板（SB Admin 2），支持案例数据管理、本地/在线变异分析、PubMed 文献上传及实验室人员权限管理。项目使用 Maven 构建。

## 目录

- [项目背景](#项目背景)
- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [目录结构](#目录结构)
- [环境与安装](#环境与安装)
- [快速开始](#快速开始)
- [使用说明](#使用说明)
- [配置说明](#配置说明)
- [数据库设计](#数据库设计)
- [API 接口](#api-接口)
- [开发指南](#开发指南)
- [测试](#测试)
- [已知问题与注意事项](#已知问题与注意事项)
- [许可证](#许可证)

## 项目背景

本项目面向医学遗传学实验室，解决以下问题：

- 受检者（患者）案例信息的结构化录入与查询
- 基因片段（染色体变异位点）的检索与分析
- 与第三方遗传学数据库（NCBI BLAST、Decipher、DOAF）的联动
- 实验室人员权限管理与数据浏览

系统采用经典的三层架构（Servlet → Service → DAO），数据库连接优先通过 `db.properties` 配置文件（DBCP2 连接池）获取，未配置时回退到容器 JNDI 数据源。

## 功能特性

功能依据《基因拷贝数变异管理系统及关联分析工具开发》（论文）设计：

- 用户登录/注销，基于 Session 的登录状态管理（user 表）
- 案例数据录入：受检者基本信息、临床详情、基因检测结果文件上传
- 本地检测诊断：按染色体区间（chrom:start-end）检索本地数据库重叠变异位点（论文3.4.1 集合比对算法）
- 在线诊断管理：按 DGV → Decipher → PubMed 顺序生成查询链接（论文4.3）
- 临床详情字典维护：类别/详情的查询、新增、作废（论文表8）
- 报告录入与查询：致病分类、变异区域/坐标/基因、结果描述（论文表10）
- PubMed 文档存储：文档 URL 及 PDF 上传，与临床申请单号关联回溯（论文4.4）
- 数据浏览：病人列表、样本列表、变异位点列表
- 实验室人员登录与权限管理（数据库/表/列级权限展示）
- 第三方资料库快捷入口（NCBI BLAST、Decipher、DOAF）
- 基于区间树（Interval Tree）的基因组区间检索算法实现
- Puppeteer 自动化脚本抓取 Decipher 搜索结果并导出 PDF

## 技术栈

| 类别 | 技术 |
|---|---|
| 后端 | Java Servlet 3.0、JSP 2.5、JSTL 1.2 |
| 数据库 | MySQL 5.x（mysql-connector-java 5.1.38） |
| 数据访问 | Apache Commons DbUtils 1.7 |
| 连接池 | Apache Commons DBCP2 2.5.0（db.properties 配置）/ JNDI（回退） |
| 日志 | Apache Log4j 2.11.0 |
| 前端框架 | Bootstrap 3.3.7（SB Admin 2 模板） |
| 前端插件 | jQuery、DataTables、Flot、Morris.js、MetisMenu、Font Awesome |
| 构建/测试 | Maven 3.x（war 打包）、JUnit 4.13 |
| 运行容器 | Apache Tomcat 7.0+（Servlet 3.0） |
| 辅助工具 | Node.js + Puppeteer（Decipher PDF 抓取） |

## 目录结构

```
SegmentSearch/
├── pom.xml                      # Maven 构建脚本（依赖管理、编译、测试、war 打包）
├── .gitignore                   # 排除 target/、db.properties、*.class
├── src/                         # Java 主源码（保留原 Eclipse WTP 布局）
│   ├── controller/
│   │   └── Signin.java          # 登录 Servlet（@WebServlet /Signin）
│   ├── web/client/
│   │   ├── LoginServlet.java    # 登录 Servlet（/Login）
│   │   ├── LoginOutServlet.java # 注销 Servlet
│   │   ├── SampleServlet.java   # 样本操作 Servlet（/SampleServlet）
│   │   ├── AnalyzeServlet.java  # 本地检测诊断（区间重叠查询，论文4.2）
│   │   ├── DictServlet.java     # 临床详情字典维护（论文4.1.1）
│   │   ├── ReportServlet.java   # 报告录入与查询（论文表10）
│   │   └── PubmedServlet.java   # PubMed 文档存储与回溯（论文4.4）
│   ├── service/
│   │   ├── BusinessService.java # 业务接口
│   │   └── impl/
│   │       └── BusinessServiceImpl.java  # 注册/登录/查询业务实现
│   ├── dao/
│   │   ├── UserDao.java / PatientDao.java / SampleDao.java / VariationsitesDao.java
│   │   ├── DictDao.java / ReportDao.java / PubmedDoc.java 对应 DAO
│   │   ├── RowParser.java / UserParser.java  # 结果集行解析器
│   │   └── impl/               # 上述 DAO 的实现
│   ├── domain/                  # 领域对象
│   │   ├── User.java / Patient.java / Samples.java / Variationsites.java
│   │   ├── Dict.java / Report.java / PubmedDoc.java
│   ├── model/
│   │   └── IntervalTreeProblem.java  # 区间树算法（基因组区间检索）
│   ├── filter/
│   │   ├── CharactorEncodingFilter.java  # UTF-8 编码过滤器
│   │   ├── HtmlFilter.java               # HTML 转义过滤器（防 XSS）
│   │   ├── AuthFilter.java               # 登录拦截器（未启用）
│   │   └── LoginCheckFilter.java         # 登录检查过滤器
│   ├── exception/
│   │   └── SegmentException.java
│   ├── utils/
│   │   ├── JdbcUtils.java       # 数据源工具（db.properties 优先，JNDI 回退）
│   │   ├── DaoFactory.java      # DAO 工厂（反射创建）
│   │   ├── WebUtils.java        # UUID 生成
│   │   └── MakeCertPic.java     # 验证码图片生成
│   ├── main/resources/
│   │   └── db.properties.example  # 数据库连接配置模板
│   └── test/java/               # JUnit 单元测试
│       ├── domain/DomainObjectsTest.java
│       ├── filter/HtmlFilterTest.java
│       ├── model/IntervalTreeTest.java
│       └── utils/DaoFactoryTest.java / WebUtilsTest.java / MakeCertPicTest.java
├── WebContent/                  # Web 资源（Maven war 插件打包目录）
│   ├── WEB-INF/
│   │   ├── web.xml              # 部署描述符（过滤器 + Login/Sample Servlet）
│   │   └── lib/                 # 历史手工依赖（不再打包，由 Maven 依赖替代）
│   ├── META-INF/
│   │   ├── MANIFEST.MF
│   │   └── context.xml          # JNDI 数据源回退配置（localhost）
│   ├── js/
│   │   └── config.js            # 前端环境配置（分析接口地址等）
│   ├── pages/                   # 主界面页面（SB Admin 2 模板）
│   │   ├── index.html           # 概览/仪表盘
│   │   ├── addExample.html      # 案例录入
│   │   ├── localAnalyze.html    # 本地变异分析
│   │   ├── onlineAnalyze.html   # 在线变异分析
│   │   ├── pubmedUpload.html    # PubMed 文献上传
│   │   ├── tables.html          # 数据浏览
│   │   ├── forms.html           # 变异分析
│   │   ├── login.html           # 登录页（含验证码）
│   │   ├── register.html        # 用户注册
│   │   └── ...
│   ├── login.jsp / Home.jsp / list.jsp / analyze.jsp / addExample.jsp / message.jsp
│   ├── EmployeeLogin.html / EmployeeHome.html / showPrivileges.html / addUser.html
│   └── css/  js/  images/  fonts/  vendor/  dist/  less/  data/
├── database/
│   └── db.sql                   # 数据库建表脚本
├── DGV.js / package.json        # Node.js + Puppeteer（Decipher PDF 抓取）
├── .project / .settings/        # Eclipse 工程文件（可选）
└── README.md
```

## 环境与安装

### 环境要求

| 组件 | 版本 |
|---|---|
| JDK | 8+（编译目标 1.8） |
| Maven | 3.x |
| Apache Tomcat | 7.0+（Servlet 3.0 容器） |
| MySQL | 5.x |
| Node.js | 8.x+（仅 DGV.js 脚本需要） |

### 安装步骤

1. **初始化数据库**

   ```bash
   mysql -u root -p -e "CREATE DATABASE segmentdb CHARACTER SET utf8;"
   mysql -u root -p segmentdb < database/db.sql
   ```

2. **创建数据库配置文件**

   复制模板并按环境修改（该文件已加入 .gitignore，不会提交密码）：

   ```bash
   cp src/main/resources/db.properties.example src/main/resources/db.properties
   ```

   编辑 `db.properties` 中的 `db.url`、`db.username`、`db.password`。

3. **构建**

   ```bash
   mvn clean package
   ```

   构建产物为 `target/SegmentSearch.war`（依赖由 Maven 统一管理，不包含 WebContent/WEB-INF/lib 中的历史手工 jar）。

4. **部署**

   ```bash
   cp target/SegmentSearch.war $TOMCAT_HOME/webapps/
   ```

5. **安装 Node.js 依赖（可选，仅 DGV.js 脚本）**

   ```bash
   npm install
   ```

## 快速开始

1. 启动 MySQL 并导入 `database/db.sql`
2. 按上文步骤创建 `src/main/resources/db.properties`
3. `mvn clean package` 构建
4. 将 `target/SegmentSearch.war` 部署到 Tomcat
5. 访问：

   ```
   http://localhost:8080/SegmentSearch/
   ```

6. 系统自动重定向到 `pages/index.html`，点击登录页输入用户名和密码进入系统

## 使用说明

### 登录

- 访问 `http://localhost:8080/SegmentSearch/login.jsp`
- 使用默认账号 `admin / admin`（首次部署后请修改密码）
- 登录成功后跳转到 `pages/addExample.html`（案例录入页）
- 页面底部有「实验室人员登录」入口，跳转到 `EmployeeLogin.html`

### 案例录入

1. 登录后进入「案例管理 → 数据录入」
2. 填写受检者基本信息（样本编号、姓名、性别、年龄、出生年月等）
3. 选择临床详情（神经系统、循环呼吸系统、骨骼与肌肉系统等）
4. 上传基因检测结果文件
5. 点击「提交」

### 本地检测诊断（论文4.2）

1. 进入「变异分析 → 本地变异分析」页面（需登录）
2. 设置染色体和位置区间（如 `chr1:2-2222222`），点击「增加」加入待分析列表（支持多区间）
3. 点击「提交」，系统查询本地数据库中与输入区间有重叠的变异位点记录并展示列表
4. 无重叠记录时标注「未发现明显异常」

### 在线诊断管理（论文4.3）

1. 进入「变异分析 → 在线变异分析」页面
2. 选择染色体和位置区间
3. 选择数据源：DGV（正常人群多态库）或 Decipher（异常表型库）
4. 点击「提交」，系统按论文 URL 模板生成查询链接并打开
5. 检索顺序建议：DGV（过滤正常多态）→ Decipher（比对已知综合征）→ PubMed（文献佐证），均无结果则标注「临床意义不明确」

### 临床详情字典维护（论文4.1.1）

1. 进入「系统字典 → 临床详情字典」页面（需登录）
2. 查看/新增字典类别（如神经系统）和详情（如癫痫）
3. 作废记录仅更新作废标志（逻辑删除）

### 报告录入（论文表10）

1. 进入「输出报告 → 报告录入」页面（需登录）
2. 填写样本编号、致病分类（致病性/良性/正常/临床意义不明确）、重复/缺失类型、染色体区域与坐标、变异大小、基因、结果描述
3. 报告人员自动取当前登录用户，点击「提交」
4. 按样本编号查询历史报告

### PubMed 文档存储（论文4.4）

1. 进入「PubMed上传」页面（需登录）
2. 输入临床申请单号（必填）、PubMed 文档 URL、选择 PDF 文件（≤20MB，仅 .pdf）
3. 点击「提交」，文档与申请单号关联保存
4. 右侧「按申请单号回溯文档」可查询该申请单关联的全部文档

### 数据浏览

1. 进入「数据浏览」页面
2. 查看病人列表、样本列表、变异位点列表

### 第三方资料库

侧边栏提供以下快捷入口：
- [NCBI BLAST](http://blast.ncbi.nlm.nih.gov/)
- [Decipher](https://decipher.sanger.ac.uk/)
- [DOAF](http://doa.nubic.northwestern.edu/pages/search.php)

### Decipher PDF 抓取（DGV.js）

使用 Puppeteer 自动化抓取 Decipher 搜索结果并导出 PDF：

```bash
node DGV.js
```

脚本会打开 Decipher 搜索页面，执行鼠标框选操作，并将结果导出为 `DGV.pdf`。

## 配置说明

### 数据库连接（db.properties，推荐）

位置：`src/main/resources/db.properties`（模板见 `db.properties.example`），运行时位于 `WEB-INF/classes/`。

| 属性 | 默认值（模板） | 说明 |
|---|---|---|
| `db.driver` | `com.mysql.jdbc.Driver` | JDBC 驱动类 |
| `db.url` | `jdbc:mysql://localhost:3306/segmentdb?...` | 数据库连接 URL |
| `db.username` | `root` | 数据库用户名 |
| `db.password` | （空） | 数据库密码 |
| `db.maxTotal` | `100` | 连接池最大连接数 |
| `db.maxIdle` | `30` | 连接池最大空闲连接数 |
| `db.maxWaitMillis` | `10000` | 获取连接最大等待时间（ms） |

`JdbcUtils` 读取该文件并创建 DBCP2 连接池；**未提供该文件时自动回退到容器 JNDI 数据源** `jdbc/segmentdb`（配置见 `WebContent/META-INF/context.xml`，保持原有部署方式可用）。

### 前端环境配置（js/config.js）

位置：`WebContent/js/config.js`，各页面统一引用，部署到不同环境时只需修改本文件。

| 属性 | 说明 |
|---|---|
| `dgvUrl` | DGV 在线诊断入口模板（占位符 `{chr}/{start}/{end}`） |
| `decipherUrl` | Decipher 在线诊断入口模板 |
| `pubmedUrl` | PubMed 文献检索入口模板 |

### 过滤器配置（web.xml）

| 过滤器 | 类 | URL 模式 | 说明 |
|---|---|---|---|
| CharactorEncodingFilter | `filter.CharactorEncodingFilter` | `/*` | 设置请求/响应编码为 UTF-8 |
| HtmlFilter | `filter.HtmlFilter` | `/*` | HTML 转义，防止 XSS 注入 |

### Servlet 映射

| Servlet | 类 | URL 模式 | 说明 |
|---|---|---|---|
| Login | `web.client.LoginServlet` | `/Login`（web.xml） | 用户登录 |
| Sample | `web.client.SampleServlet` | `/Sample`（web.xml） | 样本操作 |
| Signin | `controller.Signin` | `/Signin`（@WebServlet） | 用户登录（供 pages/login.html） |
| SampleServlet | `web.client.SampleServlet` | `/SampleServlet`（@WebServlet） | 样本操作 |
| AnalyzeServlet | `web.client.AnalyzeServlet` | `/AnalyzeServlet`（@WebServlet） | 本地检测诊断（区间重叠查询） |
| DictServlet | `web.client.DictServlet` | `/DictServlet`（@WebServlet） | 临床详情字典维护 |
| ReportServlet | `web.client.ReportServlet` | `/ReportServlet`（@WebServlet） | 报告录入与查询 |
| PubmedServlet | `web.client.PubmedServlet` | `/PubmedServlet`（@WebServlet） | PubMed 文档存储与回溯 |

## 数据库设计

### 表结构

| 表名 | 说明 | 主要字段 |
|---|---|---|
| `user` | 系统用户（论文表3） | id, username, passwd, phone, cellphone, address, email |
| `patientInfo` | 受检者信息（论文表5） | ID, patientID, pName, Sex, age, ptype, ClinicalFindings, ClinicalData, PatientRegion, FamilyID, MotherID, MotherName, MatherAge, FatherID, FatherName, FatherAge |
| `sampleinfo` | 样本信息 | sampleID, sampleName, testMethod, sendDoctor, sendDate, receiveDate, sampleType, collectedDate, sampleDosage, patientID |
| `testPic` | 检测图片 | picID, picType（FISH图1/核型图2）, delFlag, upFlag, sampleID |
| `variationSites` | 变异位点（论文表9） | varID, sampleID, Chr, Start, Stop, Length, sampleValue, Conf, vComment, CNVIndex, Cytobands, MarkersNo, Genes |
| `dict` | 临床详情字典（论文表8） | categoryID, detailID（0为类别名）, detailContent, delFlag, updateTime |
| `report` | 报告内容（论文表10） | ID, sampleID, pathoClass, varType, cytobands, coordStart, coordStop, varSize, genes, resultDesc, reporter, reviewer, reportTime, reviewTime |
| `pubmedDoc` | PubMed 文档（论文4.4） | ID, applyNo, docUrl, filePath, operator, opTime |

### 建表脚本

见 [database/db.sql](database/db.sql)，包含全部 8 张表、字典初始数据和默认管理员账号（admin/admin，部署后请立即修改密码）。

## API 接口

| 接口 | 方法 | 说明 |
|---|---|---|
| `/Login` | GET/POST | 用户登录，参数：`username`, `password` |
| `/Signin` | GET/POST | 用户登录，参数：`SigninName`, `SigninPassword` |
| `/Sample?op=addExample` | GET | 添加样本（需登录） |
| `/SampleServlet?op=addExample` | GET | 添加样本（需登录） |
| `/AnalyzeServlet?op=local` | POST | 本地检测诊断，参数：`rowChrom[]/rowStart[]/rowStop[]`（多区间）或 `chrom/chromStart/chromEnd`（单区间），返回重叠变异位点列表（需登录） |
| `/DictServlet?op=list` | GET/POST | 临床详情字典列表（需登录） |
| `/DictServlet?op=addCategory` | POST | 新增字典类别，参数：`content` |
| `/DictServlet?op=addDetail` | POST | 新增字典详情，参数：`categoryId`, `content` |
| `/DictServlet?op=void` | POST | 作废字典记录，参数：`categoryId`, `detailId` |
| `/ReportServlet?op=save` | POST | 报告录入，参数：`sampleId, pathoClass, varType, cytobands, coordStart, coordStop, varSize, genes, resultDesc, reviewer`（需登录） |
| `/ReportServlet?op=list` | GET | 按样本编号查询报告，参数：`sampleId` |
| `/PubmedServlet?op=save` | POST | PubMed 文档保存（multipart），参数：`applyNo, docUrl, pdfFile`（≤20MB，仅 .pdf）（需登录） |
| `/PubmedServlet?op=list` | GET | 按申请单号回溯文档，参数：`applyNo` |

### 核心类说明

- **`JdbcUtils`**：优先读取 `db.properties` 创建 DBCP2 数据源，未找到时回退 JNDI
- **`DaoFactory`**：单例工厂，通过反射创建 DAO 实例
- **`BusinessServiceImpl`**：业务服务实现，包含用户注册、登录、查询
- **`AnalyzeUtils`**：区间重叠判定（论文3.4.1）、DGV/Decipher URL 模板（论文4.3）、HTML 转义
- **`IntervalTree`**：区间树算法实现，支持基因组区间的 stabbing query 和 intersection query

## 开发指南

### 构建与运行

```bash
# 编译 + 测试 + 打包
mvn clean package

# 仅运行测试
mvn test

# 本地快速验证编译
mvn compile
```

项目保留了原有的 Eclipse WTP 目录布局（Java 源码在 `src/`，Web 资源在 `WebContent/`），Maven 通过 `pom.xml` 中的 `sourceDirectory` 与 `warSourceDirectory` 配置适配；也可用 Eclipse + m2e-wTP 直接导入。

### 代码规范

- 源码编码：UTF-8（`project.build.sourceEncoding`）
- 编译目标：Java 8
- 依赖统一由 Maven 管理：新增依赖请修改 `pom.xml`，不要向 `WebContent/WEB-INF/lib` 手工添加 jar（该目录已排除在 war 打包之外）

## 测试

单元测试位于 `src/test/java`，使用 JUnit 4，执行命令：

```bash
mvn test
```

当前共 28 个用例，覆盖：

| 测试类 | 用例数 | 覆盖内容 |
|---|---|---|
| `model.IntervalTreeTest` | 5 | 区间树 stabbing 查询、区间求交、开区间边界语义、构建状态 |
| `utils.AnalyzeUtilsTest` | 10 | 论文3.4.1 四种区间重叠情况、DGV/Decipher URL 模板（含性染色体）、HTML 转义 |
| `filter.HtmlFilterTest` | 3 | HTML 转义（防 XSS）、null/普通文本、请求参数过滤 |
| `utils.MakeCertPicTest` | 2 | 验证码生成（4 位字符集、图片输出、最小尺寸保护） |
| `utils.WebUtilsTest` | 2 | UUID 生成格式与唯一性 |
| `utils.DaoFactoryTest` | 2 | DAO 工厂反射创建、类不存在时的异常 |
| `domain.DomainObjectsTest` | 4 | User/Patient/Samples/Variationsites 领域对象 |

涉及数据库访问的 DAO/Service 为集成测试范畴，需先配置 `db.properties` 并导入数据后方可扩展。

## 安全说明

项目已通过安全审计（详见 [security_best_practices_report.md](security_best_practices_report.md)），主要安全措施：

| 措施 | 说明 |
|---|---|
| 服务端验证码 | `CaptchaServlet` 生成图片验证码，码值存 Session（一次性），登录接口强制校验 |
| 密码哈希 | PBKDF2WithHmacSHA256（65536 轮迭代 + 随机盐），旧明文密码登录成功后自动迁移为哈希 |
| 登录失败锁定 | 按 用户名+IP 计数，连续 5 次失败锁定 15 分钟 |
| CSRF 防护 | `CsrfFilter` 全局 Synchronizer Token 模式，前端 `csrf.js` 自动填充表单隐藏字段 |
| 安全响应头 | `SecurityHeaderFilter` 输出 CSP、X-Frame-Options、X-Content-Type-Options、Referrer-Policy |
| 存储型 XSS 防护 | `UrlUtils` 校验 docUrl 仅允许 http/https 协议，拒绝 javascript:/data: |
| 文件上传安全 | PDF 魔数校验（%PDF）+ 后缀校验 + 20MB 限制，下载走 `DownloadServlet` 鉴权代理 |
| 会话固定防护 | 登录成功后 invalidate 旧 Session 并创建新 Session |
| 审计日志 | log4j2 记录登录、字典变更、报告保存、文档上传等关键操作 |
| 依赖安全 | jQuery 3.5.1（修复 CVE-2019-11358/2020-11022/11023）、log4j 2.17.2（修复 Log4Shell）、MySQL 8.0.33 |

**部署注意**：
1. 生产环境必须启用 HTTPS（TLS），Session Cookie 建议设置 `HttpOnly`（Tomcat `context.xml` 中 `cookieHttpOnly="true"`）
2. 默认 admin/admin 账号部署后请立即修改密码
3. `db.properties` 含数据库密码，已加入 `.gitignore`，勿提交到版本库
4. CSP 中 `script-src 'unsafe-inline'` 为过渡项（项目存在内联脚本），后续内联脚本外置后可移除

## 已知问题与注意事项

1. **目录布局**：为兼容原 Eclipse WTP 工程，源码仍位于 `src/`（而非 `src/main/java`），`pom.xml` 已做适配。
2. **前端演示页残留**：SB Admin 2 模板自带的部分演示页面（forms.html、morris.html 等）未接后端。
3. **Puppeteer 抓取**：`DGV.js` 需在部署机安装 Node.js 14+ 与 Chromium，DGV 站点响应较慢时需调整 timeout 参数（论文4.3.1）。
4. **旧 WEB-INF/lib**：目录中的历史 jar 及杂物（zip、crdownload）不再参与打包，可择机删除。
5. **登录失败计数为内存存储**：`LoginService` 的失败计数在 JVM 内存中，Tomcat 重启后清零；多实例部署需改用 Redis 等外部存储。

## 许可证

MIT License — 详见 [WebContent/LICENSE](WebContent/LICENSE)
