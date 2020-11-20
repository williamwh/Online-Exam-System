package filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.Student;

/**
 * Servlet Filter implementation class StudentFilter
 */
@WebFilter("/app/Students/*")
public class StudentFilter implements Filter {

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        
    	HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        
        String requestPath = request.getRequestURI();
        System.out.println(requestPath);
        System.out.println(session.getAttribute("user"));
        
        if (session == null || session.getAttribute("user") == null || !(session.getAttribute("user") instanceof Student)) {
            response.sendRedirect(request.getContextPath() + "/error.jsp"); // No logged-in admin user found, so redirect to login page.
        } else {
            chain.doFilter(req, res); // Logged-in admin user found, so just continue request.
        }    
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
