package com.example.demo.Models.UserModels;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import ENUMS.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NonNull;

@Entity(name = "UserGender")
@Table(name = "UserGender")
public class UserGender implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	@Id
	private String id;

	@NonNull
	@Column(unique = true)
	private String userId;

	@NonNull
	private Gender gender;

	public UserGender(@NonNull String userId, @NonNull Gender gender) {
		super();
		this.userId = userId;
		this.gender = gender;
	}

	public UserGender() {
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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserGender [id=" + id + ", userId=" + userId + ", gender=" + gender + "]";
	}

}
