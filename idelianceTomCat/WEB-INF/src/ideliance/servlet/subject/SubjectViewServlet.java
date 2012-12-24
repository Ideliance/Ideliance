package ideliance.servlet.subject;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Subject;
import ideliance.core.object.Triplet;
import ideliance.core.object.type.TripletType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class SubjectViewServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(SubjectViewServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during SubjectViewServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String root = (String) request.getSession().getAttribute("root");
		
		try {
			String lang = (String) request.getSession().getAttribute("lang");
			
			int id = Integer.parseInt(request.getParameter("id"));
			int flag = Integer.parseInt(request.getParameter("type"));

			// TODO A GERER : ID INEXISTANT
			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			Subject s = subjectDao.selectSingle(id);
			
			s.getFreeText(lang).replaceAll("\n", "<br/>");
			
			TripletType type = TripletType.fromInt(flag);
			
			TripletDAO tripletDao = daoFactory.getTripletDAO();
			List<Triplet> listTriplet = tripletDao.selectAllBySubject(s.getId(), type, true);
			
			Map<Integer, Integer> listSimplification = new HashMap<Integer, Integer>();
			
			for (Triplet t : listTriplet) {
				Integer nb = listSimplification.get(t.getRelation().getId());
				
				if (nb != null) {
					nb++;
				} else {
					nb = 1;
				}
				
				listSimplification.put(t.getRelation().getId(), nb);
			}

			//List<Subject> listSubject = subjectDao.selectAll();
			
			request.setAttribute("subject", s);
			request.setAttribute("subjectFlag", flag);
			request.setAttribute("listSimplification", listSimplification);
			request.setAttribute("listTriplet", listTriplet);
			
			//set the attribute for visualisation
			request.getSession().setAttribute("subjectToDisplay",listTriplet);
			System.out.println(listTriplet); 
			
			
			
			request.getRequestDispatcher("/WEB-PAGES/Subject/viewSubject.jsp").forward(request, response);
		} catch(NumberFormatException e) {
			response.sendRedirect(root);
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
