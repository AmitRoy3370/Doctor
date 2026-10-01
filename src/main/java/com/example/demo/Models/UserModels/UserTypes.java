package com.example.demo.Models.UserModels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ENUMS.UserType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.NonNull;

@Entity(name = "UserTypes")
@Table(name = "user_types")
public class UserTypes implements Serializable {

	private static final long serialVersionUID = 5L;

	@Id
	private String id;

	@NonNull
	@Column(name = "user_id", unique = true)
	private String userId;

	@NonNull
	@ElementCollection
	@CollectionTable(name = "user_types_roles", joinColumns = @JoinColumn(name = "user_types_id"))
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private List<UserType> type = new ArrayList<>();

	public UserTypes(@NonNull String userId, @NonNull List<UserType> type) {
		super();
		this.userId = userId;
		this.type = type;
	}

	public UserTypes() {
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

	public List<UserType> getType() {
		return type;
	}

	public void setType(List<UserType> type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserTypes [id=" + id + ", userId=" + userId + ", type=" + type + "]";
	}

}