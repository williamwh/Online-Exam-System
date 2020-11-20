package controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.Instructor;
import domain.Subject;
import mappers.InstructorMapper;
import mappers.InstructorSubMapper;
import mappers.StudentSubMapper;
import mappers.SubjectMapper;

/**
 * Servlet implementation class addSubjectController
 */
public class addSubjectController extends HttpServlet {
	private static final long serialVersionUID = 1;
    InstructorMapper instructorMapper = new InstructorMapper();
    SubjectMapper subjectMapper = new SubjectMapper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addSubjectController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String view = "/Admin/addSubjects.jsp";
	     ServletContext servletContext = getServletContext();
	     RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
	     requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subjectCode = request.getParameter("code");
        String name = request.getParameter("name");
        String coordinators[] = request.getParameterValues("coordinators");
        //Instructor coordinator = instructorMapper.find(coordinatorID);
        String students[] = request.getParameterValues("students");
        
        Subject subject = new Subject(subjectCode,name);
        
        subjectMapper.insert(subject);
        for(String student : students) {
        	StudentSubMapper.insert(student, subject);
        }
        for(String coordinator : coordinators) {
        	InstructorSubMapper.insert(coordinator, subject);
        }
        String view = "/app/Admins/subjects.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
	}

}
