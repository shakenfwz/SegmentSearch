package service;

import domain.Patient;
import domain.Samples;

//import domain.Category;

public interface BusinessService {

	/**添加分类**/
	//void addCategory(Category category);

	/**查找分类**/
	//Category findCategory(String id);

	/**得到所有分类**/
	//List<Category> getAllCategory();

	/**
	 * 在同一事务中新增患者 + 样本：要么同时成功，要么同时回滚。
	 * @param patient  患者信息
	 * @param sample   样本信息（PatientID 会被回填为新增患者的 ID）
	 * @return 新增患者的 ID
	 */
	int addSampleWithPatient(Patient patient, Samples sample);

}