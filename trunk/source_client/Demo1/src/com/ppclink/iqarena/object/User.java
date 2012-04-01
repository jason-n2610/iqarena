/**
 * 
 */
package com.ppclink.iqarena.object;

/**
 * @author hoangnh
 * 
 */
public class User {
	private float money, scoreLevel;
	private int userId, powerUser;
	private String username, email, registed_date;

	public User(int userId, String username, String email, float scoreLevel,
			String registed_date, int powerUser, float money) {
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.registed_date = registed_date;
		this.powerUser = powerUser;
		this.money = money;
		this.setScoreLevel(scoreLevel);
	}

	public String getEmail() {
		return email;
	}

	public float getMoney() {
		return money;
	}

	public int getPowerUser() {
		return powerUser;
	}

	public String getRegisted_date() {
		return registed_date;
	}

	public float getScoreLevel() {
		return scoreLevel;
	}

	public int getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public void setPowerUser(int powerUser) {
		this.powerUser = powerUser;
	}

	public void setRegisted_date(String registed_date) {
		this.registed_date = registed_date;
	}

	public void setScoreLevel(float scoreLevel) {
		this.scoreLevel = scoreLevel;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
