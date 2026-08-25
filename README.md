# SegmentSearch — 基因片段检索分析系统

基于 Java Servlet + JSP + MySQL 的 Web 应用，用于人染色体基因片段的录入、检索、变异分析与第三方资料库联动。前端采用 Bootstrap 3.3.7 管理后台模板（SB Admin 2），支持案例数据管理、本地/在线变异分析、PubMed 文献上传及实验室人员权限管理。项目使用 Maven 构建。

## 目录

- [项目背景](#项目背景)
- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [许可证](#许可证)

## 项目背景

本项目面向医学遗传学实验室，解决以下问题：

- 受检者（患者）案例信息的结构化录入与查询
- 基因片段（染色体变异位点）的检索与分析
- 与第三方遗传学数据库（NCBI BLAST、Decipher、DOAF）的联动
- 实验室人员权限管理与数据浏览

系统采用经典的三层架构（Servlet → Service → DAO），数据库连接优先通过 `db.properties` 配置文件（DBCP2 连接池）获取，未配置时回退到容器 JNDI 数据源。

## 功能特性

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

## 许可证

MIT License — 详见 [WebContent/LICENSE](WebContent/LICENSE)
