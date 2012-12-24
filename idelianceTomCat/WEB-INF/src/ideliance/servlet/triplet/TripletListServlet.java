package ideliance.servlet.triplet;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Triplet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class TripletListServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(TripletListServlet.class);
	
	private DAOFactory daoFactory = null;
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during TripletListServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setAttribute("title", "Liste des triplets");
		int NumberResultByPage = 30;
		int pageNb;
		try{
			pageNb = Integer.parseInt(request.getParameter("page"));
		} catch(NumberFormatException e) {
			pageNb = 1;
		}
		if(pageNb<=0)
			pageNb = 1;
		request.setAttribute("pageNb", pageNb);
		
		try {
			TripletDAO tripletDao = daoFactory.getTripletDAO();
			
			int pageCount = (int)(tripletDao.countAll()/NumberResultByPage)+1;
			request.setAttribute("pageCount", pageCount);
			
			List<Triplet> listTriplet = tripletDao.selectAll(false, NumberResultByPage, (pageNb-1)*NumberResultByPage);
			request.setAttribute("listTriplet", listTriplet);
			
			request.getRequestDispatcher("/WEB-PAGES/Triplet/listTriplet.jsp").forward(request, response);
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
