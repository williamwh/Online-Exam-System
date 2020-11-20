package domain;
import java.sql.Timestamp;

public class ExamAnswer {
	
	private int examAnswerID;
	private int examID;
	private String studentUsername;
	private boolean isMarked;
	private int totalMarks;
	private Timestamp date_created;
	private String markingComment;
	
	public ExamAnswer(int examAnswerID, int examID, String studentUsername, 
			boolean isMarked, int totalMarks, Timestamp date_created, String markingComment) {
		this.examAnswerID = examAnswerID;
		this.examID = examID;
		this.studentUsername = studentUsername;
		this.isMarked = isMarked;
		this.totalMarks = totalMarks;
		this.date_created = date_created;
		this.markingComment = markingComment;
		
	}
	
	public int getExamAnswerID() {
		return examAnswerID;
	}
	
	public int getExamID() {
		return examID;
	}
	
	public String getStudentUsername() {
		return studentUsername;
	}
	
	public boolean getIsMarked() {
		return isMarked;
	}
	
	public int getTotalMarks() {
		return totalMarks;
	}
	
	public Timestamp getDateCreated() {
		return date_created;
	}
	
	public String getComment() {
		return markingComment;
	}
	
	public void setTotalMarks(int mark) {
		totalMarks = mark;
	}
	
	public void setComment(String comment) {
		this.markingComment = comment;
	}
	

}
