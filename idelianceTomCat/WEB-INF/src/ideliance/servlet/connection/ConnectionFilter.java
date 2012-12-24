package ideliance.servlet.connection;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.UserDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class ConnectionFilter implements Filter {
	public static final Logger log = Logger.getLogger(ConnectionFilter.class);

	private FilterConfig config;
	
	private DAOFactory daoFactory = null;
	
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during UserDeleteServlet initialization", e);
		}
	}
	
	public void destroy() {	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		String exclude = config.getInitParameter("excludePatterns");

		if (req.getServletPath().matches(exclude)) {
			request.getRequestDispatcher(req.getServletPath()).forward(request, response);
			return;
		}
		
		Cookie [] tCookies = req.getCookies();
		
		Map<String, String> cookies = new HashMap<String, String>();
		if (tCookies != null) {
	    	for (Cookie cookie : tCookies) {
	    		cookies.put(cookie.getName(), cookie.getValue());
	    	}
		}
    	
    	// Check the login method
		HttpSession session = ((HttpServletRequest)request).getSession();
    	String login, password;
    	if (session.getAttribute("login") != null && session.getAttribute("password") != null) {
    		login = (String)session.getAttribute("login");
    		password = (String)session.getAttribute("password");
    	} else if (cookies.get("login") != null && cookies.get("password") != null) {
    		login = cookies.get("login");
    		password = cookies.get("password");
    	} else if (request.getParameter("login") != null && request.getParameter("password") != null) {
    		login = request.getParameter("login");
    		password = request.getParameter("password");
    	} else {
    		request.getRequestDispatcher("/WEB-PAGES/Other/connection.jsp").forward(request, response);
    		return;
    	}
    	
    	// Check login and password
    	User u = null;
		try {
			UserDAO userDao = daoFactory.getUserDAO();
			
			int idUser = userDao.exists(login, password);
			
			u = userDao.selectSingle(idUser);
		} catch (DAOException e) {
			log.error("An error occured", e);
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
			return;
		}
		
		if (u == null) {
			request.setAttribute("error", "login");
			request.getRequestDispatcher("/WEB-PAGES/Other/connection.jsp").forward(request, response);
		} else {
	    	// Set session parameters
			session.setAttribute("login", login);
			session.setAttribute("password", password);
			session.setAttribute("level", u.getLevel());
			
			// TODO Add lang parameter
			session.setAttribute("lang", "en");
			session.setAttribute("root", "/");
			
			chain.doFilter(request, response);
		}
	}
}