package com.example.demo.DTOFiles;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.Models.UserModels.UserContact;
import com.example.demo.Models.UserModels.UserGender;
import com.example.demo.Models.UserModels.UserLocation;
import com.example.demo.Models.UserModels.UserTypes;

public class DoctorResponse implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 11L;

	private String id;

	private String userId, userName, name;

	private String bio, accepted_user;

	private String bmdcRegistrationNumber;

	private String qualification;

	private List<String> hospitals = new ArrayList<>();

	private List<String> specialist = new ArrayList<>();

	private Instant createdAt = Instant.now();

	private Instant updatedAt = Instant.now();

	private String uploadedCV;

	private UserContact contact;

	private UserGender userGender;

	private UserLocation location;

	private UserTypes types;

	public DoctorResponse(String id, String userId, String userName, String bio, String bmdcRegistrationNumber,
			String qualification, List<String> hospitals, List<String> specialist, Instant createdAt, Instant updatedAt,
			String uploadedCV, UserContact contact, UserGender userGender, UserLocation location, UserTypes types,
			String name, String accepted_user) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.name = name;
		this.bio = bio;
		this.bmdcRegistrationNumber = bmdcRegistrationNumber;
		this.qualification = qualification;
		this.hospitals = hospitals;
		this.specialist = specialist;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.uploadedCV = uploadedCV;
		this.contact = contact;
		this.userGender = userGender;
		this.location = location;
		this.types = types;
		this.accepted_user = accepted_user;
	}

	public DoctorResponse() {
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getBmdcRegistrationNumber() {
		return bmdcRegistrationNumber;
	}

	public void setBmdcRegistrationNumber(String bmdcRegistrationNumber) {
		this.bmdcRegistrationNumber = bmdcRegistrationNumber;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public List<String> getHospitals() {
		return hospitals;
	}

	public void setHospitals(List<String> hospitals) {
		this.hospitals = hospitals;
	}

	public List<String> getSpecialist() {
		return specialist;
	}

	public void setSpecialist(List<String> specialist) {
		this.specialist = specialist;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUploadedCV() {
		return uploadedCV;
	}

	public void setUploadedCV(String uploadedCV) {
		this.uploadedCV = uploadedCV;
	}

	public UserContact getContact() {
		return contact;
	}

	public void setContact(UserContact contact) {
		this.contact = contact;
	}

	public UserGender getUserGender() {
		return userGender;
	}

	public void setUserGender(UserGender userGender) {
		this.userGender = userGender;
	}

	public UserLocation getLocation() {
		return location;
	}

	public void setLocation(UserLocation location) {
		this.location = location;
	}

	public UserTypes getTypes() {
		return types;
	}

	public void setTypes(UserTypes types) {
		this.types = types;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setName(String name) {

		this.name = name;

	}

	public String getName(String name) {

		return name;

	}

	public void setAccepted_user(String accepted_user) {

	    this.accepted_user = accepted_user;

	}

	public String getAccepted_user() {

	    return accepted_user;

	}

	@Override
	public String toString() {
		return "DoctorResponse [id=" + id + ", userId=" + userId + ", userName=" + userName + ", name=" + name
				+ ", bio=" + bio + ", bmdcRegistrationNumber=" + bmdcRegistrationNumber + ", qualification="
				+ qualification + ", hospitals=" + hospitals + ", specialist=" + specialist + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", uploadedCV=" + uploadedCV + ", contact=" + contact + ", userGender="
				+ userGender + ", location=" + location + ", types=" + types + ", accepted_user=" + accepted_user + "]";
	}

}
