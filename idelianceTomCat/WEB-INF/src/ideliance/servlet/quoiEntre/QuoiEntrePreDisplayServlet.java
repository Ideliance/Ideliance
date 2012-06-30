package ideliance.servlet.quoiEntre;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.exception.DAOException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class QuoiEntrePreDisplayServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(QuoiEntrePreDisplayServlet.class);
	
	private DAOFactory daoFactory = null;
	
	private int subjectA;
	private int subjectB;
	private ArrayList<String> listExcludedSubjects = new ArrayList<String>();
	private ArrayList<String> listExcludedRelations = new ArrayList<String>();
	
	public void init() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		try {
			daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
		} catch (DAOException e) {
			log.error("Error during QuoiEntreCreateServlet initialization", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// Set page title
		request.setAttribute("title", "Pre Visualisation");
				
		try {
			ArrayList<ArrayList<KeyGroup>> arrayQuoiEntre = runQuoiEntre(request);
			request.setAttribute("resultsTable", arrayQuoiEntre);
			request.getSession().setAttribute("quoiEntreResults", arrayQuoiEntre);
		} catch (NumberFormatException e) {
			request.getRequestDispatcher("/WEB-PAGES/QuoiEntre/create.jsp").forward(request, response);
		} catch (DAOException e) {
			log.error("Error during QuoiEntreCreateServlet initialization", e);
		}
		request.getRequestDispatcher("/WEB-PAGES/QuoiEntre/preDisplay.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			runQuoiEntre(request);
		} catch (NumberFormatException e) {
			request.getRequestDispatcher("/WEB-PAGES/QuoiEntre/create.jsp").forward(request, response);
		} catch (DAOException e) {
			log.error("Error during QuoiEntreCreateServlet initialization", e);
			e.printStackTrace();
		}
		doGet(request, response);
	}
	private ArrayList<ArrayList<KeyGroup>> runQuoiEntre(HttpServletRequest request) throws DAOException, NumberFormatException {
		ArrayList<ArrayList<KeyGroup>> totalListA = new ArrayList<ArrayList<KeyGroup>>();
		try{
			subjectA = Integer.parseInt(request.getParameter("subjectA")); 
			subjectB = Integer.parseInt(request.getParameter("subjectB"));
			int minNbRelations = Integer.parseInt(request.getParameter("minNbRelations"));
			int maxNbRelations = Integer.parseInt(request.getParameter("maxNbRelations"));
			if(minNbRelations>maxNbRelations)
				minNbRelations 	= maxNbRelations;
			
			String[] excludedSubjects = request.getParameterValues("excludedSubject");
			if(excludedSubjects != null)
				listExcludedSubjects = new ArrayList<String>(Arrays.asList(excludedSubjects));

			String[] excludedRelations = request.getParameterValues("excludedRelation");
			if(excludedRelations != null)
				listExcludedRelations = new ArrayList<String>(Arrays.asList(excludedRelations));

			
			//FOR LIST A
			KeyGroup keyGroupA = new KeyGroup();
			ArrayList<KeyGroup> listA = new ArrayList<KeyGroup>();
			
			keyGroupA.setIdSubject(subjectA);
			listA.add(keyGroupA);
			totalListA.add(listA);

			//FOR LIST B
			ArrayList<ArrayList<List<Integer>>> totalListB = new ArrayList<ArrayList<List<Integer>>>();
			ArrayList<List<Integer>> listB = new ArrayList<List<Integer>>();
			ArrayList<Integer> listSubjectB = new ArrayList<Integer>();
			listSubjectB.add(subjectB);
			listSubjectB.add(0);
			listSubjectB.add(0);
			listB.add(listSubjectB);
			totalListB.add(listB);
			totalListA = constructArray(totalListA,minNbRelations,maxNbRelations,1);
			System.out.println("totalListA : "+totalListA);
			
			maxNbRelations = 0;
			int nbResultats = 0;
			
			//At the end, delete all line NOT containing subject B
			Iterator<ArrayList<KeyGroup>> it = totalListA.iterator();
			while (it.hasNext()) {
				ArrayList<KeyGroup> nextInList = it.next();
			    int lastSubjectId =  (nextInList.get(nextInList.size()-1)).getIdSubject();
			    if(lastSubjectId!=subjectB)
			    	it.remove();
			    else{
			    	maxNbRelations = (maxNbRelations<nextInList.size())?nextInList.size():maxNbRelations;
			    	nbResultats++;
			    }
			}

			request.setAttribute("maxNbRelations", maxNbRelations);
			request.setAttribute("nbResultats", nbResultats);
		} catch(NumberFormatException e){
			return totalListA;
		}
		return totalListA;
		
	}
	@SuppressWarnings("unchecked")
	private ArrayList<ArrayList<KeyGroup>> constructArray(ArrayList<ArrayList<KeyGroup>> mainArray, int levelMin, int levelMax, int currentLevel){
		
		ArrayList<ArrayList<KeyGroup>> finalArray = new ArrayList<ArrayList<KeyGroup>>();

		for (ArrayList<KeyGroup> parentGroup : mainArray) {
			int subjectId = parentGroup.get(parentGroup.size()-1).getIdSubject();
		
			//check if the subject id of the parentGroup is different from objective subjectID
			if(subjectId!=subjectB){
				ArrayList<KeyGroup> newList;
				newList = selectForQuoiEntre(subjectId);
				for (KeyGroup newListElement : newList) {
					if(!listExcludedSubjects.contains(""+newListElement.getIdSubject()) && !listExcludedRelations.contains(""+newListElement.getIdRelation())){
						//Part to  check the presence of newListElement in mainArray
						boolean isNewElmtInMainArray = false;
						for(ArrayList<KeyGroup> childArray : mainArray){
							if(newListElement.getIdSubject() != subjectB && childArray.contains(newListElement)){
								isNewElmtInMainArray = true;
							}
						}
						if(!isNewElmtInMainArray){
							ArrayList<KeyGroup> parentGroupCopy = ((ArrayList<KeyGroup>) parentGroup.clone());
							parentGroupCopy.add(newListElement);
							finalArray.add(parentGroupCopy);
						}
					}
				}
			}else{
				finalArray.add(parentGroup);
			}
		}
		if(currentLevel<levelMax){
			currentLevel++;
			finalArray = constructArray(finalArray,levelMin,levelMax,currentLevel);
		}
		return finalArray;
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
}