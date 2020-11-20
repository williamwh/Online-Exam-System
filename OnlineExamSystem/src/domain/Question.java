package domain;

import java.util.List;

import mappers.ChoiceMapper;

public class Question {
	private int questionId;
	private int examId;
	private int questionIndex;
	private String questionText;
	private int marks;
	private String questionType;
	private List<Choice> choices = null;
	
	public Question(int questionId, int examId, int questionIndex, String questionText, int marks, String questionType) {
		this.questionId = questionId;
		this.examId = examId;
		this.questionIndex = questionIndex;
		this.questionText = questionText;
		this.marks = marks;
		this.questionType = questionType;
    }
	
	public Question(int examId, int questionIndex, String questionText, int marks, String questionType) {
		this.examId = examId;
		this.questionIndex = questionIndex;
		this.questionText = questionText;
		this.marks = marks;
		this.questionType = questionType;
		
		
    }
	
	public Question(int questionId) {
		this.questionId = questionId;
	}
	
	public int getQuestionID() {
		return this.questionId;
	}

	public void setQuestionID(int questionId) {
		this.questionId = questionId;
	}

	public int getExamId() {
		return this.examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

	public int getQuestionIndex() {
		return this.questionIndex;
	}

	public void setQuestionIndex(int questionIndex) {
		this.questionIndex = questionIndex;
	}
	
	public String getQuestionText() {
		return this.questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	public int getMarks() {
		return this.marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}
	
	public String getQuestionType() {
		return this.questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	
	public List<Choice> getChoices(){
		if(this.choices == null) {
			this.choices = ChoiceMapper.getAllChoicesFromQuestion(this.questionId);
		}
		return this.choices;
	}
}
