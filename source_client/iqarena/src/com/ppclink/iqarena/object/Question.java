package com.ppclink.iqarena.object;

public class Question {
	private String mStrId, mStrContent, mStrAnswerA, mStrAnswerB, mStrAnswerC,
			mStrAnswerD;

	public Question(String strId, String strContent, String strA, String strB,
			String strC, String strD) {
		this.setmStrId(strId);
		this.mStrContent = strContent;
		this.mStrAnswerA = strA;
		this.mStrAnswerB = strB;
		this.mStrAnswerC = strC;
		this.mStrAnswerD = strD;
	}

	public String getmStrAnswerA() {
		return mStrAnswerA;
	}

	public String getmStrAnswerB() {
		return mStrAnswerB;
	}

	public String getmStrAnswerC() {
		return mStrAnswerC;
	}

	public String getmStrAnswerD() {
		return mStrAnswerD;
	}

	public String getmStrContent() {
		return mStrContent;
	}

	public String getmStrId() {
		return mStrId;
	}

	public void setmStrAnswerA(String mStrAnswerA) {
		this.mStrAnswerA = mStrAnswerA;
	}

	public void setmStrAnswerB(String mStrAnswerB) {
		this.mStrAnswerB = mStrAnswerB;
	}

	public void setmStrAnswerC(String mStrAnswerC) {
		this.mStrAnswerC = mStrAnswerC;
	}

	public void setmStrAnswerD(String mStrAnswerD) {
		this.mStrAnswerD = mStrAnswerD;
	}

	public void setmStrContent(String mStrContent) {
		this.mStrContent = mStrContent;
	}

	public void setmStrId(String mStrId) {
		this.mStrId = mStrId;
	}

}
