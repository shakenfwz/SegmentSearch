package domain;

import java.util.Date;

public class Patient {
	private int PatientID;
	private String PName;
	private byte Sex;
	private int Age;
	private byte PType;
	private String ClinicalFindings; //症状表现
	private String ClinicalData; //临床资料
	private String PatientRegion;//受检者地区
	private int FamilyID; //受检者家系编号
	private int MotherID;//受检者母亲编号
	private String MotherName;
	private int MotherAge;
	private int FatherID;
	private String FatherName;
	private int FatherAge;
	// 新增：受检者扩展信息（出生年月/体重/身高/身份证/电话/地址/孕周/不良妊娠史）
	private Date Birthday;
	private double Weight;
	private double Height;
	private String IDNumber;
	private String Phone;
	private String Mobilephone;
	private String Address;
	private int PrenWeeks;
	private String PrenHistory;

	public int getPatientID() {
		return PatientID;
	}
	public void setPatientID(int patientID) {
		PatientID = patientID;
	}
	public String getPName() {
		return PName;
	}
	public void setPName(String pName) {
		PName = pName;
	}
	public byte getSex() {
		return Sex;
	}
	public void setSex(byte sex) {
		Sex = sex;
	}
	public int getAge() {
		return Age;
	}
	public void setAge(int age) {
		Age = age;
	}
	public byte getPType() {
		return PType;
	}
	public void setPType(byte pType) {
		PType = pType;
	}
	public String getClinicalFindings() {
		return ClinicalFindings;
	}
	public void setClinicalFindings(String clinicalFindings) {
		ClinicalFindings = clinicalFindings;
	}
	public String getClinicalData() {
		return ClinicalData;
	}
	public void setClinicalData(String clinicalData) {
		ClinicalData = clinicalData;
	}
	public String getPatientRegion() {
		return PatientRegion;
	}
	public void setPatientRegion(String patientRegion) {
		PatientRegion = patientRegion;
	}
	public int getFamilyID() {
		return FamilyID;
	}
	public void setFamilyID(int familyID) {
		FamilyID = familyID;
	}
	public int getMotherID() {
		return MotherID;
	}
	public void setMotherID(int motherID) {
		MotherID = motherID;
	}
	public String getMotherName() {
		return MotherName;
	}
	public void setMotherName(String motherName) {
		MotherName = motherName;
	}
	public int getMotherAge() {
		return MotherAge;
	}
	public void setMotherAge(int motherAge) {
		MotherAge = motherAge;
	}
	public int getFatherID() {
		return FatherID;
	}
	public void setFatherID(int fatherID) {
		FatherID = fatherID;
	}
	public String getFatherName() {
		return FatherName;
	}
	public void setFatherName(String fatherName) {
		FatherName = fatherName;
	}
	public int getFatherAge() {
		return FatherAge;
	}
	public void setFatherAge(int fatherAge) {
		FatherAge = fatherAge;
	}
	public Date getBirthday() {
		return Birthday;
	}
	public void setBirthday(Date birthday) {
		Birthday = birthday;
	}
	public double getWeight() {
		return Weight;
	}
	public void setWeight(double weight) {
		Weight = weight;
	}
	public double getHeight() {
		return Height;
	}
	public void setHeight(double height) {
		Height = height;
	}
	public String getIDNumber() {
		return IDNumber;
	}
	public void setIDNumber(String iDNumber) {
		IDNumber = iDNumber;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getMobilephone() {
		return Mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		Mobilephone = mobilephone;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public int getPrenWeeks() {
		return PrenWeeks;
	}
	public void setPrenWeeks(int prenWeeks) {
		PrenWeeks = prenWeeks;
	}
	public String getPrenHistory() {
		return PrenHistory;
	}
	public void setPrenHistory(String prenHistory) {
		PrenHistory = prenHistory;
	}
}
