-- ============================================================
-- 基因拷贝数变异管理系统（SegmentSearch）建表脚本
-- 依据《基因拷贝数变异管理系统及关联分析工具开发》表3/8/9/10 设计
-- ============================================================

-- 系统用户表（论文表3：人员编号、姓名、密码、功能权限、组别）
create table user (
  id varchar(20) not null comment '人员编号',
  username varchar(20) not null comment '姓名',
  passwd varchar(255) not null comment '密码（PBKDF2 哈希，格式 pbkdf2$iterations$saltHex$hashHex）',
  phone varchar(20) comment '联系电话',
  cellphone varchar(11) comment '手机',
  address varchar(100) comment '通讯地址',
  email varchar(50) comment '电子邮箱',
  primary key (id)
);
insert into user(id, username, passwd, phone, cellphone, address, email)
values ('1', 'admin', 'admin', '', '', '', '');

-- 受检者基本信息（论文表5）
create table patientInfo (ID mediumint not null auto_increment comment '主键',patientID int comment '病例编号',pName varchar(10) comment '受检者姓名',
Sex char(2) comment '受检者性别',age int comment '受检者年龄',ptype tinyint comment '受检者类型',
ClinicalFindings varchar(200) comment '症状表现',ClinicalData varchar(200) comment '临床资料',PatientRegion varchar(50) comment '受检者地区',
FamilyID int comment '受检者家系编号',MotherID int comment '受检者母亲编号',MotherName varchar(10) comment '受检者母亲姓名',MatherAge int comment '受检者母亲年龄',
FatherID int comment '受检者父亲编号',FatherName varchar(10) comment '受检者父亲姓名',FatherAge int comment '受检者父亲年龄',
Birthday date comment '出生年月',Weight decimal(5,2) comment '体重(kg)',Height decimal(5,2) comment '身高(cm)',
IDNumber varchar(20) comment '身份证号',Phone varchar(20) comment '联系电话',Mobilephone varchar(11) comment '手机',
Address varchar(100) comment '通讯地址',PrenWeeks int comment '孕周',PrenHistory varchar(200) comment '不良妊娠史',
primary key (ID)) ;

create table sampleinfo(sampleID mediumint not null auto_increment comment '样品条码,主键',sampleName varchar(20) comment '样本名称',
testMethod varchar(20) comment '检测方法',sendDoctor varchar(20) comment '送检医生',sendDate datetime comment '送检日期',
receiveDate datetime comment '接收日期',sampleType varchar(10) comment '样本类型',collectedDate datetime comment '采样日期',
sampleDosage int comment '样本剂量',patientID int,primary key (sampleID));

create table testPic (picID mediumint not null auto_increment comment '主键',picType tinyint comment '图片类型（FISH图1，核型图2）',
delFlag tinyint comment '删除标志',upFlag tinyint comment '上传标志',sampleID int comment '样品条码,',primary key (picID));

-- 基因片段数据库（论文表9：含开始/结束位置，本地区间重叠检索依据）
create table variationSites(varID mediumint not null auto_increment comment '主键',	sampleID int  comment '样品条码,主键',
Chr char(2), Start bigint comment '开始位置', Stop bigint comment '结束位置',
Length int comment '长度', sampleValue int comment '倍数', Conf float comment '可信度',
vComment varchar(100) comment '备注',CNVIndex int,Cytobands varchar(200) comment '条带',
MarkersNo int comment '探针数',Genes varchar(6000) comment '基因', primary key(varID));
create index idx_var_overlap on variationSites(Chr, Start, Stop);

-- 临床详情字典（论文表8：detailID 为 0 表示类别名称）
create table dict (
  categoryID int not null comment '类别ID',
  detailID int not null default 0 comment '详情ID，0为类别名称',
  detailContent varchar(500) not null comment '详情内容',
  delFlag tinyint not null default 0 comment '作废标志 0:正常 1:作废',
  updateTime datetime comment '更新时间',
  primary key (categoryID, detailID)
);
insert into dict(categoryID, detailID, detailContent) values
(1, 0, '神经系统'),      (1, 1, '癫痫'), (1, 2, '脑发育异常'), (1, 3, '智力障碍'),
(2, 0, '循环呼吸系统'),  (2, 1, '先天性心脏病'), (2, 2, '室间隔缺损'), (2, 3, '法洛四联症'),
(3, 0, '骨骼与肌肉系统'),(3, 1, '手足裂畸形'),
(4, 0, '泌尿生殖系统'),
(5, 0, '消化系统'),
(6, 0, '五官系统'),
(7, 0, '其他');

-- 报告内容（论文表10：检测结果及结果描述，精简实现）
create table report (
  ID mediumint not null auto_increment comment '主键',
  sampleID int not null comment '样本编号',
  pathoClass varchar(30) comment '致病分类（正常/良性/致病性/临床意义不明确）',
  varType varchar(10) comment '重复/缺失',
  cytobands varchar(100) comment '染色体区域',
  coordStart bigint comment '染色体坐标-开始',
  coordStop bigint comment '染色体坐标-结束',
  varSize double comment '变异大小(Mb)',
  genes varchar(2000) comment '变异基因片段',
  resultDesc varchar(8000) comment '结果描述',
  reporter varchar(20) comment '报告人员',
  reviewer varchar(20) comment '审核人员',
  reportTime datetime comment '报告时间',
  reviewTime datetime comment '审核时间',
  primary key (ID)
);
create index idx_report_sample on report(sampleID);

-- PubMed 文档存储（论文4.4：文档 URL 及 PDF 文件，与临床检测申请单号关联）
create table pubmedDoc (
  ID mediumint not null auto_increment comment '主键',
  applyNo varchar(30) not null comment '临床检测申请单号',
  docUrl varchar(500) comment 'PubMed 文档 URL',
  filePath varchar(500) comment 'PDF 文件存储路径',
  operator varchar(20) comment '操作人员',
  opTime datetime comment '操作时间',
  primary key (ID)
);
create index idx_pubmedDoc_applyNo on pubmedDoc(applyNo);
