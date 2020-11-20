package lockManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import datasource.DBConnection;

public class LockManager {
	public static boolean hasLock(int lockable, String owner) {
		Connection conn = DBConnection.getDBConnection();
		try {
			String findLockStatement = "SELECT * FROM locks WHERE lockable = ? AND owner = ?";
			PreparedStatement Statement = conn.prepareStatement(findLockStatement);
			Statement.setInt(1, lockable);
			Statement.setString(2, owner);
			ResultSet rs = Statement.executeQuery();
			if (rs.next()) {
				// lock acquired
				return true;
			}
			conn.close();
		} catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				System.out.println("Has Lock Sql Connection Already closed");
			}
			System.out.println("Locked");
		}
		return false;
	}

	// tables for exams, questions, choices, student_exam_answers, student_question_answers are classfied as 1
	// If you are going to edit any of these tables, please use lockable as 1
	public static boolean acquireLock(int lockable, String owner){
		Connection conn = DBConnection.getDBConnection();
		try {
			String insertLockStmt = "INSERT INTO locks (lockable, owner) VALUES (?, ?);";
			PreparedStatement Statement = conn.prepareStatement(insertLockStmt);
			Statement.setInt(1, lockable);
			Statement.setString(2, owner);
			Statement.executeUpdate();
			conn.close();
			System.out.println(owner + " has sucessfully acquired lock: " + lockable);
			return true;
		} catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				System.out.println("Acquire Lock Sql Connection Already closed");
			}
			System.out.println(owner + " can't acquire lock: " + lockable);
		}
		return false;
	
	}
	
	
	public static void releaseLock(int lockable, String owner) {
		try {
			String deleteLockStmt = "DELETE FROM locks WHERE lockable = ? AND owner = ?";
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement Statement = conn.prepareStatement(deleteLockStmt);
			Statement.setInt(1, lockable);
			Statement.setString(2, owner);
			Statement.executeUpdate();
			System.out.println(owner + " has released lock: " + lockable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
