package com.yanyoo.model;

public class Assign {
	private int id;
	private String title;
	private String content;
	private String time;
	private String type;
	private String member;
	private String state;
	private String sponsor;
	private int maxmem;
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public int getMaxmem() {
		return maxmem;
	}
	public void setMaxmem(int maxmem) {
		this.maxmem = maxmem;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "Assign [id=" + id + ", title=" + title + ", content=" + content
				+ ", time=" + time + ", type=" + type + ", member=" + member
				+ ", state=" + state + ", sponsor=" + sponsor + ", maxmem="
				+ maxmem + "]";
	}
	

	
}
