package ideliance.servlet.category;

import ideliance.core.config.ApplicationContext;
import ideliance.core.config.MyTimestamp;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;
import ideliance.core.object.Subject;
import ideliance.core.object.Triplet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class CategoryAddServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(CategoryAddServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during CategoryAddServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String title = "Modifier une catégorie";
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			
			Subject category = subjectDao.selectSingle(id);
			
			request.setAttribute("category", category);
		} catch(NumberFormatException e) {
			title = "Ajouter une catégorie";
		} catch (DAOException e) {
			log.error("An error occured", e);
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
			return;
		}
		
		// Set page title
		request.setAttribute("title", title);
		
		request.getRequestDispatcher("/WEB-PAGES/Category/addCategory.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String root = (String) request.getSession().getAttribute("root");
		
		try {
			if (request.getParameter("id") != null) {
				update(request);
				
				response.sendRedirect(root +"category/view?id=" + request.getParameter("id"));
			} else {
				int id = add(request);
				
				response.sendRedirect(root + "category/view?id=" + id);
			}
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
	private int add(HttpServletRequest request) throws DAOException {
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		HttpSession session = request.getSession();

		Subject s = new Subject();
		s.setEntitled((String) session.getAttribute("lang"), request.getParameter("categoryEntitled"));
		s.setAuthorCreation((String) session.getAttribute("login"));
		s.setAuthorModification((String) session.getAttribute("login"));
		
		s = subjectDao.add(s);
		
		Relation rIs = relationDao.selectSystem("ISA");
		Subject sCategory = subjectDao.selectSystem("CATEGORY");
		
		Triplet t = new Triplet();
		
		t.setIsSystem(true);
		t.setRelation(rIs);
		t.setSSubject(s);
		t.setSComplement(sCategory);
		t.setAuthorCreation((String) session.getAttribute("login"));
		t.setAuthorModification((String) session.getAttribute("login"));
		
		tripletDao.add(t);
		
		return s.getId();
	}
	
	private void update(HttpServletRequest request) throws DAOException {
		int id = Integer.parseInt(request.getParameter("id"));
		
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		
		HttpSession session = request.getSession();
		
		Subject category = subjectDao.selectSingle(id);
		category.setEntitled((String) request.getSession().getAttribute("lang"), request.getParameter("entitled"));
		category.setAuthorModification((String) session.getAttribute("login"));
		category.setDateModification(MyTimestamp.getCurrentTimestamp().getTime());
		
		subjectDao.update(category);
	}
}