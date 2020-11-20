package mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datasource.DBConnection;
import domain.Instructor;
import domain.Subject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datasource.DBConnection;
import domain.Subject;
import domain.Instructor;

public class InstructorSubMapper {

	private static SubjectMapper subjectMapper = new SubjectMapper();
	private static InstructorMapper instructorMapper = new InstructorMapper();
	
	public static String insert(Instructor instructor, Subject subject) {
		String sql = "INSERT INTO instructor_subject_mapping (instructor_username, subject_code)" +
				"VALUES (?, ?)";
		try {
			PreparedStatement insertStatement = DBConnection.prepare(sql);
			insertStatement.setString(1, instructor.getUsername());
			insertStatement.setString(2, subject.getSubjectCode());
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instructor.getUsername() + ", " + subject.getName();
	}
	
	public static String insert(String instructorUsername, Subject subject) {
		String sql = "INSERT INTO instructor_subject_mapping (instructor_username, subject_code)" +
				"VALUES (?, ?)";
		try {
			PreparedStatement insertStatement = DBConnection.prepare(sql);
			insertStatement.setString(1, instructorUsername);
			insertStatement.setString(2, subject.getSubjectCode());
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instructorUsername + ", " + subject.getName();
	}
	
	public static Instructor find(String username) {
		Instructor instructor = null;
		String sql = "SELECT username, email, password " +
						"FROM instructors " +
						"WHERE username = ?";
		try {
			PreparedStatement findStatement = DBConnection.prepare(sql);
			findStatement.setString(1, username);
			System.out.println(findStatement);
			ResultSet result = findStatement.executeQuery();
			if(result.next()) {
				String resultEmail = result.getString(2);
				String resultPassword = result.getString(3);
				instructor = new Instructor(username, resultEmail, resultPassword);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instructor; 
	}
	
	public static List<String> findInstructorUsernames(String subjectCode) {
		List<String> instructors = new ArrayList<String>();
		String sql = "SELECT * FROM instructor_subject_mapping WHERE subject_code = ?";
		try {
			PreparedStatement stmt = DBConnection.prepare(sql);
			stmt.setString(1, subjectCode);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String username = rs.getString(2);
				instructors.add(username);
//				String sql2 = "SELECT * FROM students WHERE username = ?";
//				PreparedStatement stmt2 = DBConnection.prepare(sql2);
//				stmt2.setString(1, username);
//				ResultSet rs2 = stmt2.executeQuery();
//				while(rs2.next()) {
//					students.add(new Student(username, rs2.getString(columnIndex), password));
//				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instructors;
	}
	
	public static List<Instructor> findInstructors(String subjectCode) {
		List<Instructor> instructors = new ArrayList<Instructor>();
		String sql = "SELECT * FROM instructor_subject_mapping WHERE subject_code = ?";
		try {
			PreparedStatement stmt = DBConnection.prepare(sql);
			stmt.setString(1, subjectCode);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String username = rs.getString(2);
				Instructor instructor = instructorMapper.find(username);
				instructors.add(instructor);
//				String sql2 = "SELECT * FROM students WHERE username = ?";
//				PreparedStatement stmt2 = DBConnection.prepare(sql2);
//				stmt2.setString(1, username);
//				ResultSet rs2 = stmt2.executeQuery();
//				while(rs2.next()) {
//					students.add(new Student(username, rs2.getString(columnIndex), password));
//				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instructors;
	}

	public static List<Subject> findSubjects(String username) {
		
		List<Subject> subjects = new ArrayList<Subject>();
		String sql = "SELECT subject_code FROM instructor_subject_mapping WHERE instructor_username = ? ORDER BY subject_code";
		try {
			PreparedStatement stmt = DBConnection.prepare(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String subjectCode = rs.getString(1);
				Subject subject = SubjectMapper.find(subjectCode);
				subjects.add(subject);
//				String sql2 = "SELECT * FROM students WHERE username = ?";
//				PreparedStatement stmt2 = DBConnection.prepare(sql2);
//				stmt2.setString(1, username);
//				ResultSet rs2 = stmt2.executeQuery();
//				while(rs2.next()) {
//					students.add(new Student(username, rs2.getString(columnIndex), password));
//				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subjects;
	}
}
