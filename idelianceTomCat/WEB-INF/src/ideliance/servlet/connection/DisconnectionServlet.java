package ideliance.servlet.connection;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DisconnectionServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// Delete cookies
		Cookie [] tCookies = request.getCookies();
			
    	for (Cookie cookie : tCookies) {
    		cookie.setMaxAge(0);
    		cookie.setValue("");
    		response.addCookie(cookie);
    	}
		
		request.getSession().invalidate();
		request.getRequestDispatcher("/WEB-PAGES/Other/connection.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}
}