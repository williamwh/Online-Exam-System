package controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.Exam;
import domain.Question;
import mappers.QuestionMapper;

public class addQuestionController extends HttpServlet {
	private static final long serialVersionUID = 114345;
    QuestionMapper questionMapper = new QuestionMapper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addQuestionController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String view = "/app/Instructor/Exams.jsp";
	     ServletContext servletContext = getServletContext();
	     RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
	     requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int examId = Integer.parseInt(request.getParameter("examId"));
        int questionIndex = Integer.parseInt(request.getParameter("questionIndex"));
        String questionText = request.getParameter("questionText");
        int points = Integer.parseInt(request.getParameter("points"));
        String questionType = request.getParameter("questionType");
        
        
        Question question = new Question(examId, questionIndex, questionText, points, questionType);
        questionMapper.insert(question);


        String view = "/app/Instructor/Exams.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
	}
}

