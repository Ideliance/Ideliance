package ideliance.servlet.triplet;

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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class TripletAddServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(TripletAddServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during TripletAddServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String title = "Modifier un triplet";
		
		try {
			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			RelationDAO relationDao = daoFactory.getRelationDAO();
			TripletDAO tripletDao = daoFactory.getTripletDAO();
			
			List<Subject> listSubject = subjectDao.selectAll(false);
			List<Relation> listRelation = relationDao.selectAll(false);
			List<Triplet> listTriplet = tripletDao.selectAll(false);

			request.setAttribute("listSubject", listSubject);
			request.setAttribute("listRelation", listRelation);
			request.setAttribute("listTriplet", listTriplet);
			
			int id = Integer.parseInt(request.getParameter("id"));
			
			Triplet t = tripletDao.selectSingle(id);
			
			request.setAttribute("triplet", t);
		} catch(NumberFormatException e) {
			title = "Ajouter un triplet";
		} catch (DAOException e) {
			log.error("An error occured", e);
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
			return;
		}
		
		// Set page title
		request.setAttribute("title", title);
		
		request.getRequestDispatcher("/WEB-PAGES/Triplet/addTriplet.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String root = (String) request.getSession().getAttribute("root");
		
		try {
			if (request.getParameter("id") != null) {
				update(request);
			} else {
				add(request);
			}
			
			response.sendRedirect(root + "triplet/list");
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
	
	private void add(HttpServletRequest request) throws DAOException, NumberFormatException {
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		HttpSession session = request.getSession();
		
		Triplet t = new Triplet();
		
		// Select the relation from the database
		int relationId = Integer.parseInt(request.getParameter("relation"));
		
		Relation r = relationDao.selectSingle(relationId, false);
		t.setRelation(r);
		
		try {
			int subjectId = Integer.parseInt(request.getParameter("subjectSubject"));
			
			Subject s = subjectDao.selectSingle(subjectId);
			t.setSSubject(s);
		} catch (NumberFormatException e) {
			int tripletId = Integer.parseInt(request.getParameter("subjectTriplet"));
			
			Triplet t2 = tripletDao.selectSingle(tripletId);
			t.setTSubject(t2);
		}
		
		try {
			int subjectId = Integer.parseInt(request.getParameter("complementSubject"));
			
			Subject s = subjectDao.selectSingle(subjectId);
			t.setSComplement(s);
		} catch (NumberFormatException e) {
			int tripletId = Integer.parseInt(request.getParameter("complementTriplet"));
			
			Triplet t2 = tripletDao.selectSingle(tripletId);
			t.setTComplement(t2);
		}
		
		t.setAuthorCreation((String) session.getAttribute("login"));
		t.setAuthorModification((String) session.getAttribute("login"));
		
		tripletDao.add(t);
	}
	
	private void update(HttpServletRequest request) throws DAOException {
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		HttpSession session = request.getSession();
		
		int id = Integer.parseInt(request.getParameter("id"));
		
		Triplet t = tripletDao.selectSingle(id);
		
		// Select the relation from the database
		int relationId = Integer.parseInt(request.getParameter("relation"));
		
		Relation r = relationDao.selectSingle(relationId, false);
		t.setRelation(r);
		
		try {
			int subjectId = Integer.parseInt(request.getParameter("subjectSubject"));
			
			Subject s = subjectDao.selectSingle(subjectId);
			t.setSSubject(s);
		} catch (NumberFormatException e) {
			int tripletId = Integer.parseInt(request.getParameter("subjectTriplet"));
			
			Triplet t2 = tripletDao.selectSingle(tripletId);
			t.setTSubject(t2);
		}
		
		try {
			int subjectId = Integer.parseInt(request.getParameter("complementSubject"));
			
			Subject s = subjectDao.selectSingle(subjectId);
			t.setSComplement(s);
		} catch (NumberFormatException e) {
			int tripletId = Integer.parseInt(request.getParameter("complementTriplet"));
			
			Triplet t2 = tripletDao.selectSingle(tripletId);
			t.setTComplement(t2);
		}
		
		t.setAuthorModification((String) session.getAttribute("login"));
		t.setDateModification(MyTimestamp.getCurrentTimestamp().getTime());
		
		tripletDao.update(t);
	}
}
