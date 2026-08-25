package dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.VariationsitesDao;
import dao.VariationsitesListParser;
import dao.VariationsitesParser;
import domain.Variationsites;
import utils.JdbcUtils;

public class VariationsitesDaoImpl implements VariationsitesDao {

	private static final Logger log = LogManager.getLogger(VariationsitesDaoImpl.class);

	@Override
	public void add(Variationsites Variationsites) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "insert into variationsites(varID, sampleID, Chr, Start, Stop, Length, "
					+ " sampleValue, Conf, vComment, CNVIndex, Cytobands, MarkersNo, Genes ) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Object params[] = { Variationsites.getVarID(),Variationsites.getSampleID(),Variationsites.getChr(),
					Variationsites.getStart(),Variationsites.getStop(),
					Variationsites.getLength(),Variationsites.getSampleValue(),Variationsites.getConf(),
					Variationsites.getvComment(),Variationsites.getCNVIndex(),Variationsites.getCytobands(),
					Variationsites.getMarkersNo(),Variationsites.getGenes()};
			runner.update(sql, params);
		} catch (SQLException e) {
			log.error("Variationsites add failed", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Variationsites find(String id) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "select * from Variationsites where varid=?";
			return runner.query(sql, new VariationsitesParser(), id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Variationsites Variationsites) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Variationsites> findOverlap(String chr, long start, long stop) {
		try {
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			// 论文3.4.1 区间重叠统一公式：(t2start <= t1end and t2end >= t1start)
			String sql = "select * from variationSites where Chr=? and Start<=? and Stop>=? "
					+ "order by Start";
			return runner.query(sql, new VariationsitesListParser(), chr, stop, start);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
