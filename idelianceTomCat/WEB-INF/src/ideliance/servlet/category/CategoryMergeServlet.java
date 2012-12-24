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
public class CategoryMergeServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(CategoryMergeServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during CategoryMergeServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			RelationDAO relationDao = daoFactory.getRelationDAO();
			TripletDAO tripletDao = daoFactory.getTripletDAO();
			
			Subject sCategory = subjectDao.selectSystem("CATEGORY");
			Relation rIs = relationDao.selectSystem("ISA");

			List<Triplet> listCategory = tripletDao.selectAll(-1, rIs.getId(), sCategory.getId(), TripletType.SUBJET, TripletType.SUBJET, false, true);
			
			Subject category = subjectDao.selectSingle(id);
			
			request.setAttribute("category", category);
			request.setAttribute("listCategory", listCategory);
			
			request.getRequestDispatcher("/WEB-PAGES/Category/mergeCategory.jsp").forward(request, response);
		} catch(NumberFormatException e) {
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
			int id = Integer.parseInt(request.getParameter("id"));
			int target = Integer.parseInt(request.getParameter("category"));
			
			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			RelationDAO relationDao = daoFactory.getRelationDAO();
			TripletDAO tripletDao = daoFactory.getTripletDAO();
			
			Subject sCategory = subjectDao.selectSingle(id);
			Relation rCategory = relationDao.selectSystem("IN CATEGORY");
			
			List<Triplet> listTriplet = tripletDao.selectAll(-1, rCategory.getId(), target, TripletType.SUBJET, TripletType.SUBJET, false, false);
			
			for (Triplet t : listTriplet) {
				t.setSComplement(sCategory);
				
				tripletDao.update(t);
			}

			tripletDao.deleteBySubject(target, TripletType.SUBJET);
			tripletDao.deleteByComplement(target, TripletType.SUBJET);
			subjectDao.delete(target);
			
			response.sendRedirect(root + "category/view?id=" + id);
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
}