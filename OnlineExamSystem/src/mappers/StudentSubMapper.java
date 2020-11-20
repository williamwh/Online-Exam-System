package mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datasource.DBConnection;
import domain.Student;
import domain.Subject;

public class StudentSubMapper {

	private SubjectMapper subjectMapper = new SubjectMapper();
	
	public static String insert(Student student, Subject subject) {
		String sql = "INSERT INTO student_subject_mapping (student_username, subject_code)" +
				"VALUES (?, ?)";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement insertStatement = conn.prepareStatement(sql);
			insertStatement.setString(1, student.getUsername());
			insertStatement.setString(2, subject.getSubjectCode());
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student.getUsername() + ", " + subject.getName();
	}
	
	public static String insert(String studentUsername, Subject subject) {
		String sql = "INSERT INTO student_subject_mapping (student_username, subject_code)" +
				"VALUES (?, ?)";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement insertStatement = conn.prepareStatement(sql);
			insertStatement.setString(1, studentUsername);
			insertStatement.setString(2, subject.getSubjectCode());
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return studentUsername + ", " + subject.getName();
	}
	
	public static Student find(String username) {
		Student student = null;
		String sql = "SELECT username, email, password " +
						"FROM students " +
						"WHERE username = ?";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement findStatement = conn.prepareStatement(sql);
			findStatement.setString(1, username);
			System.out.println(findStatement);
			ResultSet result = findStatement.executeQuery();
			if(result.next()) {
				String resultEmail = result.getString(2);
				String resultPassword = result.getString(3);
				student = new Student(username, resultEmail, resultPassword);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student; 
	}
	
	public static List<String> findStudentUsernames(String subjectCode) {
		List<String> students = new ArrayList<String>();
		String sql = "SELECT * FROM student_subject_mapping WHERE subject_code = ?";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, subjectCode);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String username = rs.getString(2);
				students.add(username);
//				String sql2 = "SELECT * FROM students WHERE username = ?";
//				PreparedStatement stmt2 = conn.prepareStatement(sql2);
//				stmt2.setString(1, username);
//				ResultSet rs2 = stmt2.executeQuery();
//				while(rs2.next()) {
//					students.add(new Student(username, rs2.getString(columnIndex), password));
//				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}
	
	public static List<Student> findStudents(String subjectCode) {
		List<Student> students = new ArrayList<Student>();
		String sql = "SELECT * FROM student_subject_mapping WHERE subject_code = ?";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, subjectCode);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String username = rs.getString(2);
				Student student = StudentMapper.find(username);
				students.add(student);
//				String sql2 = "SELECT * FROM students WHERE username = ?";
//				PreparedStatement stmt2 = conn.prepareStatement(sql2);
//				stmt2.setString(1, username);
//				ResultSet rs2 = stmt2.executeQuery();
//				while(rs2.next()) {
//					students.add(new Student(username, rs2.getString(columnIndex), password));
//				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}

	public List<Subject> findSubjects(String username) {
		
		List<Subject> subjects = new ArrayList<Subject>();
		String sql = "SELECT subject_code FROM student_subject_mapping WHERE student_username = ?";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String subjectCode = rs.getString(1);
				Subject subject = subjectMapper.find(subjectCode);
				subjects.add(subject);
//				String sql2 = "SELECT * FROM students WHERE username = ?";
//				PreparedStatement stmt2 = conn.prepareStatement(sql2);
//				stmt2.setString(1, username);
//				ResultSet rs2 = stmt2.executeQuery();
//				while(rs2.next()) {
//					students.add(new Student(username, rs2.getString(columnIndex), password));
//				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subjects;
	}

	
}
