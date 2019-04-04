package com.nano.videosite.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Video {
	public @Column(nullable=false) String filename;
	private @GeneratedValue @Id @Column(name="video_id") Long id;
	private Long date;
	
	private @ManyToMany(mappedBy= "playlistList") Set<Playlist> playlists;
	private int ordered;
	
	public Video() {
	}
	public Video(Long id,String filename, Long date) {
		super();
		this.filename = filename;
		this.id = id;
		this.date = date;
	}
	public Video(String filename, Long date) {
		super();
		this.filename = filename;
		this.date = date;
	}
	public Video(Long id,String filename) {
		super();
		this.filename = filename;
		this.id = id;
	}
	public Video(String filename, Long id, int ordered) {
		super();
		this.filename = filename;
		this.id = id;
		this.ordered = ordered;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
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
	public int getOrdered() {
		return ordered;
	}
	public void setOrdered(int ordered) {
		this.ordered = ordered;
	}
	
}
