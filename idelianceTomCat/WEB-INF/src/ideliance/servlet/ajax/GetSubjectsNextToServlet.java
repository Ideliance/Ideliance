package ideliance.servlet.ajax;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.servlet.quoiEntre.KeyGroup;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

@SuppressWarnings("serial")
public class GetSubjectsNextToServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(GetSubjectsNextToServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during GetSubjectNextToServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			ArrayList<KeyGroup> subjectsMatchList = new ArrayList<KeyGroup>();
			
			int subjectId = Integer.parseInt(request.getParameter("subjectId"));
			subjectsMatchList = selectForQuoiEntre(subjectId);
			response.setContentType("text/json");
			response.getWriter().write(toJson(subjectsMatchList));
			} catch (ParseException e) {
			log.error("An error occured", e);
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-PAGES/Other/error.jsp").forward(request, response);
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}
	
	private ArrayList<KeyGroup> selectForQuoiEntre(int subjectId){
		TripletDAO tripletDao = null;
		try {
			tripletDao = daoFactory.getTripletDAO();
		} catch (DAOException e) {
			log.error("Error during QuoiEntreCreateServlet initialization", e);
		}
		ArrayList<ArrayList<Integer>> newList = new ArrayList<ArrayList<Integer>>();
		ArrayList<KeyGroup> toReturn = new ArrayList<KeyGroup>();
		
		try {
			newList = tripletDao.selectForQuoiEntre(subjectId);
			for (ArrayList<Integer> newListElement : newList) {
				toReturn.add(new KeyGroup(newListElement.get(0),newListElement.get(1),(newListElement.get(1)==0)?false:true));
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return toReturn;
	}
	private String toJson(ArrayList<KeyGroup> list)
	{
		int i = 0;
		String jsonRes = "";
		jsonRes += "[";
		for (KeyGroup keyGroup : list) {
			if(i!=0)
				jsonRes += ",";
			i++;
			jsonRes += keyGroup.toJson();
		}
		jsonRes += "]";
		return jsonRes;
	}
}
