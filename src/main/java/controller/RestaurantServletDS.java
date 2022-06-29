package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import bean.Restaurant;
import dao.RestaurantDAO;

@WebServlet("/RestaurantServletDS")
public class RestaurantServletDS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DataSource ds =null;
	InitialContext ctx = null;
	Connection con = null;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");

		try {
		
			//轉成Hi
//			ctx = new InitialContext();
//			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/veganDB");
//			System.out.println("3");
//			con = ds.getConnection();
			
			RestaurantDAO restaurantDAO = new RestaurantDAO();
			if (req.getParameter("所有餐廳") != null) {
				restaurantQueryAll(req, res, restaurantDAO);
			}
			if (req.getParameter("查詢餐廳") != null) {
				findRestaurantByNumber(req, res, restaurantDAO);
				System.out.println("查詢餐廳");
			}
			if (req.getParameter("查詢餐廳GO") != null) {
				restaurantQuery(req, res, restaurantDAO);
			}
			if (req.getParameter("新增餐廳") != null) {
				processCreate(req, res, restaurantDAO);
				System.out.println("新增餐廳");
			}
		
			if (req.getParameter("刪除餐廳") != null) {
				restaurantDeleteByNumber(req, res, restaurantDAO);
			}
			if (req.getParameter("修改餐廳") != null) {
				restaurantUpdate(req, res, restaurantDAO);
			}
		} catch (SQLException e) {
			System.out.println("Database Connection Error");
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				System.out.println("Connection Pool Error!");
			}
		}
	}
	
	// 查詢餐廳 by Address&Category&Type-前台搜尋
		private void restaurantQuery(HttpServletRequest req, HttpServletResponse res, RestaurantDAO restaurantDAO)
				throws SQLException, IOException {
			// 讀取餐廳資料
			String restaurantName = req.getParameter("restaurantName");
			String restaurantAddress = req.getParameter("restaurantAddress");
			String restaurantCategory = req.getParameter("restaurantCategory");
			if (restaurantCategory==null) {
				restaurantCategory="";}
			String restaurantType = req.getParameter("restaurantType");
			if (restaurantType==null) {
				restaurantType="";}
			
			// 透過DAO元件Access Dept Table
//			List<Restaurant> restaurantList = restaurantDAO.findRestaurant(restaurantName,restaurantAddress,restaurantCategory,restaurantType);
			//改寫為Hibernate方法，其餘不變。
			List<Restaurant> restaurantList = restaurantDAO.findAll();
			// session
			req.getSession().setAttribute("restaurantList",restaurantList);
			
			if (restaurantList == null)
				try {
					RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/errorPage/showError.jsp");
					dispatcher.forward(req, res);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			else
				req.setAttribute("restaurantList", restaurantList);
			try {
//				RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/restaurantBackground/restaurantFormBackground.jsp");
				RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/restaurantForm.jsp");
				dispatcher.forward(req, res);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
			
		} 
	
	/* 查詢餐廳 by Address&Category&Type
	private void restaurantQuery(HttpServletRequest req, HttpServletResponse res, RestaurantDAO restaurantDAO)
			throws SQLException, IOException {
		// 讀取餐廳資料
		String restaurantName = req.getParameter("restaurantName");
		String restaurantAddress = req.getParameter("restaurantAddress");
		String[] restaurantCategory =req.getParameterValues("restaurantCategory");
		String[] test = {"","","",""};
		
		for (int i = 0; i < restaurantCategory.length; i++) {
			test[i]=restaurantCategory[i];
			System.out.println(test[i]);
		}
		String restaurantType = req.getParameter("restaurantType");
		if (restaurantType==null) {
			restaurantType="";}
		
		// 透過DAO元件Access Dept Table
		List<Restaurant> restaurantList = restaurantDAO.findRestaurant(restaurantName,restaurantAddress,test,restaurantType);
		if (restaurantList == null)
			try {
				RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/errorPage/showError.jsp");
				dispatcher.forward(req, res);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		else
			req.setAttribute("restaurantList", restaurantList);
		try {
			RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/restaurantForm.jsp");
			dispatcher.forward(req, res);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
		
	} */
		
		// 所有餐廳 select all-後台
		private void restaurantQueryAll(HttpServletRequest req, HttpServletResponse res, RestaurantDAO restaurantDAO)
				throws SQLException, IOException {
			
			// 透過DAO元件Access Dept Table
			List<Restaurant> restaurantList = restaurantDAO.findAllRestaurant();
			if (restaurantList == null)
				try {
					RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/errorPage/showError.jsp");
					dispatcher.forward(req, res);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			else
				req.setAttribute("restaurantList", restaurantList);
				try {
					RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/restaurantBackground/restaurantFormBackground.jsp");
					//RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/restaurantForm.jsp");
					dispatcher.forward(req, res);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
				
		}
	
	
	// 查詢餐廳 by number
	private void findRestaurantByNumber(HttpServletRequest req, HttpServletResponse res, RestaurantDAO restaurantDAO)
			throws SQLException, IOException {
		// 讀取餐廳編號
		String restaurantNumber = req.getParameter("restaurantNumber");
		
		System.out.println("到了findRestaurantByNumber");
		System.out.println(restaurantNumber);
		
		// 透過DAO元件Access Dept Table
		Restaurant restaurant = restaurantDAO.findRestaurantByNumber(Integer.parseInt(restaurantNumber));
		if (restaurant == null)
			try {
				System.out.println("空");
				RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/errorPage/showError.jsp");
				dispatcher.forward(req, res);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		else
			try {
				req.setAttribute("restaurant", restaurant);
				RequestDispatcher dispatcher = req.getRequestDispatcher("/checkForm.jspf");
				dispatcher.forward(req, res);
				System.out.println("轉到checkForm");
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		
	}
		
	// 新增餐廳-後台
	private void processCreate(HttpServletRequest req, HttpServletResponse res, RestaurantDAO restaurantDAO)
			throws SQLException, IOException {
//		String restaurantNumber = req.getParameter("restaurantNumber");
		String restaurantName = req.getParameter("restaurantName");
		String restaurantTel = req.getParameter("restaurantTel");
		String restaurantAddress = req.getParameter("restaurantAddress");
		String restaurantCategory = req.getParameter("restaurantCategory");
		String restaurantType = req.getParameter("restaurantType");
		String restaurantBusinessHours = req.getParameter("restaurantBusinessHours");
		String restaurantScore = req.getParameter("restaurantScore");
		
		
		Restaurant restaurant =new Restaurant(restaurantName, restaurantTel, restaurantAddress, restaurantCategory, restaurantType, restaurantBusinessHours, restaurantScore);

		Boolean insertBoolean = restaurantDAO.createRestaurant(restaurant);
		if (insertBoolean) {
			try {
				req.setAttribute("restaurant", restaurant);
				RequestDispatcher dispatcher = req.getRequestDispatcher("/backend.jspf");
				dispatcher.forward(req, res);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}
		
		
	} 

	
	// 刪除餐廳-後台
	private void restaurantDeleteByNumber(HttpServletRequest req, HttpServletResponse res, RestaurantDAO restaurantDAO)
			throws SQLException, IOException {
		
		String restaurantNumber = req.getParameter("restaurantNumber");
		// 透過DAO元件Access Dept Table
		Boolean booleanDelete = restaurantDAO.deleteRestaurantByNumber(Integer.parseInt(restaurantNumber));
		if (booleanDelete)
			try {
				RequestDispatcher dispatcher = req.getRequestDispatcher("/backend.jspf");
				//RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/restaurantForm.jsp");
				dispatcher.forward(req, res);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
	}
	
	// 修改餐廳-後台
	private void restaurantUpdate(HttpServletRequest req, HttpServletResponse res, RestaurantDAO restaurantDAO)
			throws SQLException, IOException {
		String restaurantNumber = req.getParameter("restaurantNumber");
		String restaurantName = req.getParameter("restaurantName");
		String restaurantTel = req.getParameter("restaurantTel");
		String restaurantAddress = req.getParameter("restaurantAddress");
		String restaurantCategory = req.getParameter("restaurantCategory");
		String restaurantType = req.getParameter("restaurantType");
		String restaurantBusinessHours = req.getParameter("restaurantBusinessHours");
		String restaurantScore = req.getParameter("restaurantScore");

		Restaurant restaurant = restaurantDAO.findRestaurantByNumber(Integer.parseInt(restaurantNumber));
		if (restaurant == null)
			try {
				RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/showError.jsp");
				dispatcher.forward(req, res);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		else {
			restaurant.setRestaurantName(restaurantName);
			restaurant.setRestaurantTel(restaurantTel);
			restaurant.setRestaurantAddress(restaurantAddress);
			restaurant.setRestaurantCategory(restaurantCategory);
			restaurant.setRestaurantType(restaurantType);
			restaurant.setRestaurantBusinessHours(restaurantBusinessHours);
			restaurant.setRestaurantScore(restaurantScore);
			if (restaurantDAO.updateRestaurant(restaurant))
				try {
					RequestDispatcher dispatcher = req.getRequestDispatcher("/backend.jspf");
					//RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/restaurantForm.jsp");
					dispatcher.forward(req, res);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
		}
	}


}
