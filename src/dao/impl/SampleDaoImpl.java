package dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import dao.SampleDao;
import domain.Samples;
import domain.User;
import utils.JdbcUtils;

public class SampleDaoImpl implements SampleDao {

	@Override
	public void add(Samples Samples) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "insert into samples(sampleID, sampleName, testMethod, sendDoctor, sendDate,"
					+ "  receiveDate, sampleType, collectedDate, sampleDosage, patientID, CheckDate) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?)";
			Object params[] = { Samples.getSampleID(),Samples.getSampleName(),Samples.getTestMethod(),
					Samples.getSendDoctor(),Samples.getSendDate(),Samples.getReceiveDate(),Samples.getSampleType(),
					Samples.getCollectedDate(),Samples.getSampleDosage(),Samples.getPatientID(),Samples.getCheckDate()};
			runner.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public Samples find(String id) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "select * from Samples where Sampleid=?";
			return (Samples) runner.query(sql, id, new BeanHandler(Samples.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Samples Samples) {
		// TODO Auto-generated method stub

	}

}
