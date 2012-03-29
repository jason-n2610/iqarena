/**
 * 
 */
package at.test.object;

/**
 * @author hoangnh
 * 
 */
public class MemberScore {
	private String strUserName, strQuestionAnswer, strScore, strAbility,
			strCombo, strUserId;

	public String getStrUserName() {
		return strUserName;
	}

	public MemberScore(String strUserId, String strUserName,
			String strQuestionAnswer, String strScore, String strAbility,
			String strCombo) {
		super();
		this.strUserName = strUserName;
		this.strQuestionAnswer = strQuestionAnswer;
		this.strScore = strScore;
		this.strAbility = strAbility;
		this.strCombo = strCombo;
		this.strUserId = strUserId;
	}

	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	public String getStrQuestionAnswer() {
		return strQuestionAnswer;
	}

	public void setStrQuestionAnswer(String strQuestionAnswer) {
		this.strQuestionAnswer = strQuestionAnswer;
	}

	public String getStrScore() {
		return strScore;
	}

	public void setStrScore(String strScore) {
		this.strScore = strScore;
	}

	public String getStrAbility() {
		return strAbility;
	}

	public void setStrAbility(String strAbility) {
		this.strAbility = strAbility;
	}

	public String getStrCombo() {
		return strCombo;
	}

	public void setStrCombo(String strCombo) {
		this.strCombo = strCombo;
	}

	public String getStrUserId() {
		return strUserId;
	}

	public void setStrUserId(String strUserId) {
		this.strUserId = strUserId;
	}
}
