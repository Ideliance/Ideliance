package ideliance.test.main;

import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.DictionaryDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Dictionary;
import ideliance.core.object.type.DictionaryType;

import java.util.Map;
import java.util.Set;

public class TestDictionary {
	
	public static void main(String[] argv) {
		
		try {
			peupler();
			select();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public static void peupler() throws DAOException {
		DictionaryDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getDictionaryDAO();
		
		for (int i = 1; i < 6; i++)
		{
			Dictionary d = new Dictionary();
			
			d.setFk(i + 5);
			d.setType(DictionaryType.SUBJECT_FREE_TEXT);
			d.setLang("en");
			d.setValue("Anglais value " + i);
			
			dao.add(d);
		}
	}
	
	public static void select() throws DAOException {
		DictionaryDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getDictionaryDAO();
		
		Map<String, Dictionary>list = dao.selectByFk(10, DictionaryType.SUBJECT_FREE_TEXT);
		
		Set<String> keys = list.keySet();
		
		for (String key : keys) {
			System.out.println(list.get(key));
		}
	}
}
