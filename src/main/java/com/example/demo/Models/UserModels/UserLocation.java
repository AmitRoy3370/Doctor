package com.example.demo.Models.UserModels;

import java.io.Serializable;


import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NonNull;

@Entity(name = "UserLocation")
@Table(name = "UserLocation")
public class UserLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;

	@Id
	private String id;

	@NonNull
	@Column(unique = true)
	private String userId;

	@NonNull
	private String locationName;

	private double lattitude, longititude;

	public UserLocation(@NonNull String userId, @NonNull String locationName, double lattitude, double longititude) {
		super();
		this.userId = userId;
		this.locationName = locationName;
		this.lattitude = lattitude;
		this.longititude = longititude;
	}

	public UserLocation() {
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

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public double getLattitude() {
		return lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public double getLongititude() {
		return longititude;
	}

	public void setLongititude(double longititude) {
		this.longititude = longititude;
	}

	@Override
	public String toString() {
		return "UserLocation [id=" + id + ", userId=" + userId + ", locationName=" + locationName + ", lattitude="
				+ lattitude + ", longititude=" + longititude + "]";
	}

}
