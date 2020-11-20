package domain;

public class QuestionAnswer {
	
	private int questionAnswerID;
	private int examAnswerID;
	private int questionID;
	private String studentAnswer;
	private int marks;
	
	public QuestionAnswer(int questionAnswerID, int examAnswerID, int questionID, String studentAnswer, int marks) {
		this.questionAnswerID = questionAnswerID;
		this.examAnswerID = examAnswerID;
		this.questionID = questionID;
		this.studentAnswer = studentAnswer;
		this.marks = marks;
	}
	
	public int getQuestionAnswerID() {
		return questionAnswerID;
	}
	
	public int getExamAnswerID() {
		return examAnswerID;
	}
	
	public int getQuestionID() {
		return questionID;
	}
	
	public String getStudentAnswer() {
		return studentAnswer;
	}
	
	public int getMarks() {
		return marks;
	}
	
	public void setMarks(int marks) {
		this.marks = marks;
	}
}
