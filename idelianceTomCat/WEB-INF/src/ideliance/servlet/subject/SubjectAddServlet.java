package ideliance.servlet.subject;

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
import ideliance.core.object.type.TripletType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class SubjectAddServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(SubjectAddServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during SubjectAddServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String title = "Modifier un sujet";
		
		try {
			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			RelationDAO relationDao = daoFactory.getRelationDAO();
			TripletDAO tripletDao = daoFactory.getTripletDAO();
			
			Subject sCategory = subjectDao.selectSystem("CATEGORY");
			Relation rCategory = relationDao.selectSystem("ISA");
			
			List<Triplet> listTriplet = tripletDao.selectAll(-1, rCategory.getId(), sCategory.getId(), TripletType.SUBJET, TripletType.SUBJET, false, true);
			
			request.setAttribute("listCategory", listTriplet);

			try {
				int id = Integer.parseInt(request.getParameter("id"));
				
				Subject s = subjectDao.selectSingle(id);
				
				rCategory = relationDao.selectSystem("IN CATEGORY");
				
				listTriplet = tripletDao.selectAll(id, rCategory.getId(), -1, TripletType.SUBJET, TripletType.SUBJET, false, true);
				
				List<Integer> selectCategories = new ArrayList<Integer>();
				
				for (Triplet t : listTriplet) {
					selectCategories.add(t.getSComplement().getId());
				}
				
				request.setAttribute("selectCategories", selectCategories);
				request.setAttribute("subject", s);
			} catch (NumberFormatException e) {
				title = "Ajouter un sujet";
			}
			
			try {
				int idCategory = Integer.parseInt(request.getParameter("category"));
				
				Subject category = subjectDao.selectSingle(idCategory);
				
				request.setAttribute("category", category);
			} catch (NumberFormatException e) {
				
			}
		} catch (DAOException e) {
			log.error("An error occured", e);
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
			return;
		}
		
		// Set page title
		request.setAttribute("title", title);
		
		request.getRequestDispatcher("/WEB-PAGES/Subject/addSubject.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String root = (String) request.getSession().getAttribute("root");
		
		try {
			if (request.getParameter("id") != null) {
				update(request);
				
				response.sendRedirect(root + "subject/view?id=" + request.getParameter("id") + "&type=" + TripletType.SUBJET.getValue());
			} else {
				add(request);
				
				response.sendRedirect(root + "subject/list");
			}
		} catch (NumberFormatException e) {
			log.error("Id conversion failed", e);
			
			request.setAttribute("error", "Id conversion failed : " + e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
		} catch (DAOException e) {
			log.error("An error occured", e);
			
			request.setAttribute("error", "An error occurred : " + e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
		}
	}
	
	private void add(HttpServletRequest request) throws DAOException {
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		
		HttpSession session = request.getSession();
		
		Subject s = new Subject();
		s.setEntitled((String) session.getAttribute("lang"), request.getParameter("entitled"));
		s.setFreeText((String) session.getAttribute("lang"), request.getParameter("text"));
		s.setAuthorCreation((String) session.getAttribute("login"));
		s.setAuthorModification((String) session.getAttribute("login"));
		
		s = subjectDao.add(s);
		
		try {
			addCategories(request, s);
		} catch (NumberFormatException e) {}
	}
	
	private void update(HttpServletRequest request) throws DAOException {
		int id = Integer.parseInt(request.getParameter("id"));
		
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		HttpSession session = request.getSession();
		
		Subject s = subjectDao.selectSingle(id);
		s.setEntitled((String) request.getSession().getAttribute("lang"), request.getParameter("entitled"));
		s.setFreeText((String) request.getSession().getAttribute("lang"), request.getParameter("text"));
		s.setAuthorModification((String) session.getAttribute("login"));
		s.setDateModification(MyTimestamp.getCurrentTimestamp().getTime());
		
		subjectDao.update(s);
		
		Relation rCategory = relationDao.selectSystem("IN CATEGORY");
		
		tripletDao.deleteByAll(id, rCategory.getId(), -1, TripletType.SUBJET, TripletType.NONE);
		
		try {
			addCategories(request, s);
		} catch (NumberFormatException e) {}
	}
	
	private void addCategories(HttpServletRequest request, Subject s) throws DAOException, NumberFormatException {
		HttpSession session = request.getSession();
		
		String [] categories = request.getParameterMap().get("category[]");
		
		if (categories == null) {
			return;
		}
		
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		Relation rCategory = relationDao.selectSystem("IN CATEGORY");
		
		for (String strId : categories) {
			int idCategory = Integer.parseInt(strId);
			
			Subject sCategory = subjectDao.selectSingle(idCategory);
			
			Triplet t = new Triplet();
			t.setSSubject(s);
			t.setRelation(rCategory);
			t.setSComplement(sCategory);
			t.setAuthorCreation((String) session.getAttribute("login"));
			t.setAuthorModification((String) session.getAttribute("login"));
			t.setIsSystem(true);
			
			tripletDao.add(t);
		}
	}
}
