package ideliance.servlet.subject;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Subject;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class SubjectListServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(SubjectListServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during SubjectListServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setAttribute("title", "Liste des sujets");
		
		try {
			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			
			List<Subject> listSubject = subjectDao.selectAll(false);
			
			request.setAttribute("listSubject", listSubject);
			
			request.getRequestDispatcher("/WEB-PAGES/Subject/listSubject.jsp").forward(request, response);
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
