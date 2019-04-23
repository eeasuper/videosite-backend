package com.nano.videosite.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

@Entity
public class Playlist {
	
	private @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="playlist_id") long id;
	private long userId;
	private String title;
	private long date;
	private String username;
//	@OneToMany
	//Will receive playlistVideo Ids.
//	@ElementCollection
//	@CollectionTable(name="playlist_list",joinColumns= @JoinColumn(name="playlist_id"))
//	@Column(name="playlistVideo_id")
//	private Set<Long> playlistList;
	@ManyToMany
    @JoinTable(name="playlist_values",
        joinColumns=@JoinColumn(name="playlist_id"),
        inverseJoinColumns= @JoinColumn(name="video_id"))
	private Map<Integer, Video> playlistList;
	
	public Playlist() {
		
	}
	public Playlist(String title) {
		super();
		this.title = title;
	}
	public Playlist(long userId, String title, long date) {
		super();
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.date = date;
	}
	public Playlist(long id, long userId, Map<Integer, Video> playlist) {
		super();
		this.id = id;
		this.userId = userId;
		this.playlistList = playlist;
	}
	public Playlist(long userId, Map<Integer, Video> playlist) {
		super();
		this.id = id;
		this.userId = userId;
		this.playlistList = playlist;
	}
	
	public Playlist(long userId, String title, long date, Map<Integer, Video> playlistValue) {
		super();
		this.userId = userId;
		this.title = title;
		this.date = date;
		this.playlistList = playlistValue;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Map<Integer, Video> getPlaylist() {
		return playlistList;
	}

	public void setPlaylist(Map<Integer, Video> playlist) {
		this.playlistList = playlist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}	
	
}
