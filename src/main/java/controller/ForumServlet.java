package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import bean.ForumBean;
import bean.User;
import dao.ForumDAO;

@WebServlet(name="ForumServlet" , urlPatterns="/ForumServlet")
public class ForumServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
	private static final String CHARSET_CODE = "UTF-8";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding(CHARSET_CODE);
		response.setContentType(CONTENT_TYPE);

		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", -1); // Prevents caching at the proxy server
		DataSource ds = null;
		InitialContext ctxt = null;
		Connection conn = null;
		try {

			ctxt = new InitialContext();

			ds = (DataSource) ctxt.lookup("java:comp/env/jdbc/veganDB");

			conn = ds.getConnection();

			ForumDAO forumDAO = new ForumDAO(conn);

			if (request.getParameter("Query") != null)
				processQuery(request, response, forumDAO);

			if (request.getParameter("Create") != null)
				processCreate(request, response,forumDAO);
		
			if (request.getParameter("Delete") != null)
				processDelete(request, response, forumDAO);

			if (request.getParameter("confirm") != null)
				processConfirm(request, response);

			if (request.getParameter("Update") != null)
				processUpdate(request, response,forumDAO);
			
			if(request.getParameter("ForumHome")!=null)
				prcoessHome(request,response);
		}

		catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				System.out.println("Connection Pool Error!");
			}
		}
	}

	protected void processQuery(HttpServletRequest request, HttpServletResponse response, ForumDAO forumDAO)
			throws ServletException, IOException {
		DataSource ds = null;
		InitialContext ctxt = null;
		Connection conn = null;
		try {
			ctxt = new InitialContext();

			// 雿輻JNDI API��DataSource
			ds = (DataSource) ctxt.lookup("java:comp/env/jdbc/veganDB");

			// ��ataSource閬onnection
			conn = ds.getConnection();
			// 撱箇�atabase Access Object,鞎痊Table��ccess
			forumDAO = new ForumDAO(conn); // STUDENTDAO閬��遣瑽����
			//String id = request.getParameter("vgeid");//���d
			String name = request.getParameter("vgename");
			ForumBean forumBean = forumDAO.queryForum(name);
			if(forumBean ==null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/forum/ErrorQuery.jsp");
				dispatcher .forward(request, response);
				// request.getRequestDispatcher("/ShowError.html");
			
			}else {
				request.setAttribute("vgeid", forumBean.getVgeid());
				request.setAttribute("vgename", forumBean.getVgename());
				request.setAttribute("vgetheme", forumBean.getVgetheme());
				request.setAttribute("vgecontent", forumBean.getVgecontent());
				
				request.getSession(true);
				request.getRequestDispatcher("/WEB-INF/jsp/forum/QueryResult.jsp").forward(request, response);			
			}
		} catch (NamingException ne) {
			System.out.println("Naming Service Lookup Exception");
		} catch (SQLException e) {
			System.out.println("Database Connection Error");
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				System.out.println("Connection Pool Error!");
			}
		}

	}

	protected void processCreate(HttpServletRequest request, HttpServletResponse response, ForumDAO forumDAO)
			throws ServletException, IOException {
		DataSource ds = null;
		InitialContext ctxt = null;
		Connection conn = null;
		try {
			String vgeid = request.getParameter("vgeid");
			// 撱箇�ontext Object,���JNDI Server
			ctxt = new InitialContext();

			// 雿輻JNDI API��DataSource
			ds = (DataSource) ctxt.lookup("java:comp/env/jdbc/veganDB");

			// ��ataSource閬onnection
			conn = ds.getConnection();

			// 撱箇�atabase Access Object,鞎痊Table��ccess
			ForumBean forumBean = forumDAO.queryForum(vgeid);
			if(forumBean != null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/forum/ErrorCreate.jsp");
				dispatcher .forward(request, response);
			}else {
				
				 forumDAO = new ForumDAO(conn); // STUDENTDAO閬��遣瑽����
				 String vgename = request.getParameter("vgename");
				 String vgetheme = request.getParameter("vgetheme");
				 String vgecontent = request.getParameter("vgecontent");
				 ForumBean vge = new ForumBean(vgeid, vgename, vgetheme, vgecontent);
				
				 if( vgename.equals("") || vgetheme.equals("") ||vgecontent.equals("")) {
					 RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/forum/ErrorCreate.jsp");
					 dispatcher .forward(request, response);
				 }else {
				 
					 System.out.println(vgecontent+" "+vgename+" "+vgetheme);
					 request.getSession(true).setAttribute("vge", vge);
					 request.getRequestDispatcher("/WEB-INF/jsp/forum/DisplayForum.jsp").forward(request, response);
					 }
			}
			
		} catch (NamingException ne) {
			System.out.println("Naming Service Lookup Exception");
		} catch (SQLException e) {
			System.out.println("Database Connection Error");
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				System.out.println("Connection Pool Error!");
			}
		}
	}

	protected void processUpdate(HttpServletRequest request, HttpServletResponse response, ForumDAO forumDAO)
			throws ServletException, IOException {
		
		DataSource ds = null;
		InitialContext ctxt = null;
		Connection conn = null;
		try {
			ctxt = new InitialContext();
			ds = (DataSource) ctxt.lookup("java:comp/env/jdbc/veganDB");
			conn = ds.getConnection();
			forumDAO = new ForumDAO(conn);
			
			String vgeid =request.getParameter("vgeid");
			String vgename = request.getParameter("vgename");
			String vgetheme = request.getParameter("vgetheme");
			String vgecontent = request.getParameter("vgecontent");
			ForumBean foruData=new ForumBean (vgeid,vgename,vgetheme,vgecontent);
			forumDAO.updateForum(foruData);
			//foruData.setVgeid(vgeid);
			foruData.setVgename(vgename);
			foruData.setVgetheme(vgetheme);
			foruData.setVgecontent(vgecontent);
			
			request.getSession(true).setAttribute("foruData", foruData);
			request.getRequestDispatcher("/WEB-INF/jsp/forum/UpdateResult.jsp").forward(request, response);
		} catch (NamingException ne) {
			System.out.println("Naming Service Lookup Exception");
		} catch (SQLException e) {
			System.out.println("Database Connection Error");
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				System.out.println("Connection Pool Error!");
			}
		}
	}

	protected void processDelete(HttpServletRequest request, HttpServletResponse response ,ForumDAO forumDAO)
			throws ServletException, IOException {
		DataSource ds = null;
		InitialContext ctxt = null;
		Connection conn = null;
		try {
			ctxt = new InitialContext();

			ds = (DataSource) ctxt.lookup("java:comp/env/jdbc/veganDB");

			conn = ds.getConnection();
			forumDAO = new ForumDAO(conn); // STUDENTDAO閬��遣瑽����
			 //���d
			 String id =request.getParameter("vgeid") ;		 
			 forumDAO.deleteForum(id);
			 request.getSession(true);
			 request.getRequestDispatcher("/WEB-INF/jsp/forum/DeleteForum.jsp").forward(request, response);
			


		} catch (NamingException ne) {
			System.out.println("Naming Service Lookup Exception");
		} catch (SQLException e) {
			System.out.println("Database Connection Error");
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				System.out.println("Connection Pool Error!");
			}
			}	
	}

	
	public void processConfirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User userForum = (User) request.getSession().getAttribute("user");
		DataSource ds = null;
		InitialContext ctxt = null;
		Connection conn = null;
		try (PrintWriter out = response.getWriter()){
			// 撱箇�ontext Object,���JNDI Server
			ctxt = new InitialContext();

			ds = (DataSource) ctxt.lookup("java:comp/env/jdbc/veganDB");
			conn = ds.getConnection();

			@SuppressWarnings("unused")
			ForumDAO forumDAO = new ForumDAO(conn); // STUDENTDAO閬��遣瑽����
			ForumBean forumData = (ForumBean) request.getSession(true).getAttribute("vge");
			
			
			if (ForumDAO.insertForum(forumData)) {
				request.setAttribute("user", userForum);
				request.getSession().getAttribute("user");
				request.getRequestDispatcher("/WEB-INF/jsp/forum/Thank.jsp").forward(request, response);
			}
		} catch (NamingException ne) {
			System.out.println("Naming Service Lookup Exception");
		} catch (SQLException e) {
			System.out.println("Database Connection Error");
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				System.out.println("Connection Pool Error!");
			}
		}

	}
	public void prcoessHome(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

				User userForum = (User) request.getSession().getAttribute("user");
		try (PrintWriter out = response.getWriter()){
			if(userForum==null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/forum/QueryForum.jsp");
					dispatcher.forward(request, response);

			}else {
					if(userForum.getUid()==0) 
						response.sendRedirect("/vegetarian/Login");
						request.getRequestDispatcher("/WEB-INF/jsp/forum/QueryForum.jsp").forward(request,response);
				
		}		
	}catch (Exception e) {
	}
	}
	
	
}
