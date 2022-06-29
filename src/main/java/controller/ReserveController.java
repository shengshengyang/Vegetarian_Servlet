package controller;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Reserve;
import bean.Restaurant;
import Interface.IReserveDAO;

import dao.ReserveDAO;
import dao.RestaurantDAO;

/**
 * Servlet implementation class ReserveController
 */
@WebServlet("/reserve")
public class ReserveController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	IReserveDAO rDao = new ReserveDAO();
	RestaurantDAO restaurantDAO = new RestaurantDAO();
	Reserve reserve = new Reserve();
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReserveController() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		if(request.getParameter("oRestaurant") != null) {
			goToForm(request,response);
		}
		else {
			try {
				order(request,response);
			} catch (ServletException | IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		if(request.getParameter("act") != null) {
			goToForm(request,response);
		}
	}
	
	private void goToForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int restaurantNumber = Integer.parseInt(request.getParameter("id")) ;
		Restaurant restaurant = new Restaurant();
		restaurant = restaurantDAO.findRestaurantByNumber(restaurantNumber);
		String restaurantName = restaurant.getRestaurantName();
		String restaurantTel = restaurant.getRestaurantTel();
		String restaurantAddress = restaurant.getRestaurantAddress();
		String restaurantCategory = restaurant.getRestaurantCategory();
		String restaurantType = restaurant.getRestaurantType();
		String restaurantBusinessHours = restaurant.getRestaurantBusinessHours();
		String restaurantMap = restaurant.getRestaurantMap();
		
		request.setAttribute("restaurantNumber", restaurantNumber);
		request.setAttribute("restaurantName", restaurantName);
		request.setAttribute("restaurantTel", restaurantTel);
		request.setAttribute("restaurantAddress", restaurantAddress);
		request.setAttribute("restaurantCategory", restaurantCategory);
		request.setAttribute("restaurantType", restaurantType);
		request.setAttribute("restaurantBusinessHours", restaurantBusinessHours);
		request.setAttribute("restaurantMap", restaurantMap);
		
		
		request.getRequestDispatcher("reservationIndex.jsp").forward(request, response);
	}
	
	private void order(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
		String oDate =request.getParameter("orderdate");
		int count = Integer.valueOf(request.getParameter("memberCount").toString());
		int restaurantNumber = Integer.valueOf(request.getParameter("restaurantNumber").toString());
		int uid = Integer.valueOf(request.getParameter("userID").toString());
		boolean isSuccess = false ;
		Date orderDate=new SimpleDateFormat("yyyy-MM-dd").parse(oDate);
		
        reserve.setDate(orderDate);
        reserve.setCount(count);
        //--odate 紀錄伺服器現在時間 轉型存進資料庫
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);
        reserve.setOrderDate(formatDateTime);
        reserve.setRestaurantNumber(restaurantNumber);
        reserve.setUid(uid);
        
        if (rDao.insert(reserve) != null) isSuccess  = true;
		//英傑借我跳轉一下 測試用
		request.setAttribute("isSuccess", isSuccess);
		request.setAttribute("results", isSuccess ? "成功" : "失敗");
		
		request.getRequestDispatcher("/WEB-INF/jsp/OrderResult.jsp").forward(request, response);
	}
	
}
