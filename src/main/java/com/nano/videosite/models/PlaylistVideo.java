package com.nano.videosite.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PlaylistVideo {
	private @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="pv_id") long id;
	private long videoId;
	private long playlistId;
	private int ordered;
	public PlaylistVideo() {
	}
	public PlaylistVideo(long id, long videoId, long playlistId, int order) {
		super();
		this.id = id;
		this.videoId = videoId;
		this.playlistId = playlistId;
		this.ordered = order;
	}
	public PlaylistVideo(long videoId, long playlistId, int order) {
		super();
		this.videoId = videoId;
		this.playlistId = playlistId;
		this.ordered = order;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getVideoId() {
		return videoId;
	}
	public void setVideoId(long videoId) {
		this.videoId = videoId;
	}
	public long getPlaylistId() {
		return playlistId;
	}
	public void setPlaylistId(long playlistId) {
		this.playlistId = playlistId;
	}
	public int getOrder() {
		return ordered;
	}
	public void setOrder(int order) {
		this.ordered = order;
	}
	
}
