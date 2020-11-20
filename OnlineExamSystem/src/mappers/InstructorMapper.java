package mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import datasource.DBConnection;
import domain.Instructor;
import domain.User;

public class InstructorMapper {

	public String insert(User user) {
		String sql = "INSERT INTO instructors (username, email, password)" +
				"VALUES (?, ?, ?)";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement insertStatement = conn.prepareStatement(sql);
			insertStatement.setString(1, user.getUsername());
			insertStatement.setString(2, user.getEmail());
			insertStatement.setString(3, user.getPassword());
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user.getUsername();
	}
	
	public static Instructor find(String username) {
		Instructor instructor = null;
		String sql = "SELECT username, email, password " +
						"FROM instructors " +
						"WHERE username = ?";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement findStatement = conn.prepareStatement(sql);
			findStatement.setString(1, username);
			ResultSet result = findStatement.executeQuery();
			if(result.next()) {
				String resultEmail = result.getString(2);
				String resultPassword = result.getString(3);
				instructor = new Instructor(username, resultEmail, resultPassword);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instructor; 
	}
	
	
	public static ArrayList<Instructor> findAll() {
		ArrayList<Instructor> instructors = new ArrayList<Instructor>();
		String sql = "SELECT * FROM instructors";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String username = rs.getString(1);
				String email = rs.getString(2);
				String password = rs.getString(3);
				instructors.add(new Instructor(username, email, password));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instructors;
	}
	
	public Instructor findWithPassword(String username, String password) {
		Instructor instructor = null;
		String sql = "SELECT username, email, password " +
						"FROM instructors " +
						"WHERE username = ? AND password = ?";
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement findStatement = conn.prepareStatement(sql);
			findStatement.setString(1, username);
			findStatement.setString(2, password);
			ResultSet result = findStatement.executeQuery();
			if(result.next()) {
				String resultEmail = result.getString(2);
				String resultPassword = result.getString(3);
				instructor = new Instructor(username, resultEmail, resultPassword);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instructor; 
	}
}
