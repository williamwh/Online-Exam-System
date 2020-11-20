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
import mappers.ExamAnswerMapper;
import domain.ExamAnswer;

public class TableViewServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2862385922607260565L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public TableViewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String view = "/app/Instructors/tableMarkingView.jsp";
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
		
		int mark = Integer.parseInt(request.getParameter("mark"));
		int examAnswerID = Integer.parseInt(request.getParameter("examAnswerID"));

		ExamAnswer examAnswer = ExamAnswerMapper.getExamAnswer(examAnswerID);
		examAnswer.setTotalMarks(mark);
		
//        ExamAnswerMapper.update(examAnswer);
		uow.registerDirty(examAnswer);

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
