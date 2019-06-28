package com.nano.videosite.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SiteUser")
public class User {
	private @Id @GeneratedValue(strategy=GenerationType.SEQUENCE) long id;
	private @Column(nullable=false) String  name;
	private @Column(nullable=false) String username;
	private @Column(nullable=false) String password;
	private @Column(nullable=false) String email;
	private String token;
	
	public User(){
	}

	public User(String username) {
		this.username = username;
	}
	
	public User(long id, String username) {
		super();
		this.id = id;
		this.username = username;
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(String name, String username, String password, String email) {
		super();
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	public User(String token, String name, String username, String password, String email) {
		super();
		this.token = token;
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public User(long id, String name, String username, String password, String email) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
