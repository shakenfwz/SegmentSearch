select * from customers;

/*drop table patientInfo;*/
create table patientInfo (ID mediumint not null auto_increment comment '���ؼ�',patientID int comment '�������',pName varchar(10) comment '�ܼ�������',
Sex char(2) comment '�ܼ����Ա�',age int comment '�ܼ�������',ptype tinyint comment '�ܼ�������',
ClinicalFindings varchar(200) comment '֢״����',ClinicalData varchar(200) comment '�ٴ�����',PatientRegion varchar(50) comment '�ܼ��ߵ���',
FamilyID int comment '�ܼ��߼�ϵ���',MotherID int comment '�ܼ���ĸ�ױ��',MotherName varchar(10) comment '�ܼ���ĸ������',MatherAge int comment '�ܼ���ĸ������',
FatherID int comment '�ܼ��߸��ױ��',FatherName varchar(10) comment '�ܼ��߸�������',FatherAge int comment '�ܼ��߸�������',primary key (ID)) ;