package mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datasource.DBConnection;
import domain.Instructor;
import domain.Student;
import domain.Subject;

public class SubjectMapper {
	
	private static final String findAllSubjectsStatement =
			"SELECT * from subjects";
    private static final String insertSubjectStatement =
			"INSERT INTO subjects (subject_code, name) VALUES (?, ?)";
    private static UserMapper userMapper = new UserMapper();
    
    public String insert(Subject subject) {
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement insertStatement = conn.prepareStatement(insertSubjectStatement);
			insertStatement.setString(1, subject.getSubjectCode());
			insertStatement.setString(2, subject.getName());
			//insertStatement.setString(3, subject.getCoordinator().getUsername());
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
		return subject.getSubjectCode();

	}
    
    
    public static Subject find(String subjectCode) {
    	Subject subject = null;
		String sql = "SELECT * " +
						"FROM subjects " +
						"WHERE subject_code = ?";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement findStatement = conn.prepareStatement(sql);
			findStatement.setString(1, subjectCode);
			System.out.println(findStatement);
			ResultSet result = findStatement.executeQuery();
			if(result.next()) {
				String name = result.getString(2);
				//String coordinatorName = result.getString(3);
				//Instructor coordinator = (Instructor) userMapper.find(coordinatorName);
				subject = new Subject(subjectCode, name);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subject;
    }
    
	public static List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        try {
        	Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(findAllSubjectsStatement);

        	ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String code = rs.getString(1);
				String name = rs.getString(2);
				//String coordinatorID = rs.getString(3);
				//InstructorMapper instructorMapper = new InstructorMapper();
				//Instructor coordinator = instructorMapper.find(coordinatorID);
				subjects.add(new Subject(code,name));
			}
			conn.close();
		} catch (SQLException e) {
	
		}
        return subjects;
    }
	
	public List<Subject> getInstructorSubjects(String coordinator_id){
		List<Subject> subjects = new ArrayList<Subject>();
		String findInstructorSubjectsStatement = "SELECT subjects.subject_code, subjects.name from subjects " + 
				"INNER JOIN instructor_subject_mapping ON subjects.subject_code = instructor_subject_mapping.subject_code " + 
				"WHERE instructor_subject_mapping.instructor_username = ?;";
		
		try {
			Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(findInstructorSubjectsStatement);
        	stmt.setString(1, coordinator_id);
        	ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String code = rs.getString(1);
				String name = rs.getString(2);
				//String coordinatorID = rs.getString(3);
				//InstructorMapper instructorMapper = new InstructorMapper();
				//Instructor coordinator = instructorMapper.find(coordinatorID);
				subjects.add(new Subject(code,name));
			}
			conn.close();
		} catch (SQLException e) {
	
		}
        return subjects;
	}
	
	public List<Subject> getStudentSubjects(String student_id){
		List<Subject> subjects = new ArrayList<Subject>();
		String findStudentSubjectsStatement = "SELECT subjects.subject_code, subjects.name from subjects " + 
				"INNER JOIN student_subject_mapping ON subjects.subject_code = student_subject_mapping.subject_code " + 
				"WHERE student_subject_mapping.student_username = ?;";
		try {
			Connection conn = DBConnection.getDBConnection();
        	PreparedStatement stmt = conn.prepareStatement(findStudentSubjectsStatement);
        	stmt.setString(1, student_id);
        	ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String code = rs.getString(1);
				String name = rs.getString(2);
				//String coordinatorID = rs.getString(3);
				//InstructorMapper instructorMapper = new InstructorMapper();
				//Instructor coordinator = instructorMapper.find(coordinatorID);
				subjects.add(new Subject(code,name));
			}
			conn.close();
		} catch (SQLException e) {
	
		}
		System.out.println(subjects.size());
        return subjects;
	}
	
}
