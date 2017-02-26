package dao;

import domain.Variationsites;;

public interface VariationsitesDao {
	void add(Variationsites Variationsites);

	Variationsites find(String id);
	
	void update(Variationsites Variationsites);

}
