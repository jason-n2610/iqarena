package com.ppclink.iqarena.object;

public class Category {
	String category_id, category_name, date_created, describle;
	public Category(String category_id, String category_name, 
			String date_created, String describle){
		this.category_id = category_id;
		this.category_name = category_name;
		this.date_created = date_created;
		this.describle = describle;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getDate_created() {
		return date_created;
	}
	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}
	public String getDescrible() {
		return describle;
	}
	public void setDescrible(String describle) {
		this.describle = describle;
	}	
}
