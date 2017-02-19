package domain;

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
}
