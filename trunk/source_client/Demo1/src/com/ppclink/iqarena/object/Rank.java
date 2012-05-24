package com.ppclink.iqarena.object;

public class Rank {
	String name;
	int id, score;
	public Rank(int id, String name, int score){
		this.name = name;
		this.id = id;
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
