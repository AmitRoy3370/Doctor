package com.example.demo.Models.UserModels;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NonNull;

@Entity(name = "UserContact")
@Table(name = "UserContact")
public class UserContact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	@Id
	private String id;
	@NonNull
	@Column(unique = true)
	private String userId;
	@Column(unique = true)
	private String email;
	@Column(unique = true)
	private String phone;

	public UserContact(@NonNull String userId, String email, String phone) {
		super();
		this.userId = userId;
		this.email = email;
		this.phone = phone;
	}

	public UserContact() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserContact [id=" + id + ", userId=" + userId + ", email=" + email + ", phone=" + phone + "]";
	}

}
