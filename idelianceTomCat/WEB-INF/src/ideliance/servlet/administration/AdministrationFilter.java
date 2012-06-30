package ideliance.servlet.administration;

import ideliance.core.object.type.UserLevel;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdministrationFilter implements Filter {

//	private FilterConfig config;

	public void init(FilterConfig config) throws ServletException {
//		this.config = config;
	}
	
	public void destroy() {	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		UserLevel level = (UserLevel) session.getAttribute("level");
    	
		if (level == null || level != UserLevel.ADMINISTRATOR) {
			((HttpServletResponse)response).sendRedirect("/");
		} else {
			chain.doFilter(request, response);
		}
	}
}