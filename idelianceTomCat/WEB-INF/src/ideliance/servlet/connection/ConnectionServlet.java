package ideliance.servlet.connection;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class ConnectionServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String login = (String) request.getParameter("login");
		String password = (String) request.getParameter("password");
		HttpSession session = request.getSession();
		
		session.setAttribute("login", login);
		session.setAttribute("password", password);
		
		// Auto connection
		if (request.getParameter("auto_connect") != null) {
			Cookie loginCookie = new Cookie("login", login);
			Cookie passwordCookie = new Cookie("password", password);
			
			response.addCookie(loginCookie);
			response.addCookie(passwordCookie);
		}

		request.getRequestDispatcher("/").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}
}