package ideliance.servlet.administration;

import ideliance.core.imp.ImportXML;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/administration/import")
@MultipartConfig
public class ImportServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(ImportServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		request.getRequestDispatcher("/WEB-PAGES/Administration/Import/index.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		Part part = request.getPart("file");
		
		if (part == null) {
			doGet(request, response);
		}
		
		log.info("Part : " + part.getSize());
		
		InputStream is = request.getPart(part.getName()).getInputStream();
		
		ImportXML imp = new ImportXML(is);
		
		try {
			imp.load();
			
			imp.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}