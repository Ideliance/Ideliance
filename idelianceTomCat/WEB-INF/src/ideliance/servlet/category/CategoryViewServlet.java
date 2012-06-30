package ideliance.servlet.category;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;
import ideliance.core.object.Subject;
import ideliance.core.object.Triplet;
import ideliance.core.object.type.TripletType;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class CategoryViewServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(CategoryViewServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during CategoryViewServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			int idCategory = Integer.parseInt(request.getParameter("id"));

			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			RelationDAO relationDao = daoFactory.getRelationDAO();
			TripletDAO tripletDao = daoFactory.getTripletDAO();
			
			Subject sCategory = subjectDao.selectSingle(idCategory);
			Relation rCategory = relationDao.selectSystem("IN CATEGORY");
			
			List<Triplet> listTriplet = tripletDao.selectAll(-1, rCategory.getId(), idCategory, TripletType.SUBJET, TripletType.SUBJET, false, true);

			request.setAttribute("category", sCategory);
			request.setAttribute("listTriplet", listTriplet);
			
			request.getRequestDispatcher("/WEB-PAGES/Category/viewCategory.jsp").forward(request, response);
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
		String root = (String) request.getSession().getAttribute("root");
		
		try {
			int idCategory = Integer.parseInt(request.getParameter("id"));
			String [] parameters = request.getParameterMap().get("subject[]");
			
			if (request.getParameter("type").equals("remove")) {
				remove(idCategory, parameters);
			} else if (request.getParameter("type").equals("delete")) {
				delete(parameters);
			}
			
			response.sendRedirect(root + "category/view?id=" + idCategory);
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
	
	public void remove(int idCategory, String [] subjects) throws DAOException, NumberFormatException {
		RelationDAO relationDao = daoFactory.getRelationDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		Relation rCategory = relationDao.selectSystem("IN CATEGORY");
		
		for (String subject : subjects) {
			int id = Integer.parseInt(subject);
			
			tripletDao.deleteByAll(id, rCategory.getId(), idCategory, TripletType.SUBJET, TripletType.SUBJET);
		}
	}
	
	public void delete(String [] subjects) throws DAOException {
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		for (String subject : subjects) {
			int id = Integer.parseInt(subject);
			
			tripletDao.deleteBySubject(id, TripletType.SUBJET);
			tripletDao.deleteByComplement(id, TripletType.SUBJET);
			
			subjectDao.delete(id);
		}
	}
}