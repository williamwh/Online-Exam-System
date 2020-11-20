package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import mappers.ChoiceMapper;
import mappers.ExamAnswerMapper;
import mappers.ExamMapper;
import mappers.QuestionAnswerMapper;
import domain.Choice;
import domain.Exam;
import domain.ExamAnswer;
import domain.Question;
import domain.QuestionAnswer;
import domain.Student;
import lockManager.LockManager;
import UnitOfWork.UnitOfWork;

public class AnswerExamServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public AnswerExamServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String view = "/app/Students/Exams.jsp";
	     ServletContext servletContext = getServletContext();
	     RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
	     requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String studentUsername = (String) session.getAttribute("studentUsername");
		boolean hasLock = false;
		while (!hasLock) {
			hasLock = LockManager.acquireLock(1, studentUsername);
		}
		
		UnitOfWork.newCurrent();
		UnitOfWork uow = UnitOfWork.getCurrent();
		

		int examID = (int) session.getAttribute("examID");
		int studentExamAnswerID = (int) session.getAttribute("studentExamAnswerID");
		Exam exam = ExamMapper.getExam(examID);
		List<Question> questions = exam.getQuestions();
		
		if (!exam.getIsClosedVar()) {
			for (Question question : questions) {

				String answer = request.getParameter(Integer.toString(question.getQuestionID()));
				if (answer == null){
					answer = "";
				}
				QuestionAnswer questionAnswer = new QuestionAnswer(0, studentExamAnswerID, question.getQuestionID(), answer, 0);
				uow.registerNew(questionAnswer);
			}
			
			try {
				uow.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			request.setAttribute("alertMsg", "Cannot attempt! Exam has been closed!");
		}
		
		LockManager.releaseLock(1, studentUsername);
		String view = "/app/Students/Exams.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
	}

}
