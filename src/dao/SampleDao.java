package dao;

import domain.Samples;

public interface SampleDao {
	void add(Samples Samples);

	Samples find(String id);
	
	void update(Samples Samples);
}
