package mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datasource.DBConnection;
import domain.ExamAnswer;

public class ExamAnswerMapper {
    
    public static int insert(ExamAnswer examAnswer) {
    	String sql = "INSERT INTO student_exam_answers (exam_id, student_username, "
    			+ "is_marked) VALUES (?, ?, ?)";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement insertStatement = conn.prepareStatement(sql);
			insertStatement.setInt(1, examAnswer.getExamID());
			insertStatement.setString(2, examAnswer.getStudentUsername());
			insertStatement.setBoolean(3, examAnswer.getIsMarked());
			
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		return examAnswer.getExamAnswerID();

	}
    
    public static int update(ExamAnswer examAnswer, Connection conn) {
    	String sql = "UPDATE student_exam_answers SET total_marks = ?, marking_comment = ? "
    			+ "WHERE student_exam_answers_id = ?";
    	//Connection conn = DBConnection.getDBConnection();
		try {
			PreparedStatement insertStatement = conn.prepareStatement(sql);
			insertStatement.setInt(1, examAnswer.getTotalMarks());
			insertStatement.setString(2, examAnswer.getComment());
			insertStatement.setInt(3, examAnswer.getExamAnswerID());
			
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
			//conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		return examAnswer.getExamAnswerID();

	}
    
	public static List<ExamAnswer> getExamAnswers(int examID, String studentUsername) {
        List<ExamAnswer> examAnswers = new ArrayList<>();
    	String sql = "SELECT * FROM student_exam_answers WHERE exam_id = ? AND student_username = ?";
        try {
        	Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(sql);
        	stmt.setInt(1, examID);
        	stmt.setString(2, studentUsername);
        	ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int examAnswerID = rs.getInt(1);
				int exam_id = rs.getInt(2);
				String student_username = rs.getString(3);
				boolean is_marked = rs.getBoolean(4);
				int total_marks = rs.getInt(5);
				Timestamp date_created = rs.getTimestamp(6);
				String comment = rs.getString(7);

				examAnswers.add(new ExamAnswer(examAnswerID, exam_id, student_username, 
						is_marked, total_marks, date_created, comment));
			}
			conn.close();
		} catch (SQLException e) {
	
		}
        return examAnswers;
    }
	
	public static ExamAnswer getExamAnswer(int id) {
		String sql = "SELECT * FROM student_exam_answers WHERE student_exam_answers_id = ?";
        try {
        	Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(sql);
           	stmt.setInt(1, id);
        	ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int examAnswerID = rs.getInt(1);
				int exam_id = rs.getInt(2);
				String student_username = rs.getString(3);
				boolean is_marked = rs.getBoolean(4);
				int total_marks = rs.getInt(5);
				Timestamp date_created = rs.getTimestamp(6);
				String comment = rs.getString(7);

				return new ExamAnswer(examAnswerID, exam_id, student_username, 
						is_marked,total_marks, date_created, comment);
			}
			conn.close();
		} catch (SQLException e) {
	
		}
        return null;
    }
	
	public static int getTotalAnswers(int examId) {
		String sql = "SELECT COUNT(*) FROM student_exam_answers WHERE exam_id = ?";
		int count = 0;
        try {
        	Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(sql);
           	stmt.setInt(1, examId);
        	ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
	
		}
        System.out.println("ExamAnswerMapper.getTotalAnswers count: " + count);
        return count;
	}
	
	public static int getLatestExam() {
		int studentExamAnswerId = -1;
		String findID_sql = "SELECT MAX(student_exam_answers_id) from student_exam_answers;";
        try {
        	Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(findID_sql);
        	ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				studentExamAnswerId = rs.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
	
		}
        return studentExamAnswerId;
	}
	
	public static int findStudentExamAnswerID(String studentUsername, int examId) {
		int studentExamAnswerId = -1;
		String findID_sql = "SELECT student_exam_answers_id from student_exam_answers WHERE student_username = ? AND exam_id = ?;";
        try {
        	Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(findID_sql);
        	stmt.setString(1, studentUsername);
        	stmt.setInt(2, examId);
        	ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				studentExamAnswerId = rs.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
	
		}
        return studentExamAnswerId;
	}
}
