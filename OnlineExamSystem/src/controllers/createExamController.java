package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import UnitOfWork.UnitOfWork;
import domain.Choice;
import domain.Exam;
import domain.Question;
import domain.Subject;
import lockManager.LockManager;
import mappers.ExamMapper;
import mappers.QuestionMapper;

public class createExamController extends HttpServlet {
	ExamMapper examMapper = new ExamMapper();
	QuestionMapper questionMapper = new QuestionMapper();
	private static final long serialVersionUID = 123456;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public createExamController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String view = "/app/Instructors/Exams.jsp";
		ServletContext servletContext = getServletContext();
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
		requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String modifiedBy = request.getParameter("modifiedBy");
		boolean hasLock = false;
		while (!hasLock) {
			hasLock = LockManager.acquireLock(1, modifiedBy);
		}
		// Create Exam
		String subjectCode = request.getParameter("subjectCode");
		String examName = request.getParameter("examName");
		Boolean isPublished = Boolean.parseBoolean(request.getParameter("isPublished"));
		int timeAllowed = Integer.parseInt(request.getParameter("timeAllowed"));
		Exam exam = new Exam(subjectCode, examName, isPublished, timeAllowed, modifiedBy);
		
		UnitOfWork.newCurrent();
		UnitOfWork.getCurrent().registerNew(exam);
		
		
		// insert each question to exam
		int amountOfQuestions = Integer.parseInt(request.getParameter("amountOfQuestions"));
		for (int i = 1; i <= amountOfQuestions; i++) {
//			int examId = Integer.parseInt(request.getParameter("examId" + i)); 
	        int questionIndex = Integer.parseInt(request.getParameter("questionIndex" + i));
	        String questionText = request.getParameter("questionText" + i);
	        int points = Integer.parseInt(request.getParameter("points" + i));
	        String questionType = request.getParameter("questionType" + i);
	        System.out.println(questionType);        
	        Question question = new Question(-1, questionIndex, questionText, points, questionType);
	        UnitOfWork.getCurrent().registerNew(question);
	        // Insert each choice to question
	        if (questionType.equals("MC")) {
	        	System.out.println("Question " + i + " is a MC question");
	        	int amountOfChoices = Integer.parseInt(request.getParameter("q" + i + "AmountOfChoices"));
	        	for (int j = 1; j <= amountOfChoices; j++) {
	        		int questionId = Integer.parseInt(request.getParameter("questionId" + i + "Choice" + j));
	        		int choiceIndex = j; // May need to send a parameter for editing functionality
	        		String choiceText = request.getParameter("q" + i + "Choice" + j);
	        		Choice choice = new Choice(questionId, choiceIndex, choiceText);
	        		UnitOfWork.getCurrent().registerNew(choice);
	        	}
	        } else {
//	        	System.out.println("Question " + i + " is a Short question");
	        }
		}
		
		
		
		PrintWriter writer = response.getWriter();
		try {
			UnitOfWork.getCurrent().commit();
			writer.println("success");
		} catch (Exception e) {
			e.printStackTrace();
			writer.println(e.getMessage());
		}
		
		LockManager.releaseLock(1, modifiedBy);


//        String view = "/app/Instructors/Exams.jsp";
//        ServletContext servletContext = getServletContext();
//        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
//        requestDispatcher.forward(request, response);
	}
}
