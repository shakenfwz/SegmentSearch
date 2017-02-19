package dao;

import java.util.List;
import domain.Patient; 

public interface PatientDao {
	
	void add(Patient patient);

	Patient find(String id);
	
	public List<Patient> getPageData(int startindex, int pagesize);
	
	public int getTotalRecord();

	public List<Patient> getPageData(int startindex, int pagesize, String patient_id);
	
	public int getTotalRecord(String patient_id);
}


