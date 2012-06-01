package com.ppclink.iqarena.object;

public class Rank {
	String name;
	int id, score;
	String date_record;
	public Rank(int id, String name, int score){
		this.name = name;
		this.id = id;
		this.score = score;
	}
	public Rank(int id, String name, int score, String date_record){
		this.name = name;
		this.id = id;
		this.score = score;
		this.date_record = date_record;
	}
	public String getDate_record() {
		return date_record;
	}
	public void setDate_record(String date_record) {
		this.date_record = date_record;
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
