package dao.impl;

import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.JdbcUtils;
import dao.PatientDao;
import domain.Patient;

public class PatientDaoImpl implements PatientDao {

	private static final Logger log = LogManager.getLogger(PatientDaoImpl.class);

	@Override
	public void add(Patient patient) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "insert into Patients(patientID, pName, Sex, age, ptype, ClinicalFindings, ClinicalData,"
					+ " PatientRegion, FamilyID, MotherID, MotherName, MatherAge, FatherID, FatherName, FatherAge) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Object params[] = { patient.getPatientID(), patient.getPName(), patient.getSex(), patient.getAge(),
					patient.getPType(), patient.getClinicalFindings(), patient.getClinicalData(),
					patient.getPatientRegion(), patient.getFamilyID(), patient.getMotherID(), patient.getMotherName(),
					patient.getMotherAge(), patient.getFatherID(), patient.getFatherName(), patient.getFatherAge() };
			runner.update(sql, params);
		} catch (SQLException e) {
			log.error("Patient add failed", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Patient find(String id) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "select * from Patients where Patientid=?";
			return runner.query(sql, new BeanHandler<>(Patient.class), id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Patient> getPageData(int startindex, int pagesize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalRecord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Patient> getPageData(int startindex, int pagesize, String patient_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalRecord(String patient_id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
