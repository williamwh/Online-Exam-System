package mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datasource.DBConnection;
import domain.Question;

public class QuestionMapper {
	private static final String findAllQuestionsStatement =
			"SELECT * from questions where exam_id = ? ORDER BY question_index";
    private static final String insertQuestionStatement =
			"INSERT INTO questions (exam_id, question_index, question_text, marks, question_type) VALUES (?, ?, ?, ?, ?::question_types)";
    private static final String deleteQuestionStatement =
    		"DELETE FROM questions WHERE question_id = ?";
    private static final String updateQuestionStatement =
    		"UPDATE questions SET question_index = ?, question_text = ?, marks = ?, question_type = ?::question_types WHERE question_id = ?";
    
    public static String insert(Question question) {
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement insertStatement = conn.prepareStatement(insertQuestionStatement);
			
			int examId;
			if (question.getExamId() == -1) {
				Connection conn2 = DBConnection.getDBConnection();
				PreparedStatement lastIdStatement = conn2.prepareStatement("SELECT MAX(exam_id) from exams;");
				ResultSet rs = lastIdStatement.executeQuery();
				rs.next();
				examId = rs.getInt(1);
				conn2.close();
			} else {
				examId = question.getExamId();
			}
			
			insertStatement.setInt(1, examId);
			insertStatement.setInt(2, question.getQuestionIndex());
			insertStatement.setString(3, question.getQuestionText());
			insertStatement.setInt(4, question.getMarks());
			insertStatement.setString(5, question.getQuestionType());
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
		return "Exam: " + question.getExamId() + ", Question: " + question.getQuestionIndex() + " sucessfully added!";
	}
    
    public static List<Question> getAllQuestionsFromExam(int examIdQuery) {
        List<Question> questions = new ArrayList<>();
        try {
        	Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(findAllQuestionsStatement);
        	
        	stmt.setInt(1, examIdQuery);
        	ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int questionId = rs.getInt(1);
				int examId = rs.getInt(2);
				int questionIndex = rs.getInt(3);
				String questionText = rs.getString(4);
				int marks = rs.getInt(5);
				String questionType = rs.getString(6);
				questions.add(new Question(questionId,examId,questionIndex,questionText,marks,questionType));
			}
			conn.close();
		} catch (SQLException e) {
	
		}
        return questions;
    }
    
    public static String delete(Question question) {
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement deleteStatement = conn.prepareStatement(deleteQuestionStatement);

			deleteStatement.setInt(1, question.getQuestionID());
			System.out.println(deleteStatement);
			deleteStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
		return "Sucessfully deleted question Id: " + question.getQuestionID();
	}
    
    public static String update(Question question) {
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement updateStatement = conn.prepareStatement(updateQuestionStatement);
			//"UPDATE questions SET question_index = ?, question_text = ?, marks = ?, question_type = ?::question_types WHERE question_id = ?";
			
			updateStatement.setInt(1, question.getQuestionIndex());
			updateStatement.setString(2, question.getQuestionText());
			updateStatement.setInt(3, question.getMarks());
			updateStatement.setString(4, question.getQuestionType());
			updateStatement.setInt(5, question.getQuestionID());
			System.out.println(updateStatement);
			updateStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
		return "Question: " + question.getQuestionIndex() + " sucessfully updated!";
	}
    
    public static ArrayList<Integer> getAllQuestionIDsFromExam(int examIdQuery) {
    	ArrayList<Integer> questionIDs = new ArrayList<>();
        try {
        	Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(findAllQuestionsStatement);
        	
        	stmt.setInt(1, examIdQuery);
        	ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int questionId = rs.getInt(1);
				questionIDs.add(questionId);
			}
			conn.close();
		} catch (SQLException e) {
	
		}
        return questionIDs;
    }
    
    public static int getTotalQuestions(int examID) {
		int totalQuestions = 0;
		try {
			Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement("Select count(*)  FROM questions WHERE exam_id = " + examID);
        	ResultSet rs = stmt.executeQuery();
			while (rs.next()) {				
				totalQuestions = rs.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalQuestions;
	}
}
