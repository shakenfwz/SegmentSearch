select * from variationSites;

/*drop table testPic;*/

create table patientInfo (ID mediumint not null auto_increment comment '主键',patientID int comment '病例编号',pName varchar(10) comment '受检者姓名',
Sex char(2) comment '受检者性别',age int comment '受检者年龄',ptype tinyint comment '受检者类型',
ClinicalFindings varchar(200) comment '症状表现',ClinicalData varchar(200) comment '临床资料',PatientRegion varchar(50) comment '受检者地区',
FamilyID int comment '受检者家系编号',MotherID int comment '受检者母亲编号',MotherName varchar(10) comment '受检者母亲姓名',MatherAge int comment '受检者母亲年龄',
FatherID int comment '受检者父亲编号',FatherName varchar(10) comment '受检者父亲姓名',FatherAge int comment '受检者父亲年龄',primary key (ID)) ;

create table sampleinfo(sampleID mediumint not null auto_increment comment '样品条码,主键',sampleName varchar(20) comment '样本名称',
testMethod varchar(20) comment '检测方法',sendDoctor varchar(20) comment '送检医生',sendDate datetime comment '送检日期',
receiveDate datetime comment '接收日期',sampleType varchar(10) comment '样本类型',collectedDate datetime comment '采样日期',
sampleDosage int comment '样本剂量',patientID int,primary key (sampleID));

create table testPic (picID mediumint not null auto_increment comment '主键',picType tinyint comment '图片类型（FISH图1，核型图2）',
delFlag tinyint comment '删除标志',upFlag tinyint comment '上传标志',sampleID int comment '样品条码,',primary key (picID));


create table variationSites(varID mediumint not null auto_increment comment '主键',	sampleID int  comment '样品条码,主键',
Chr char(2), Length int, sampleValue int, Conf float, vComment varchar(100),CNVIndex int,Cytobands varchar(200),
MarkersNo int,Genes varchar(6000), primary key(varID));

