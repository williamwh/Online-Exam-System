package mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import datasource.DBConnection;
import domain.Exam;
import domain.ExamDateRange;
import domain.Question;

public class ExamMapper {
	private static final String findAllExamsStatement = "SELECT * FROM exams WHERE subject_code = ? ORDER BY exam_id";

	private static final String deleteExamStatement = "DELETE FROM exams WHERE exam_id = ?";

	/*
	 * public String insert(String subjectCode, String examName) { try {
	 * PreparedStatement insertStatement =
	 * conn.prepareStatement(insertExamStatement); insertStatement.setString(1,
	 * subjectCode); insertStatement.setString(2, examName);
	 * 
	 * System.out.println(insertStatement); insertStatement.executeUpdate(); } catch
	 * (SQLException e) { System.out.println(e.getStackTrace());
	 * e.printStackTrace(); } return "Exam: " + examName + " sucessfully created!";
	 * }
	 */
	public static String insert(Exam exam) {
		try {
			String insertExamStatement = "INSERT INTO exams (subject_code, exam_name, is_published, time_allowed, modified_by) VALUES (?, ?, ?, ?, ?)";
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement insertStatement = conn.prepareStatement(insertExamStatement);
			insertStatement.setString(1, exam.getSubjectCode());
			insertStatement.setString(2, exam.getName());
			insertStatement.setBoolean(3, exam.getIsPublishedVar());
			insertStatement.setInt(4, exam.getTimeAllowed());
			insertStatement.setString(5, exam.getModifiedBy());

			System.out.println(insertStatement);
			insertStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
		return "Exam: " + exam.getName() + " sucessfully created!";
	}

	public static List<Exam> getAllExams(String subject) {
		List<Exam> exams = new ArrayList<>();
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement stmt = conn.prepareStatement(findAllExamsStatement);
			stmt.setString(1, subject);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int examID = rs.getInt(1);
				String subjectCode = rs.getString(2);
				String name = rs.getString(3);
				String description = rs.getString(4);
				Timestamp dateCreated = rs.getTimestamp(5);
				Timestamp datePublished = rs.getTimestamp(6);
				Timestamp dateStart = rs.getTimestamp(7);
				Timestamp dateClosed = rs.getTimestamp(8);
				int timeAllowed = rs.getInt(9);
				Boolean isClosed = rs.getBoolean(10);
				Boolean isPublished = rs.getBoolean(11);
				int totalMarks = rs.getInt(12);
				int ver = rs.getInt(13);
				String modifiedBy = rs.getString(14);
				Timestamp modifiedTime = rs.getTimestamp(15);

				ExamDateRange examDateRange = new ExamDateRange(dateCreated, datePublished, dateStart, dateClosed);

				exams.add(new Exam(examID, subjectCode, name, description, timeAllowed, isClosed, isPublished,
						totalMarks, examDateRange, ver, modifiedBy, modifiedTime));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exams;
	}

	public static List<Exam> getSubjectExams(String subject_code) {
		List<Exam> exams = new ArrayList<>();
		String findSubjectExamsStatement = "SELECT * FROM exams WHERE subject_code = ? ORDER BY exam_id";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement stmt = conn.prepareStatement(findSubjectExamsStatement);
			stmt.setString(1, subject_code);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int examID = rs.getInt(1);
				String subjectCode = rs.getString(2);
				String name = rs.getString(3);
				String description = rs.getString(4);
				Timestamp dateCreated = rs.getTimestamp(5);
				Timestamp datePublished = rs.getTimestamp(6);
				Timestamp dateStart = rs.getTimestamp(7);
				Timestamp dateClosed = rs.getTimestamp(8);
				int timeAllowed = rs.getInt(9);
				Boolean isClosed = rs.getBoolean(10);
				Boolean isPublished = rs.getBoolean(11);
				int totalMarks = rs.getInt(12);
				int ver = rs.getInt(13);
				String modifiedBy = rs.getString(14);
				Timestamp modifiedTime = rs.getTimestamp(15);

				ExamDateRange examDateRange = new ExamDateRange(dateCreated, datePublished, dateStart, dateClosed);

				exams.add(new Exam(examID, subjectCode, name, description, timeAllowed, isClosed, isPublished,
						totalMarks, examDateRange, ver, modifiedBy, modifiedTime));
			}
			conn.close();
		} catch (SQLException e) {

		}
		return exams;
	}

	public static Exam getExam(int examID) {
		String sql = "SELECT * FROM exams WHERE exam_id = ?";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, examID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int examId = rs.getInt(1);
				String subjectCode = rs.getString(2);
				String examName = rs.getString(3);
				String examDescription = rs.getString(4);
				Timestamp dateCreated = rs.getTimestamp(5);
				Timestamp datePublished = rs.getTimestamp(6);
				Timestamp dateStart = rs.getTimestamp(7);
				Timestamp dateClosed = rs.getTimestamp(8);
				int timeAllowed = rs.getInt(9);
				Boolean isClosed = rs.getBoolean(10);
				Boolean isPublished = rs.getBoolean(11);
				int totalMarks = rs.getInt(12);
				int ver = rs.getInt(13);
				String modifiedBy = rs.getString(14);
				Timestamp modifiedTime = rs.getTimestamp(15);

				ExamDateRange examDateRange = new ExamDateRange(dateCreated, datePublished, dateStart, dateClosed);

				return new Exam(examId, subjectCode, examName, examDescription, timeAllowed, isClosed, isPublished,
						totalMarks, examDateRange, ver, modifiedBy, modifiedTime);
			}
			conn.close();
		} catch (SQLException e) {

		}
		return null;
	}

	public static void update(Exam exam) throws Exception {
		try {
			String updateExamStatement = "UPDATE exams SET exam_name = ?, exam_description = ?, date_created = ? , date_published = ?, date_start = ?, date_closed = ?, time_allowed = ?, is_closed = ?, is_published = ?, total_marks = ?, ver = ?, modified_by = ?, modified_time = ? WHERE exam_id = ? AND ver = ?";
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement updateStatement = conn.prepareStatement(updateExamStatement);
			updateStatement.setString(1, exam.getName());
			updateStatement.setString(2, exam.getDescription());
			updateStatement.setTimestamp(3, exam.getExamDateRange().getDateCreated());
			updateStatement.setTimestamp(4, exam.getExamDateRange().getDatePublished());
			updateStatement.setTimestamp(5, exam.getExamDateRange().getDateStart());
			updateStatement.setTimestamp(6, exam.getExamDateRange().getDateClosed());
			updateStatement.setInt(7, exam.getTimeAllowed());
			updateStatement.setBoolean(8, exam.getIsClosedVar());
			updateStatement.setBoolean(9, exam.getIsPublishedVar());
			updateStatement.setInt(10, exam.getTotalMarks());
			updateStatement.setInt(11, exam.getVer() + 1);
			updateStatement.setString(12, exam.getModifiedBy());
			updateStatement.setTimestamp(13, exam.getModifiedTime());
			updateStatement.setInt(14, exam.getExamID());
			updateStatement.setInt(15, exam.getVer());
			System.out.println(updateStatement);
			int rowCount = updateStatement.executeUpdate();
			if (rowCount == 0) {
				throwConcurrencyException(exam);
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
	}

	private static void throwConcurrencyException(Exam exam) throws Exception {
		String checkVersionStmt = "SELECT ver, modified_by, modified_time FROM exams WHERE exam_id = ?";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement checkVersionStatement = conn.prepareStatement(checkVersionStmt);
			checkVersionStatement.setInt(1, exam.getExamID());
			ResultSet rs = checkVersionStatement.executeQuery();
			if (rs.next()) {
				int ver = rs.getInt(1);
				String modifiedBy = rs.getString(2);
				Timestamp modifiedTime = rs.getTimestamp(3);
				conn.close();
				if (ver > exam.getVer()) {
					throw new Exception("Exam was already modified by " + modifiedBy + " at " + modifiedTime);
				} else {
					throw new Exception("unexpected error...");
				}
			} else {
				throw new Exception("Exam " + exam.getExamID() + " has been deleted");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Exam exam) {
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement deleteStatement = conn.prepareStatement(deleteExamStatement);

			deleteStatement.setInt(1, exam.getExamID());
			System.out.println(deleteStatement);
			deleteStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
	}

}
