/**
 * 
 */
package at.test.object;

/**
 * @author hoangnh
 *
 */
public class User {
	private int userId, powerUser;
	private String username, email, registed_date;
	private float money, scoreLevel;
	
	public User(int userId, String username, String email, float scoreLevel, String registed_date, 
			int powerUser, float money){
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.registed_date = registed_date;
		this.powerUser = powerUser;
		this.money = money;
		this.setScoreLevel(scoreLevel);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPowerUser() {
		return powerUser;
	}

	public void setPowerUser(int powerUser) {
		this.powerUser = powerUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegisted_date() {
		return registed_date;
	}

	public void setRegisted_date(String registed_date) {
		this.registed_date = registed_date;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public float getScoreLevel() {
		return scoreLevel;
	}

	public void setScoreLevel(float scoreLevel) {
		this.scoreLevel = scoreLevel;
	}
}
