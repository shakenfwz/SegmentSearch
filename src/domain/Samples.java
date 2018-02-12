package domain;

import java.util.Date;

public class Samples {
private int SampleID;
private String SampleName;
private String TestMethod;
private String SendDoctor;
private Date SendDate;
private Date ReceiveDate;
private String SampleType;
private Date CollectedDate;
private int SampleDosage;
private int PatientID;
private Date CheckDate;

public int getSampleID() {
	return SampleID;
}
public void setSampleID(int sampleID) {
	SampleID = sampleID;
}
public String getSampleName() {
	return SampleName;
}
public void setSampleName(String sampleName) {
	SampleName = sampleName;
}
public String getTestMethod() {
	return TestMethod;
}
public void setTestMethod(String testMethod) {
	TestMethod = testMethod;
}
public String getSendDoctor() {
	return SendDoctor;
}
public void setSendDoctor(String sendDoctor) {
	SendDoctor = sendDoctor;
}
public Date getSendDate() {
	return SendDate;
}
public void setSendDate(Date sendDate) {
	SendDate = sendDate;
}
public Date getReceiveDate() {
	return ReceiveDate;
}
public void setReceiveDate(Date receiveDate) {
	ReceiveDate = receiveDate;
}
public String getSampleType() {
	return SampleType;
}
public void setSampleType(String sampleType) {
	SampleType = sampleType;
}
public Date getCollectedDate() {
	return CollectedDate;
}
public void setCollectedDate(Date collectedDate) {
	CollectedDate = collectedDate;
}
public int getSampleDosage() {
	return SampleDosage;
}
public void setSampleDosage(int sampleDosage) {
	SampleDosage = sampleDosage;
}
public int getPatientID() {
	return PatientID;
}
public void setPatientID(int patientID) {
	PatientID = patientID;
}
public Date getCheckDate() {
	return CheckDate;
}
public void setCheckDate(Date checkDate) {
	CheckDate = checkDate;
}
}
