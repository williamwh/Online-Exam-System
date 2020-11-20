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

public class StartAnswerExamServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public StartAnswerExamServlet() {
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
		Student student = (Student) session.getAttribute("user");
		boolean hasLock = false;
		while (!hasLock) {
			hasLock = LockManager.acquireLock(1, student.getUsername());
		}
		
		UnitOfWork.newCurrent();
		UnitOfWork uow = UnitOfWork.getCurrent();
		int studentExamAnswerID;
		
		int examID = Integer.parseInt(request.getParameter("examID"));
		Exam exam = ExamMapper.getExam(examID);

		if (!exam.getIsClosedVar()) {
			ExamAnswer examAnswer = new ExamAnswer(0, exam.getExamID(), student.getUsername(), false, 0, null, "");
			uow.registerNew(examAnswer);
			
			try {
				uow.commit();
				studentExamAnswerID = ExamAnswerMapper.getLatestExam();
				request.setAttribute("studentExamAnswerID", studentExamAnswerID);
				request.setAttribute("examID", examID);
				request.setAttribute("studentUsername", student.getUsername());
				String view = "/app/Students/AnswerExam.jsp";
		        ServletContext servletContext = getServletContext();
		        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
		        requestDispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			request.setAttribute("alertMsg", "Cannot attempt! Exam has been closed!");
			String view = "/app/Students/Exams.jsp";
	        ServletContext servletContext = getServletContext();
	        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
	        requestDispatcher.forward(request, response);
		}
		LockManager.releaseLock(1, student.getUsername());
	}

}
