package com.nano.videosite.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Comment {
	private @Id @GeneratedValue(strategy=GenerationType.IDENTITY) long id;
	@Column(length=255)
	private String comment;
	private long userId;
	private long videoId;
	public Comment(long id, String comment, long userId, long videoId) {
		super();
		this.id = id;
		this.comment = comment;
		this.userId = userId;
		this.videoId = videoId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getVideoId() {
		return videoId;
	}
	public void setVideoId(long videoId) {
		this.videoId = videoId;
	}
	
	
}
