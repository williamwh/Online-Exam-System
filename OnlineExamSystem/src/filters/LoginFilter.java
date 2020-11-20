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

@WebFilter("/app/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        // If you have any <init-param> in web.xml, then you could get them
        // here by config.getInitParameter("name") and assign it as field.
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        
    	HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
     
        if ((session == null || session.getAttribute("user") == null)/* && needsAuthentication(requestPath)*/) {
            response.sendRedirect(request.getContextPath() + "/LoginForm.jsp"); // No logged-in user found, so redirect to login page.
        } else {
            chain.doFilter(req, res); // Logged-in user found, so just continue request.
        }
    }

    @Override
    public void destroy() {
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }
    
    
    @SuppressWarnings("unused")
	private boolean needsAuthentication(String url) {
    	String [] authorised = {"LoginForm.jsp", "signup.jsp", "images/Uni-of-Melbourne.jpg"};
    	for(String validUrl : authorised) {
    		validUrl = "/OnlinExamBackend/" + validUrl;
    		if(validUrl.equals(url)){
    			return false;
    		}
    	}
    	return true;
    }

}