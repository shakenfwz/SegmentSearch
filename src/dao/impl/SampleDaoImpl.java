package dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.SampleDao;
import domain.Samples;
import utils.JdbcUtils;

public class SampleDaoImpl implements SampleDao {

	private static final Logger log = LogManager.getLogger(SampleDaoImpl.class);

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
			log.error("Samples add failed", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Samples find(String id) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "select * from Samples where Sampleid=?";
			return runner.query(sql, new BeanHandler<>(Samples.class), id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Samples Samples) {
		// TODO Auto-generated method stub

	}

}
