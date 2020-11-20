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
import mappers.ChoiceMapper;
import mappers.ExamAnswerMapper;
import mappers.ExamMapper;
import mappers.QuestionMapper;

public class editExamController extends HttpServlet {
	ExamMapper examMapper = new ExamMapper();
	QuestionMapper questionMapper = new QuestionMapper();
	private static final long serialVersionUID = 123456;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public editExamController() {
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
		PrintWriter writer = response.getWriter();
		int examId = Integer.parseInt(request.getParameter("examId"));
		if (ExamAnswerMapper.getTotalAnswers(examId) > 0){
			writer.println("A student has already started an attempt on this Exam \nPlease save as a new exam.");
			return;
		}
		
		
		String modifiedBy = request.getParameter("modifiedBy");
		boolean hasLock = false;
		while (!hasLock) {
			hasLock = LockManager.acquireLock(1, modifiedBy);
		}
		// Update Exam Name
		Exam exam = ExamMapper.getExam(examId);
		String examName = request.getParameter("examName");
		exam.setName(examName);
		Boolean isPublished = Boolean.parseBoolean(request.getParameter("isPublished"));
		exam.setIsPublishedVar(isPublished);
		int timeAllowed = Integer.parseInt(request.getParameter("timeAllowed"));
		exam.setTimeAllowed(timeAllowed);
		exam.setModifiedBy(modifiedBy);
		int ver = Integer.parseInt(request.getParameter("ver"));
		exam.setVer(ver);
		exam.setModifiedTime();
		UnitOfWork.newCurrent();
		UnitOfWork.getCurrent().registerDirty(exam);
				
		// Find all existing questions (unsued)
		List<Question> questionsInExam = QuestionMapper.getAllQuestionsFromExam(examId);
		List<Integer> questionIdInExamList = new ArrayList<Integer>();
		for (Question questionInExam : questionsInExam) {
			questionIdInExamList.add(questionInExam.getQuestionID());
		}
		
		// Delete questions
		String deletedQuestionsStr = request.getParameter("DeletedQuestions");
		List<Integer> deletedQuestionsIdList = new ArrayList<Integer>();
		if (deletedQuestionsStr != "") {
			var deletedQuestions = deletedQuestionsStr.split(",");
			for (String deletedQuestionIdStr : deletedQuestions) {
				// register delete deletedQuestion ID
				int deletedQuestionId = Integer.parseInt(deletedQuestionIdStr);
				deletedQuestionsIdList.add(deletedQuestionId);
				Question questionForDeletion = new Question(deletedQuestionId);
				UnitOfWork.getCurrent().registerDeleted(questionForDeletion);
			}
		}
		// Remaining questions in exam for updating (unused)
		questionIdInExamList.removeAll(deletedQuestionsIdList);
		
		
		// Add/Update questions to exam
		int amountOfQuestions = Integer.parseInt(request.getParameter("amountOfQuestions"));
		for (int i = 1; i <= amountOfQuestions; i++) {
	        int questionIndex = Integer.parseInt(request.getParameter("questionIndex" + i));
	        String questionText = request.getParameter("questionText" + i);
	        int points = Integer.parseInt(request.getParameter("points" + i));
	        String questionType = request.getParameter("questionType" + i);
	        String questionId = request.getParameter("questionId" + i);

	        if (questionId.equals("-1")){ 
	        	// New Question
	        	System.out.println("new question");
		        Question question = new Question(examId, questionIndex, questionText, points, questionType);
		        UnitOfWork.getCurrent().registerNew(question);
		        if (questionType.equals("MC")) {
		        	// Insert choices to question
		        	int amountOfChoices = Integer.parseInt(request.getParameter("q" + i + "AmountOfChoices"));
		        	for (int j = 1; j <= amountOfChoices; j++) {
		        		int choiceQuestionId = Integer.parseInt(request.getParameter("questionId" + i + "Choice" + j));
		        		int choiceIndex = j; // May need to send a parameter for editing functionality
		        		String choiceText = request.getParameter("q" + i + "Choice" + j);
		        		Choice choice = new Choice(choiceQuestionId, choiceIndex, choiceText);
		        		UnitOfWork.getCurrent().registerNew(choice);
		        	}
		        }
			} else { 
				// Update Existing Question
				int questionIdInt = Integer.parseInt(questionId);
				Question question = new Question(questionIdInt, examId, questionIndex, questionText, points, questionType);
				UnitOfWork.getCurrent().registerDirty(question);
				if (questionType.equals("MC")) {
					// delete all existing choices
					List<Choice> choicesInQuestion = ChoiceMapper.getAllChoicesFromQuestion(questionIdInt);
					for (Choice existingChoice : choicesInQuestion) {
						UnitOfWork.getCurrent().registerDeleted(existingChoice);
					}
					// insert new choices
		        	int amountOfChoices = Integer.parseInt(request.getParameter("q" + i + "AmountOfChoices"));
		        	for (int j = 1; j <= amountOfChoices; j++) {
		        		int choiceIndex = j;
		        		String choiceText = request.getParameter("q" + i + "Choice" + j);
		        		Choice choice = new Choice(questionIdInt, choiceIndex, choiceText);
		        		UnitOfWork.getCurrent().registerNew(choice);
		        	}
		        }
			}
		}
		
			
		
		
			
		try {
			UnitOfWork.getCurrent().commit();
			writer.println("success");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			writer.println(e.getMessage() + "\nPlease save as a new exam.");
		}
		LockManager.releaseLock(1, modifiedBy);
//		String view = "/app/Instructors/Exams.jsp";
//		ServletContext servletContext = getServletContext();
//		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
//		requestDispatcher.forward(request, response);
	}}
