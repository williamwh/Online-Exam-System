package mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datasource.DBConnection;
import domain.Choice;

public class ChoiceMapper {
	private static final String findAllChoicesStatement = "SELECT * FROM choices WHERE question_id = ? ORDER BY choice_index";
	private static final String insertChoiceStatement = "INSERT INTO choices (question_id, choice_index, text) VALUES (?, ?, ?)";
	private static final String deleteChoiceStatement = "DELETE FROM choices WHERE choice_id = ?";

	public static String insert(Choice choice) {
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement insertStatement = conn.prepareStatement(insertChoiceStatement);

			int questionId;
			if (choice.getQuestionId() == -1) {
				Connection conn2 = DBConnection.getDBConnection();
				PreparedStatement lastIdStatement = conn2.prepareStatement("SELECT MAX(question_id) FROM questions;");
				ResultSet rs = lastIdStatement.executeQuery();
				rs.next();
				questionId = rs.getInt(1);
				conn2.close();
			} else {
				questionId = choice.getQuestionId();
			}

			insertStatement.setInt(1, questionId);
			insertStatement.setInt(2, choice.getChoiceIndex());
			insertStatement.setString(3, choice.getChoiceText());
			System.out.println(insertStatement);
			insertStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Question: " + choice.getQuestionId() + ", Choice: " + choice.getChoiceIndex() + " sucessfully added!";
	}

	public static List<Choice> getAllChoicesFromQuestion(int questionIdQuery) {
		List<Choice> choices = new ArrayList<>();
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement stmt = conn.prepareStatement(findAllChoicesStatement);
			stmt.setInt(1, questionIdQuery);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int choiceId = rs.getInt(1);
				int questionId = rs.getInt(2);
				int choiceIndex = rs.getInt(3);
				String choiceText = rs.getString(4);
				choices.add(new Choice(choiceId, questionId, choiceIndex, choiceText));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return choices;
	}

	public static void delete(Choice choice) {
		try {
			Connection conn = DBConnection.getDBConnection();
			PreparedStatement deleteStatement = conn.prepareStatement(deleteChoiceStatement);

			deleteStatement.setInt(1, choice.getChoiceId());
			System.out.println(deleteStatement);
			deleteStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
	}
}