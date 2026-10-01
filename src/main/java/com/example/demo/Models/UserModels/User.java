package com.example.demo.Models.UserModels;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NonNull;

@Entity(name = "User")
@Table(name = "User")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@NonNull
	@Column(unique = true, name = "userName")
	private String userName;
	@NonNull
	@Column(name = "name")
	private String name;
	@Column(name = "profileImageId")
	private String profileImageId;
	@NonNull
	@Column(name = "password")
	private String password;

	public User(@NonNull String userName, @NonNull String name, String profileImageId, @NonNull String password) {
		super();
		this.userName = userName;
		this.name = name;
		this.profileImageId = profileImageId;
		this.password = password;
	}

	public User() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileImageId() {
		return profileImageId;
	}

	public void setProfileImageId(String profileImageId) {
		this.profileImageId = profileImageId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", name=" + name + ", profileImageId=" + profileImageId
				+ ", password=" + password + "]";
	}

}
