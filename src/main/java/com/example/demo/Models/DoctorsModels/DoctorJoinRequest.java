package com.example.demo.Models.DoctorsModels;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import org.hibernate.annotations.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity(name = "doctor_join_request")
@Table(name = "\"doctor_join_request\"")
public class DoctorJoinRequest implements Serializable {

	private static final long serialVersionUID = 8L;

	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String id;

	@Column(name = "\"user_id\"", updatable = true, nullable = false, unique = true)
	private String userId;

	@Column(name = "bio", updatable = true, nullable = true)
	private String bio;

	@Column(name = "\"bmdc_registration_number\"", updatable = true, nullable = false, unique = true)
	private String bmdcRegistrationNumber;

	@Column(name = "qualification", updatable = true, nullable = false)
	private String qualification;

	// ✅ MUST BE String[] for Hypersistence Utils
	@Type(StringArrayType.class)
	@Column(name = "hospitals", updatable = true, nullable = true, columnDefinition = "text[]")
	private String[] hospitals = new String[0];

	// ✅ MUST BE String[] for Hypersistence Utils
	@Type(StringArrayType.class)
	@Column(name = "specialist", updatable = true, nullable = true, columnDefinition = "text[]")
	private String[] specialist = new String[0];

	@Column(name = "\"created_at\"", updatable = false, nullable = false)
	private Instant createdAt = Instant.now();

	@Column(name = "\"updated_at\"", updatable = true, nullable = true)
	private Instant updatedAt = Instant.now();

	@Column(name = "\"uploaded_cv\"", updatable = true, nullable = true)
	private String uploadedCV;

	@PrePersist
	public void generateId() {
		if (this.id == null || this.id.isBlank()) {
			this.id = UUID.randomUUID().toString();
		}
	}

	public DoctorJoinRequest(String userId, String bio, String bmdcRegistrationNumber, String qualification,
			List<String> hospitals, List<String> specialist, Instant createdAt, Instant updatedAt, String uploadedCV) {
		super();
		this.userId = userId;
		this.bio = bio;
		this.bmdcRegistrationNumber = bmdcRegistrationNumber;
		this.qualification = qualification;
		this.setHospitals(hospitals);       // ✅ Uses setter to convert List to Array
		this.setSpecialist(specialist);     // ✅ Uses setter to convert List to Array
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.uploadedCV = uploadedCV;
	}

	public DoctorJoinRequest() {
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

	// ✅ Getter converts Array -> List for your app/DTOs
	public List<String> getHospitals() {
		return hospitals == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(hospitals));
	}

	// ✅ Setter converts List -> Array for Hibernate
	public void setHospitals(List<String> hospitals) {
		this.hospitals = hospitals == null ? new String[0] : hospitals.toArray(new String[0]);
	}

	// ✅ Getter converts Array -> List for your app/DTOs
	public List<String> getSpecialist() {
		return specialist == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(specialist));
	}

	// ✅ Setter converts List -> Array for Hibernate
	public void setSpecialist(List<String> specialist) {
		this.specialist = specialist == null ? new String[0] : specialist.toArray(new String[0]);
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUploadedCV() {
		return uploadedCV;
	}

	public void setUploadedCV(String uploadedCV) {
		this.uploadedCV = uploadedCV;
	}

	@Override
	public String toString() {
		return "DoctorJoinRequest [id=" + id + ", userId=" + userId + ", bio=" + bio + ", bmdcRegistrationNumber="
				+ bmdcRegistrationNumber + ", qualification=" + qualification + ", hospitals=" + getHospitals()
				+ ", specialist=" + getSpecialist() + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", uploadedCV=" + uploadedCV + "]";
	}
}
