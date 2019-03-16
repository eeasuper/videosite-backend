package com.nano.videosite.models;

import java.util.HashMap;
import java.util.HashSet;
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
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;

@Entity
public class Playlist {
	
	private @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="playlist_id") long id;
	
	private long userId;
	
	//https://www.callicoder.com/hibernate-spring-boot-jpa-element-collection-demo/
	//https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/customizing-map-mapping.html
	//Key will be order of video. 
	@ElementCollection(fetch = FetchType.EAGER)
	//, referencedColumnName="id"
	@CollectionTable(name="playlist_value", joinColumns = @JoinColumn(name="playlist_id"))
//	@CollectionTable(name="playlist_value")
//	@MapKeyColumn(name="order")
//	@Column(name="video_idd")
	private Map<Integer, Video> playlistValue = new HashMap();

	public Playlist() {
		
	}
	
	public Playlist(long id, long userId, Map<Integer, Video> playlist) {
		super();
		this.id = id;
		this.userId = userId;
		this.playlistValue = playlist;
	}
	public Playlist(long userId, Map<Integer, Video> playlist) {
		super();
		this.id = id;
		this.userId = userId;
		this.playlistValue = playlist;
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
		return playlistValue;
	}

	public void setPlaylist(Map<Integer, Video> playlist) {
		this.playlistValue = playlist;
	}
	
	
}
