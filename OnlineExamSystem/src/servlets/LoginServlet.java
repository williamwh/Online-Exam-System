package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.Instructor;
import domain.Student;
import domain.User;
import mappers.UserMapper;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 0;
    private UserMapper userMapper = new UserMapper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	     response.setContentType("text/html");
	     System.out.println("Hello from GET method in LoginServlet");
	     String user = request.getParameter("userName");
	     String pass = request.getParameter("passWord");
	     PrintWriter writer = response.getWriter();
	     writer.println("<h3> Hello from Get " +user+" "+pass+"</h3>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		request.setAttribute("validUser", null);
		System.out.println("Hello from Post method in LoginServlet");
		String username = request.getParameter("userName");
		String pass = request.getParameter("passWord");
		
		User user = userMapper.findWithPassword(username, pass);
		String destPage = "LoginForm.jsp";
		
		if(user != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("user", user);
			session.setAttribute("role", user.getRole());
			session.setAttribute("username", user.getUsername());
			destPage = "app/";
			// Checks what role the user has, and redirects them to the appropriate page.
			if(user instanceof Student) {
				destPage += "Students/";
			}
			else if (user instanceof Instructor) {
				destPage += "Instructors/";
			}
			else {
				destPage += "Admins/";
			}
			destPage += "subjects.jsp";
		} else {
			String message = "Invalid username/password";
			request.setAttribute("message", message);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
		dispatcher.forward(request, response);
	}
	

}
