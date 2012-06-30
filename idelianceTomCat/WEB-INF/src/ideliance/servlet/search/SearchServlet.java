package ideliance.servlet.search;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;
import ideliance.core.object.Subject;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class SearchServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(SearchServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during SearchServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setAttribute("title", "Liste des recherches");
		
		try {
			String search = request.getParameter("search");
			String lang = (String) request.getSession().getAttribute("lang");
			
			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			RelationDAO relationDao = daoFactory.getRelationDAO();
			
			List<Subject> listSubject = subjectDao.search(search, lang);
			List<Relation> listRelation = relationDao.search(search, lang);
			
			request.setAttribute("listSubject", listSubject);
			request.setAttribute("listRelation", listRelation);
			
			request.getRequestDispatcher("/WEB-PAGES/Search/list.jsp").forward(request, response);
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
