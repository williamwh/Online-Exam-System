package mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import datasource.DBConnection;
import domain.QuestionAnswer;

public class QuestionAnswerMapper {
    
    public static int insert(QuestionAnswer questionAnswer) {
    	String findID_sql = "SELECT MAX(student_exam_answers_id) from student_exam_answers;";
    	String sql = "INSERT INTO student_question_answers (student_exam_answers_id, "
    			+ "question_id, student_answer, marks) VALUES (?, ?, ?, ?)";
		try {
			
			Connection conn2 = DBConnection.getDBConnection();
			PreparedStatement insertStatement = conn2.prepareStatement(sql);
			if (questionAnswer.getExamAnswerID() == 0) {
				Connection conn = DBConnection.getDBConnection();
				PreparedStatement findID_Statement = conn.prepareStatement(findID_sql);
				ResultSet rs = findID_Statement.executeQuery();
				rs.next();
				insertStatement.setInt(1, rs.getInt(1));
				conn.close();
			} else {
				insertStatement.setInt(1, questionAnswer.getExamAnswerID());
			}
			insertStatement.setInt(2, questionAnswer.getQuestionID());
			insertStatement.setString(3, questionAnswer.getStudentAnswer());
			insertStatement.setInt(4, questionAnswer.getMarks());
			
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
			conn2.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		return questionAnswer.getQuestionAnswerID();

	}
    
    public static int update(QuestionAnswer questionAnswer, Connection conn) {
    	String sql = "UPDATE student_question_answers SET student_exam_answers_id = ?, question_id = ?, "
    			+ "student_answer = ?, marks = ? WHERE student_question_answers_id = ?";
    	//Connection conn = DBConnection.getDBConnection();
    	try {
			
			PreparedStatement insertStatement = conn.prepareStatement(sql);
			insertStatement.setInt(1, questionAnswer.getExamAnswerID());
			insertStatement.setInt(2, questionAnswer.getQuestionID());
			insertStatement.setString(3, questionAnswer.getStudentAnswer());
			insertStatement.setInt(4, questionAnswer.getMarks());
			insertStatement.setInt(5, questionAnswer.getQuestionAnswerID());
			
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
		return questionAnswer.getQuestionAnswerID();

	}
    
	public static QuestionAnswer getQuestionAnswer(int examAnswerID, int questionID) {
    	String sql = "SELECT * FROM student_question_answers WHERE student_exam_answers_id = ? AND question_id = ?";
        try {
        	Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(sql);
        	stmt.setInt(1, examAnswerID);
        	stmt.setInt(2, questionID);
        	ResultSet rs = stmt.executeQuery();
        	conn.close();
			while (rs.next()) {
				int questionAnswerID = rs.getInt(1);
				int exam_AnswerID = rs.getInt(2);
				int question_ID = rs.getInt(3);
				String answer = rs.getString(4);
				int marks = rs.getInt(5);
				return new QuestionAnswer(questionAnswerID, exam_AnswerID, question_ID, answer, marks);
			}
		} catch (SQLException e) {
	
		}
        return null;
    }
	
	public static boolean studentFinishedExam(int studentExamAnswerID) {
    	String sql = "SELECT * FROM student_question_answers WHERE student_exam_answers_id = ?";
        try {
        	Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(sql);
        	stmt.setInt(1, studentExamAnswerID);
        	ResultSet rs = stmt.executeQuery();
        	conn.close();
        	if (rs.next()) {
        		// has entry
				return true;
			}
			
		} catch (SQLException e) {
	
		}
        return false;
    }
}
