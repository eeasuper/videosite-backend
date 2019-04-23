package com.nano.videosite.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.core.io.Resource;

@Entity
public class Video {
	public @Column(nullable=false) String filename;
	private @GeneratedValue @Id @Column(name="video_id") Long id;
	private @Column(name="uploaded_date") Long date;
	private Long uploaderId;
	private String uploaderUsername;
	
	private @ManyToMany(mappedBy= "playlistList") Set<Playlist> playlists;
	//ordered can probably be erased.
	private int ordered;
	
	private String title;
	private String description;
	private Long view = (long) 0;
	private String thumbnailImageBase64;
//	private Resource thumbnailImage;
	public Video() {
	}
	public Video(Long id) {
		this.id = id;
	}
	public Video(String filename, Long date, Long uploaderId, String uploaderUsername, String title, String description, Long view) {
		super();
		this.filename = filename;
		this.date = date;
		this.uploaderId = uploaderId;
		this.uploaderUsername = uploaderUsername;
		this.title = title;
		this.description = description;
		this.view = view;
	}
	public Video(String filename, Long date, Long uploaderId, String title, String description, Long view) {
		super();
		this.filename = filename;
		this.date = date;
		this.uploaderId = uploaderId;
		this.title = title;
		this.description = description;
		this.view = view;
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
	public Video(String filename, Long id, Long date, Long uploaderId) {
		super();
		this.filename = filename;
		this.id = id;
		this.date = date;
		this.uploaderId = uploaderId;
	}
	public Video(String filename, Long date, Long uploaderId) {
		super();
		this.filename = filename;
		this.date = date;
		this.uploaderId = uploaderId;
	}
	public Video(Long id, String title, String description) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
	}
//	public Video(Long id, String title, String description, Resource thumbnailImage) {
//		super();
//		this.id = id;
//		this.title = title;
//		this.description = description;
//	}
	public Long getView() {
		return view;
	}
	public void setView(Long view) {
		this.view = view;
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
	public Long getUploaderId() {
		return uploaderId;
	}
	public void setUploaderId(Long uploaderId) {
		this.uploaderId = uploaderId;
	}
//	public Resource getThumbnailImage() {
//		return thumbnailImage;
//	}
//	public void setThumbnailImage(Resource thumbnailImageBase64) {
//		this.thumbnailImage = thumbnailImageBase64;
//	}
	public String getUploaderUsername() {
		return uploaderUsername;
	}
	public void setUploaderUsername(String uploaderUsername) {
		this.uploaderUsername = uploaderUsername;
	}
	
}
