package com.example.demo.Models.UserModels;

import java.io.Serializable;

import jakarta.persistence.Id;
import jakarta.persistence.PrePersist; // 👈 Import this
import java.util.UUID; // 👈 Must import UUID
import ENUMS.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NonNull;
import jakarta.persistence.EnumType;      // 👈 ADDED
import jakarta.persistence.Enumerated;    // 👈 ADDED

@Entity(name = "UserGender")
@Table(name = "\"UserGender\"")
public class UserGender implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3L;

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	@NonNull
	@Column(unique = true)
	private String userId;

	@NonNull
	@Enumerated(EnumType.STRING) // 👈 Add this line
	@Column(name = "gender")
	private Gender gender;

	public UserGender(@NonNull String userId, @NonNull Gender gender) {
		super();
		this.userId = userId;
		this.gender = gender;
	}

	public UserGender() {
		super();
	}

	  // THIS IS THE MISSING METHOD YOU NEED TO ADD:
    // =========================================================
	@PrePersist
	public void generateId() {
		if (this.id == null || this.id.isBlank()) {
			this.id = UUID.randomUUID().toString();
		}
	}
    // =========================================================

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
