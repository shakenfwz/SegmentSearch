package dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import dao.VariationsitesDao;
import domain.User;
import domain.Variationsites;
import utils.JdbcUtils;

public class VariationsitesDaoImpl implements VariationsitesDao {

	@Override
	public void add(Variationsites Variationsites) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "insert into variationsites(varID, sampleID, Chr, Length, "
					+ " sampleValue, Conf, vComment, CNVIndex, Cytobands, MarkersNo, Genes ) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?)";
			Object params[] = { Variationsites.getVarID(),Variationsites.getSampleID(),Variationsites.getChr(),
					Variationsites.getLength(),Variationsites.getSampleValue(),Variationsites.getConf(),
					Variationsites.getvComment(),Variationsites.getCNVIndex(),Variationsites.getCytobands(),
					Variationsites.getMarkersNo(),Variationsites.getGenes()};
			runner.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public Variationsites find(String id) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "select * from Variationsites where  varid=?";
			return (Variationsites) runner.query(sql, id, new BeanHandler(Variationsites.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Variationsites Variationsites) {
		// TODO Auto-generated method stub

	}

}
