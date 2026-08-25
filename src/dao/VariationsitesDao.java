package dao;

import domain.Variationsites;
import java.util.List;

public interface VariationsitesDao {
	void add(Variationsites Variationsites);

	Variationsites find(String id);

	void update(Variationsites Variationsites);

	/**
	 * 本地检测诊断（论文3.4.1 集合比对算法）：查找与查询区间
	 * [start, stop] 重叠的变异位点记录。
	 */
	List<Variationsites> findOverlap(String chr, long start, long stop);
}
