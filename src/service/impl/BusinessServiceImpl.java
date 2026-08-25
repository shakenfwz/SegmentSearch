package service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.DaoFactory;
//import dao.BookDao;
//import dao.CategoryDao;
//import dao.OrderDao;
import dao.UserDao;
//import domain.Book;
//import domain.Cart;
//import domain.CartItem;
//import domain.Category;
//import domain.Order;
//import domain.OrderItem;
//import domain.Page;
import domain.Patient;
import domain.Samples;
import domain.User;
import service.BusinessService;
import utils.JdbcUtils;

public class BusinessServiceImpl implements BusinessService {
	
	//private CategoryDao categoryDao = DaoFactory.getInstance().createDao("dao.impl.CategoryDaoImpl", CategoryDao.class);
	//private BookDao bookDao = DaoFactory.getInstance().createDao("dao.impl.BookDaoImpl", BookDao.class);
	private UserDao userDao = DaoFactory.getInstance().createDao("dao.impl.UserDaoImpl", UserDao.class);
	//private OrderDao orderDao = DaoFactory.getInstance().createDao("dao.impl.OrderDaoImpl", OrderDao.class);
//	/*****************************************************************************************
//	/* (non-Javadoc)
//	 * @see service.impl.BusinessService#addCategory(domain.Category)
//	 */
//	public void addCategory(Category category){
//		categoryDao.add(category);
//	}
//	
//	/* (non-Javadoc)
//	 * @see service.impl.BusinessService#findCategory(java.lang.String)
//	 */
//	public Category findCategory(String id){
//		return categoryDao.find(id);
//	}
//	
//	/* (non-Javadoc)
//	 * @see service.impl.BusinessService#getAllCategory()
//	 */
//	public List<Category> getAllCategory(){
//		return categoryDao.getAll();
//	}
//	
//	//添加书
//	public void addBook(Book book){
//		bookDao.add(book);
//	}
//	
//	//获得书
//	public Book findBook(String id){
//		return bookDao.find(id);
//	}
//	
//	//获得分页数据
//	public Page getBookPageData(String pagenum){
//		int totalrecord = bookDao.getTotalRecord();
//		Page page = null;
//		if(pagenum == null){
//			page = new Page(1,totalrecord);
//		}else{
//			page = new Page(Integer.parseInt(pagenum), totalrecord);
//		}
//		List<Book> list = bookDao.getPageData(page.getStartindex(), page.getPagesize());
//		page.setList(list);
//		return page;
//	}
//	
//	public Page getBookPageData(String pagenum, String category_id){
//		int totalrecord = bookDao.getTotalRecord(category_id);
//		Page page = null;
//		if(pagenum == null){
//			page = new Page(1,totalrecord);
//		}else{
//			page = new Page(Integer.parseInt(pagenum), totalrecord);
//		}
//		List<Book> list = bookDao.getPageData(page.getStartindex(), page.getPagesize(), category_id);
//		page.setList(list);
//		return page;
//	}
//
//	public void buyBook(Cart cart, Book book) {
//		cart.add(book);
//	}
//	***************************************************************************************************/
	//注册用户
	public void registerUser(User user) {
		userDao.add(user);
	}
	
	public User findUser(String id){
		return userDao.find(id);
	}

	public User userLogin(String username, String password){
		return userDao.find(username, password);
	}
	
	/** 按用户名查询用户（供 LoginService 做 PBKDF2 密码校验）。 */
	public User findUserByName(String username){
		return userDao.findByName(username);
	}
	
	/** 更新用户密码（明文密码透明迁移为哈希）。 */
	public void updateUserPassword(User user){
		userDao.updatePassword(user);
	}
	
//	//生成订单
//	public void createOrder(Cart cart, User user){
//		if(cart == null){
//			throw new RuntimeException("对不起，您还没有购买任何商品");
//		}
//		Order order = new Order();
//		order.setId(WebUtils.makeID());
//		order.setOrdertime(new Date());
//		order.setPrice(cart.getPrice());
//		order.setState(false);
//		order.setUser(user);
//		for(Map.Entry<String, CartItem> me : cart.getMap().entrySet()){
//			//得到一个购物项就生成一个订单项
//			CartItem citem = me.getValue();
//			OrderItem oitem = new OrderItem();
//			oitem.setBook(citem.getBook());
//			oitem.setPrice(citem.getPrice());
//			oitem.setId(WebUtils.makeID());
//			oitem.setQuantity(citem.getQuantity());
//			order.getOrderitems().add(oitem);
//		}	
//		orderDao.add(order);
//	}
//
//	//后台获取所有订单信息
//	public List<Order> listOrder(String state) {
//		return orderDao.getAll(Boolean.parseBoolean(state));	
//	}
//
//	//列出订单明细
//	public Order findOrder(String orderid) {		
//		return orderDao.find(orderid);
//	}
//
//	//把订单置为发货状态
//	public void confirmOrder(String orderid) {
//		Order order = orderDao.find(orderid);
//		order.setState(true);
//		orderDao.update(order);
//	}
//
//	//获得某个用户的订单信息
//	public List<Order> listOrder(String state, String userid) {
//		return orderDao.getAll(Boolean.parseBoolean(state), userid);
//	}
//
//	//获取某个用户的订单信息
//	public List<Order> clientListOrder(String userid) {	
//		return orderDao.getAllOrder(userid);
//	}

	private static final Logger log = LogManager.getLogger(BusinessServiceImpl.class);

	private static final String SQL_INSERT_PATIENT =
			"insert into patientInfo(patientID, pName, Sex, age, ptype, ClinicalFindings, ClinicalData,"
			+ " PatientRegion, FamilyID, MotherID, MotherName, MatherAge, FatherID, FatherName, FatherAge,"
			+ " Birthday, Weight, Height, IDNumber, Phone, Mobilephone, Address, PrenWeeks, PrenHistory) "
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static final String SQL_INSERT_SAMPLE =
			"insert into sampleinfo(sampleID, sampleName, testMethod, sendDoctor, sendDate,"
			+ "  receiveDate, sampleType, collectedDate, sampleDosage, patientID) "
			+ "values(?,?,?,?,?,?,?,?,?,?)";

	@Override
	public int addSampleWithPatient(Patient patient, Samples sample) {
		Connection conn = null;
		try {
			conn = JdbcUtils.getConnection();
			boolean oldAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			try (PreparedStatement ps1 = conn.prepareStatement(SQL_INSERT_PATIENT)) {
				int patientId = patient.getPatientID();
				ps1.setInt(1, patientId);
				ps1.setString(2, patient.getPName());
				ps1.setByte(3, patient.getSex());
				ps1.setInt(4, patient.getAge());
				ps1.setByte(5, patient.getPType());
				ps1.setString(6, patient.getClinicalFindings());
				ps1.setString(7, patient.getClinicalData());
				ps1.setString(8, patient.getPatientRegion());
				ps1.setInt(9, patient.getFamilyID());
				ps1.setInt(10, patient.getMotherID());
				ps1.setString(11, patient.getMotherName());
				ps1.setInt(12, patient.getMotherAge());
				ps1.setInt(13, patient.getFatherID());
				ps1.setString(14, patient.getFatherName());
				ps1.setInt(15, patient.getFatherAge());
				ps1.setDate(16, patient.getBirthday() == null ? null
						: new java.sql.Date(patient.getBirthday().getTime()));
				ps1.setDouble(17, patient.getWeight());
				ps1.setDouble(18, patient.getHeight());
				ps1.setString(19, patient.getIDNumber());
				ps1.setString(20, patient.getPhone());
				ps1.setString(21, patient.getMobilephone());
				ps1.setString(22, patient.getAddress());
				ps1.setInt(23, patient.getPrenWeeks());
				ps1.setString(24, patient.getPrenHistory());
				ps1.executeUpdate();

				sample.setPatientID(patientId);
				try (PreparedStatement ps2 = conn.prepareStatement(SQL_INSERT_SAMPLE)) {
					ps2.setInt(1, sample.getSampleID());
					ps2.setString(2, sample.getSampleName());
					ps2.setString(3, sample.getTestMethod());
					ps2.setString(4, sample.getSendDoctor());
					ps2.setDate(5, sample.getSendDate() == null ? null
							: new java.sql.Date(sample.getSendDate().getTime()));
					ps2.setDate(6, sample.getReceiveDate() == null ? null
							: new java.sql.Date(sample.getReceiveDate().getTime()));
					ps2.setString(7, sample.getSampleType());
					ps2.setDate(8, sample.getCollectedDate() == null ? null
							: new java.sql.Date(sample.getCollectedDate().getTime()));
					ps2.setInt(9, sample.getSampleDosage());
					ps2.setInt(10, sample.getPatientID());
					ps2.executeUpdate();
				}
				conn.commit();
				return patientId;
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException re) {
					log.warn("rollback failed", re);
				}
				log.error("addSampleWithPatient failed, rolled back", e);
				throw new RuntimeException("患者与样本录入失败，已全部回滚", e);
			} finally {
				try {
					conn.setAutoCommit(oldAutoCommit);
				} catch (SQLException ignored) {
				}
			}
		} catch (SQLException e) {
			log.error("getConnection failed", e);
			throw new RuntimeException("获取数据库连接失败", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ignored) {
				}
			}
		}
	}
}
