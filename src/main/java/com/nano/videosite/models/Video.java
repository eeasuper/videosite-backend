package com.nano.videosite.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Video {
	public @Column(nullable=false) String filename;
	private @GeneratedValue @Id Long id;
	public Video() {
	}
	public Video(String filename, Long id) {
		super();
		this.filename = filename;
		this.id = id;
	}
	public Video(String filename) {
		super();
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
