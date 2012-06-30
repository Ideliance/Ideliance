package ideliance.servlet.user;

import ideliance.core.config.ApplicationContext;
import ideliance.core.config.MyTimestamp;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.UserDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.User;
import ideliance.core.object.type.UserLevel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class UserAddServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(UserAddServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during UserAddServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String title = "Modifier un utilisateur";
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			UserDAO userDao = daoFactory.getUserDAO();
			
			if (id != -1) {
				User u = userDao.selectSingle(id);
				
				request.setAttribute("user", u);
			}
			
			request.setAttribute("listLevel", UserLevel.values());
		} catch(NumberFormatException e) {
			title = "Ajouter un utilisateur";
		} catch (DAOException e) {
			log.error("An error occured", e);
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
			return;
		}
		
		// Set page title
		request.setAttribute("title", title);
		
		request.getRequestDispatcher("/WEB-PAGES/Administration/User/userAdd.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String root = (String) request.getSession().getAttribute("root");
		
		try {
			if (request.getParameter("id") != null) {
				update(request);
			} else {
				add(request);
			}
			
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
	
	private void add(HttpServletRequest request) throws DAOException {
		HttpSession session = request.getSession();
		
		UserDAO userDao = daoFactory.getUserDAO();
		
		User u = new User();
		u.setLogin(request.getParameter("new-login"));
		u.setPassword(request.getParameter("new-password"));
		u.setLevel(UserLevel.fromString(request.getParameter("new-level")));
		u.setAuthorCreation((String) session.getAttribute("login"));
		u.setAuthorModification((String) session.getAttribute("login"));
		
		userDao.add(u);
	}
	
	private void update(HttpServletRequest request) throws DAOException, NumberFormatException {
		int id = Integer.parseInt(request.getParameter("id"));
		
		HttpSession session = request.getSession();
		
		UserDAO userDao = daoFactory.getUserDAO();
		
		User u = userDao.selectSingle(id);
		u.setLogin(request.getParameter("new-login"));
		
		if (!request.getParameter("new-password").equals("")) {
			u.setPassword(request.getParameter("new-password"));
		}
		
		u.setLevel(UserLevel.fromString(request.getParameter("new-level")));
		u.setAuthorModification((String) session.getAttribute("login"));
		u.setDateModification(MyTimestamp.getCurrentTimestamp().getTime());
		
		userDao.update(u);
	}
}
