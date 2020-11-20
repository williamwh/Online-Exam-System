package controllers;

import java.io.IOException;
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
import domain.ExamAnswer;
import domain.Question;
import domain.QuestionAnswer;
import domain.Student;
import domain.Subject;
import lockManager.LockManager;
import mappers.ChoiceMapper;
import mappers.ExamMapper;
import mappers.QuestionAnswerMapper;
import mappers.QuestionMapper;
import mappers.StudentSubMapper;
import mappers.ExamAnswerMapper;

public class updateExamController extends HttpServlet {
	ExamMapper examMapper = new ExamMapper();
	QuestionMapper questionMapper = new QuestionMapper();
	private static final long serialVersionUID = 123456;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updateExamController() {
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
		System.out.println("UpdateExamController");
		String modifiedBy = request.getParameter("modifiedBy");
		boolean hasLock = false;
		while (!hasLock) {
			hasLock = LockManager.acquireLock(1, modifiedBy);
		}
		UnitOfWork.newCurrent();
		int examId = Integer.parseInt(request.getParameter("examID"));
		String isPublished = request.getParameter("isPublished");
		String isClosed = request.getParameter("isClosed");
		String isDelete = request.getParameter("isDelete");
		Exam exam = ExamMapper.getExam(examId);
		
		
		if (isPublished.equals("1")){
			exam.setIsPublishedVar(true);
			UnitOfWork.getCurrent().registerDirty(exam);
		} else if (isClosed.equals("1")) {
			exam.setIsClosedVar(true);
			UnitOfWork.getCurrent().registerDirty(exam);
			
			List<Question> questions = exam.getQuestions();
			
			for (Student student : StudentSubMapper.findStudents(exam.getSubjectCode())) {
				
				List<ExamAnswer> examAnswers = ExamAnswerMapper.getExamAnswers(exam.getExamID(), student.getUsername());
				if (examAnswers.size() == 0) {
					ExamAnswer examAnswer = new ExamAnswer(0, exam.getExamID(), student.getUsername(), false, 0, null, "");
					UnitOfWork.getCurrent().registerNew(examAnswer);

					for (Question question : questions) {
						QuestionAnswer questionAnswer = new QuestionAnswer(0, 0, question.getQuestionID(), "", 0);
						UnitOfWork.getCurrent().registerNew(questionAnswer);
					}
				} else if (!QuestionAnswerMapper.studentFinishedExam(examId) && examAnswers.size() != 0) {
					int studentExamAnswerID = ExamAnswerMapper.findStudentExamAnswerID(student.getUsername(), examId);
					for (Question question : questions) {
						QuestionAnswer questionAnswer = new QuestionAnswer(0, studentExamAnswerID, question.getQuestionID(), "", 0);
						UnitOfWork.getCurrent().registerNew(questionAnswer);
					}
				}
			}	
			
		} else if (isDelete.equals("1")) {
			UnitOfWork.getCurrent().registerDeleted(exam);
		}
		
		try {
			UnitOfWork.getCurrent().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LockManager.releaseLock(1, modifiedBy);
		String view = "/app/Instructors/Exams.jsp";
		ServletContext servletContext = getServletContext();
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
		requestDispatcher.forward(request, response);
	}
}
