package ideliance.servlet.user;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.UserDAO;
import ideliance.core.dao.exception.DAOException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class UserDeleteServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(UserDeleteServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during UserDeleteServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String root = (String) request.getSession().getAttribute("root");
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			UserDAO userDao = daoFactory.getUserDAO();
			
			userDao.delete(id);
			
			response.sendRedirect(root + "administration/user/");
		} catch (NumberFormatException e) {
			log.error("Id conversion failed", e);
			
			request.setAttribute("error", "Id conversion failed : " + e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
		} catch (DAOException e) {
			log.error("An error occured", e);
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}
}
