package com.example.backend.auth;

public class UserContext {

	private Long id;

	private String username;

	private String photo;

	public UserContext(Long id, String username, String photo) {
		super();
		this.id = id;
		this.username = username;
		this.photo = photo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
