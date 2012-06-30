package ideliance.servlet.quoiEntre;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.exception.DAOException;
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

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class QuoiEntreDisplayServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(QuoiEntrePreDisplayServlet.class);

	public void init() {

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String root = (String) request.getSession().getAttribute("root");
		
		// Set page title
		request.setAttribute("title", "Visualisation");
		
		/*
		 * This page can display a graph for quoiEntre OR subject visuailisation.
		 * So we check is there is a quoiEntre table in session or a subject id in param
		 */
		if(request.getSession().getAttribute("quoiEntreResults") != null){
			//Send quoiEnte's datas to view page delete it
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<KeyGroup>> quoiEntreResults = (ArrayList<ArrayList<KeyGroup>>) request.getSession().getAttribute("quoiEntreResults");
			request.getSession().removeAttribute("quoiEntreResults");
			request.setAttribute("resultsTable", quoiEntreResults);
		}else if(request.getParameter("id") != null){
			//Get all subjects linked to the subject id and put it in request
			String lang = (String) request.getSession().getAttribute("lang");
			int id = Integer.parseInt(request.getParameter("id"));
			int flag = Integer.parseInt(request.getParameter("type"));

			DAOFactory daoFactory = null;
			ApplicationContext context = ApplicationContext.getInstance();
			
			try {
				daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
			} catch (DAOException e) {
				log.error("Error during SubjectViewServlet initialization", e);
			}
			
			// TODO : ID INEXISTING
			SubjectDAO subjectDao;
			try {
				subjectDao = daoFactory.getSubjectDAO();
			
				Subject s = subjectDao.selectSingle(id);
				
				s.getFreeText(lang).replaceAll("\n", "<br/>");
				
				TripletType type = TripletType.fromInt(flag);
				
				TripletDAO tripletDao = daoFactory.getTripletDAO();
				List<Triplet> listTriplet = tripletDao.selectAllBySubject(s.getId(), type, true);
				
				request.setAttribute("resultsTable", formatTripletList(listTriplet));
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			//error : nothing to display
			response.sendRedirect(root);
			return;
		}
		
		request.getRequestDispatcher("/WEB-PAGES/QuoiEntre/display.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}
	/**
	 * format a list of triplet to an Arraylist similar to a quoiEntre result and readable by the jsp
	 * 
	 * @param listTriplets the list of triplet as a List<Triplet>
	 * @return a list of the triplet as a ArrayList<ArrayList<KeyGroup>>
	 */
	private ArrayList<ArrayList<KeyGroup>> formatTripletList(List<Triplet> listTriplets){
		//function to 
		ArrayList<ArrayList<KeyGroup>> totalArrayKeyGroup = new ArrayList<ArrayList<KeyGroup>>();
		for (Triplet triplet : listTriplets) {
			ArrayList<KeyGroup> arrayKeyGroup = new ArrayList<KeyGroup>();
			arrayKeyGroup.add(new KeyGroup(triplet.getSSubject().getId(),-1,false));
			arrayKeyGroup.add(new KeyGroup(triplet.getSComplement().getId(),triplet.getRelation().getId(),false));
			totalArrayKeyGroup.add(arrayKeyGroup);
		}
		return totalArrayKeyGroup;
	}
}