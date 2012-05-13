package com.ppclink.iqarena.object;

public class QuestionLite {
	String quesName, answerA, answerB, answerC, answerD, desAnswer;
	int quesId, quesType, answer;
	public QuestionLite(int quesId, String quesName, int quesType, 
			String answerA, String answerB, String answerC, String answerD, 
			int answer, String desAnswer){
		this.quesId = quesId;
		this.quesName = quesName;
		this.quesType = quesType;
		this.answerA = answerA;
		this.answerB = answerB;
		this.answerC = answerC;
		this.answerD = answerD;
		this.answer = answer;
		this.desAnswer = desAnswer;
	}
	public String getQuesName() {
		return quesName;
	}
	public void setQuesName(String quesName) {
		this.quesName = quesName;
	}
	public String getAnswerA() {
		return answerA;
	}
	public void setAnswerA(String answerA) {
		this.answerA = answerA;
	}
	public String getAnswerB() {
		return answerB;
	}
	public void setAnswerB(String answerB) {
		this.answerB = answerB;
	}
	public String getAnswerC() {
		return answerC;
	}
	public void setAnswerC(String answerC) {
		this.answerC = answerC;
	}
	public String getAnswerD() {
		return answerD;
	}
	public void setAnswerD(String answerD) {
		this.answerD = answerD;
	}
	public String getDesAnswer() {
		return desAnswer;
	}
	public void setDesAnswer(String desAnswer) {
		this.desAnswer = desAnswer;
	}
	public int getQuesId() {
		return quesId;
	}
	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}
	public int getQuesType() {
		return quesType;
	}
	public void setQuesType(int quesType) {
		this.quesType = quesType;
	}
	public int getAnswer() {
		return answer;
	}
	public void setAnswer(int answer) {
		this.answer = answer;
	}
	
}
