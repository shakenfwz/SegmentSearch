package utils;

public class DaoFactory {

	private static final DaoFactory factory = new DaoFactory();
	private DaoFactory(){}

	public static DaoFactory getInstance(){
		return factory;
	}

	public <T> T createDao(String className, Class<T> clazz){
		try{
			Object obj = Class.forName(className).getDeclaredConstructor().newInstance();
			return clazz.cast(obj);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
