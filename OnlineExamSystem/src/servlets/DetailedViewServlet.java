package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import UnitOfWork.UnitOfWork;

import java.util.Enumeration;
import java.util.List;

import mappers.ExamAnswerMapper;
import mappers.QuestionAnswerMapper;
import domain.ExamAnswer;
import domain.Question;
import domain.QuestionAnswer;

public class DetailedViewServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1152128328615943003L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public DetailedViewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String view = "/app/Instructors/detailedMarkingView.jsp";
	     ServletContext servletContext = getServletContext();
	     RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
	     requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UnitOfWork.newCurrent();
		UnitOfWork uow = UnitOfWork.getCurrent();
		
		HttpSession session = request.getSession(false);
		List<Question> questions = (List<Question>) session.getAttribute("questions");
		int examAnswerID = (int) session.getAttribute("examAnswerID");
		ExamAnswer examAnswer = ExamAnswerMapper.getExamAnswer(examAnswerID);
		examAnswer.setTotalMarks(0);
		examAnswer.setComment(request.getParameter("comment"));
		
//		ExamAnswerMapper.update(examAnswer);
		uow.registerDirty(examAnswer);
		for (Question question : questions) {
			QuestionAnswer questionAnswer = QuestionAnswerMapper.getQuestionAnswer(examAnswerID, question.getQuestionID());
			int mark = Integer.parseInt(request.getParameter(Integer.toString(questionAnswer.getQuestionAnswerID())));
			questionAnswer.setMarks(mark);
			uow.registerDirty(questionAnswer);
//			QuestionAnswerMapper.update(questionAnswer);
		}
		
		try {
			uow.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        String view = "/app/Instructors/tableMarkingView.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
	}

}
