package ideliance.servlet.subject;

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
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class SubjectAddTripletServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(SubjectAddTripletServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during SubjectAddTripletServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			int idSubject = Integer.parseInt(request.getParameter("subject"));
			
			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			RelationDAO relationDao = daoFactory.getRelationDAO();
			
			List<Subject> listSubject = subjectDao.selectAll(false);
			List<Relation> listRelation = relationDao.selectAll(false);

			request.setAttribute("listSubject", listSubject);
			request.setAttribute("listRelation", listRelation);
			
			Subject subject = subjectDao.selectSingle(idSubject);
			
			request.setAttribute("subject", subject);
		} catch(NumberFormatException e) {
			log.error("An error occured", e);
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
			return;
		} catch (DAOException e) {
			log.error("An error occured", e);
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
			return;
		}
		
		request.getRequestDispatcher("/WEB-PAGES/Subject/addTriplet.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String root = (String) request.getSession().getAttribute("root");
		
		try {
			int idSubject = Integer.parseInt(request.getParameter("subject"));
			int idRelation = Integer.parseInt(request.getParameter("relation"));
			int idComplement = Integer.parseInt(request.getParameter("complement"));
			
			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			RelationDAO relationDao = daoFactory.getRelationDAO();
			TripletDAO tripletDao = daoFactory.getTripletDAO();
			
			HttpSession session = request.getSession();
			
			Triplet t = new Triplet();
			
			Subject subject = subjectDao.selectSingle(idSubject);
			Relation relation = relationDao.selectSingle(idRelation, false);
			Subject complement = subjectDao.selectSingle(idComplement);

			t.setSSubject(subject);
			t.setRelation(relation);
			t.setSComplement(complement);
			
			t.setAuthorCreation((String) session.getAttribute("login"));
			t.setAuthorModification((String) session.getAttribute("login"));
			
			tripletDao.add(t);
			
			response.sendRedirect(root + "subject/view?id=" + idSubject + "&type=" + TripletType.SUBJET.getValue());
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
