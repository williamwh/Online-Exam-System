package domain;


public class Choice {
	private int choiceId;
	private int questionId;
	private int choiceIndex;
	private String text;
	
	public Choice(int choiceId, int questionId, int choiceIndex, String text) {
		this.choiceId = choiceId;
		this.questionId = questionId;
		this.choiceIndex = choiceIndex;
		this.text = text;
    }
	
	public Choice(int questionId, int choiceIndex, String text) {
		this.questionId = questionId;
		this.choiceIndex = choiceIndex;
		this.text = text;
		
    }
	
	public int getChoiceId() {
		return this.choiceId;
	}

	public int getQuestionId() {
		return this.questionId;
	}
	
	public int getChoiceIndex() {
		return this.choiceIndex;
	}
	
	public String getChoiceText() {
		return this.text;
	}
}