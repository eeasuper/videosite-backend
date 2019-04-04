package com.nano.videosite.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class VideoDescription {
	private @GeneratedValue @Id @Column(name="id") Long id;
	private Long date;
	private Long userId;
	private Long videoId;
	private String title;
	private String description;
	public VideoDescription() {
	}
	public VideoDescription(Long id, Long date, Long userId, Long videoId, String title, String description) {
		super();
		this.id = id;
		this.date = date;
		this.userId = userId;
		this.videoId = videoId;
		this.title = title;
		this.description = description;
	}
	public VideoDescription(Long date, Long userId, Long videoId, String title, String description) {
		super();
		this.date = date;
		this.userId = userId;
		this.videoId = videoId;
		this.title = title;
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
