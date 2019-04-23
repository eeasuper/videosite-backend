package com.nano.videosite.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ViewVideo {
	private String ip;
	private long date;
	//change filename to id of Video later.
	private Long videoId;
	private @Id @GeneratedValue Long id;
	public ViewVideo() {
	}
	public ViewVideo(String ip, long date, Long videoId) {
		super();
		this.ip = ip;
		this.date = date;
		this.videoId = videoId;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	
	
}
