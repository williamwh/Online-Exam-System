package mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import datasource.DBConnection;
import domain.Student;
import domain.User;

public class StudentMapper {


	public String insert(User user) {
		String sql = "INSERT INTO students (username, email, password)" +
				"VALUES (?, ?, ?)";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement insertStatement = conn.prepareStatement(sql);
			insertStatement.setString(1, user.getUsername());
			insertStatement.setString(2, user.getEmail());
			insertStatement.setString(3, user.getPassword());
			insertStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user.getUsername();
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
	
	public static ArrayList<Student> findAll() {
		ArrayList<Student> students = new ArrayList<Student>();
		String sql = "SELECT * FROM students";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String username = rs.getString(1);
				String email = rs.getString(2);
				String password = rs.getString(3);
				students.add(new Student(username, email, password));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}
	
	public Student findWithPassword(String username, String password) {
		Student student = null;
		String sql = "SELECT username, email, password " +
						"FROM students " +
						"WHERE username = ? AND password = ?";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement findStatement = conn.prepareStatement(sql);
			findStatement.setString(1, username);
			findStatement.setString(2, password);
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
	
}
