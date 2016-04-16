select * from customers;

/*drop table patientInfo;*/
create table patientInfo (ID mediumint not null auto_increment comment '主关键',patientID int comment '病例编号',pName varchar(10) comment '受检者姓名',
Sex char(2) comment '受检者性别',age int comment '受检者年龄',ptype tinyint comment '受检者类型',
ClinicalFindings varchar(200) comment '症状表现',ClinicalData varchar(200) comment '临床资料',PatientRegion varchar(50) comment '受检者地区',
FamilyID int comment '受检者家系编号',MotherID int comment '受检者母亲编号',MotherName varchar(10) comment '受检者母亲姓名',MatherAge int comment '受检者母亲年龄',
FatherID int comment '受检者父亲编号',FatherName varchar(10) comment '受检者父亲姓名',FatherAge int comment '受检者父亲年龄',primary key (ID)) ;