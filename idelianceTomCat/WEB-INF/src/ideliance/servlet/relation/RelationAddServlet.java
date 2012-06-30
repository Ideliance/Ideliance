package ideliance.servlet.relation;

import ideliance.core.config.ApplicationContext;
import ideliance.core.config.MyTimestamp;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class RelationAddServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(RelationAddServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during RelationAddServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String title = "Modifier une relation";
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			RelationDAO relationDao = daoFactory.getRelationDAO();
			
			Relation r = relationDao.selectSingle(id);
			
			request.setAttribute("relation", r);
		} catch(NumberFormatException e) {
			title = "Ajouter une relation";
		} catch (DAOException e) {
			log.error("An error occured", e);
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
			return;
		}
		
		// Set page title
		request.setAttribute("title", title);
		
		request.getRequestDispatcher("/WEB-PAGES/Relation/addRelation.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String root = (String) request.getSession().getAttribute("root");
		
		try {
			if (request.getParameter("id") != null) {
				update(request);
			} else {
				add(request);
			}
			
			if (request.getParameter("ajax") == null) {
				response.sendRedirect(root + "relation/list");
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
	
	private void add(HttpServletRequest request) throws DAOException {
		RelationDAO relationDao = daoFactory.getRelationDAO();
		
		HttpSession session = request.getSession();
		
		Relation r1 = new Relation();
		r1.setEntitled((String) session.getAttribute("lang"), request.getParameter("entitled1"));
		r1.setAuthorCreation((String) session.getAttribute("login"));
		r1.setAuthorModification((String) session.getAttribute("login"));
		
		if (request.getParameter("entitled1").equals(request.getParameter("entitled2")) || request.getParameter("entitled2").equals("")) {
			relationDao.addSymetrique(r1);
		} else {
			Relation r2 = new Relation();
			r2.setEntitled((String) session.getAttribute("lang"), request.getParameter("entitled2"));
			r2.setAuthorCreation((String) session.getAttribute("login"));
			r2.setAuthorModification((String) session.getAttribute("login"));
			
			r1.setInverse(r2);
			r2.setInverse(r1);
			
			relationDao.addWithInverse(r1);
		}
	}
	
	private void update(HttpServletRequest request) throws DAOException {
		int id = Integer.parseInt(request.getParameter("id"));
		
		RelationDAO relationDao = daoFactory.getRelationDAO();
		
		HttpSession session = request.getSession();
		
		Relation r1 = relationDao.selectSingle(id);
		r1.setEntitled((String) session.getAttribute("lang"), request.getParameter("entitled1"));
		r1.setAuthorModification((String) session.getAttribute("login"));
		r1.setDateModification(MyTimestamp.getCurrentTimestamp().getTime());
		
		if (r1.getInverse() == null && (request.getParameter("entitled2").equals("") || request.getParameter("entitled2").equals(request.getParameter("entitled1")))) {
			relationDao.update(r1);
		} else if (r1.getInverse() == null && !request.getParameter("entitled2").equals("")) {
			Relation r2 = new Relation();
			r2.setEntitled((String) session.getAttribute("lang"), request.getParameter("entitled2"));
			r2.setInverse(r1);
			r2.setAuthorCreation((String) session.getAttribute("login"));
			r2.setAuthorModification((String) session.getAttribute("login"));
			
			r2 = relationDao.addWithoutInverse(r2);
			r1.setInverse(r2);
			relationDao.update(r1);
		} else if (r1.getInverse() != null && request.getParameter("entitled2").equals("")) {
			Relation r2 = relationDao.selectSingle(r1.getInverse().getId());
			r2.setInverse(null);
			
			r1.setInverse(null);
			
			relationDao.update(r1);
			relationDao.delete(r2);
		} else {
			Relation r2 = r1.getInverse();
			r2.setEntitled((String) session.getAttribute("lang"), request.getParameter("entitled2"));
			r2.setAuthorModification((String) session.getAttribute("login"));
			r2.setDateModification(MyTimestamp.getCurrentTimestamp().getTime());
			
			relationDao.update(r1);
			relationDao.update(r2);
		}
	}
}
