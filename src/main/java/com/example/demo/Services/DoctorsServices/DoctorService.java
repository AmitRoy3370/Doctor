package com.example.demo.Services.DoctorsServices;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTOFiles.DoctorPageResponse;
import com.example.demo.DTOFiles.DoctorResponse;
import com.example.demo.Models.DoctorsModels.Doctor;
import com.example.demo.Models.DoctorsModels.DoctorJoinRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface DoctorService {

	public Doctor updateDoctor(String id, Doctor doctor, String userId, MultipartFile cv);

	public DoctorResponse findById(String id);
	public List<DoctorResponse> findAll();
	public DoctorPageResponse findAll(Pageable page, HttpServletRequest http);
	public DoctorResponse findByUserId(String userId);
	public List<DoctorResponse> findByUserIdIn(List<String> usersId);
	public List<DoctorResponse> findByBioContainingIgnoreCase(String bio);
	public DoctorPageResponse findByBioContainingIgnoreCase(String bio, Pageable page, HttpServletRequest http);
	public DoctorResponse findByBmdcRegistrationNumber(String bmdcRegistrationNumber);
	public List<DoctorResponse> findByQualificationContainingIgnoreCase(String qualification);
	public DoctorPageResponse findByQualificationContainingIgnoreCase(String qualification, Pageable page, HttpServletRequest http);
	public List<DoctorResponse> findByHospitalsContainingIgnoreCase(String hospitals);
	public DoctorPageResponse findByHospitalsContainingIgnoreCase(String hospitals, Pageable page, HttpServletRequest http);
	public List<DoctorResponse> findBySpecialistContainingIgnoreCase(String specialist);
	public DoctorPageResponse findBySpecialistContainingIgnoreCase(String specialist, Pageable page, HttpServletRequest http);
	public List<DoctorResponse> findByCreatedAtAfter(Instant createdAt);
	public DoctorPageResponse findByCreatedAtAfter(Instant createdAt, Pageable page, HttpServletRequest http);
	public List<DoctorResponse> findByCreatedAtBefore(Instant createdAt);
	public DoctorPageResponse findByCreatedAtBefore(Instant createdAt, Pageable page, HttpServletRequest http);
	public List<DoctorResponse> findByUpdatedAtAfter(Instant updatedAt);
	public DoctorPageResponse findByUpdatedAtAfter(Instant updatedAt, Pageable page, HttpServletRequest http);
	public List<DoctorResponse> findByUpdatedAtBefore(Instant updatedAt);
	public DoctorPageResponse findByUpdatedAtBefore(Instant updatedAt, Pageable page, HttpServletRequest http);
	public List<DoctorResponse> findByUploadedCV(String uploadedCV);
    public List<DoctorResponse> findByAccepted_user(String accepted_user);
	public DoctorPageResponse findByAccepted_user(String accepted_user, Pageable page, HttpServletRequest http);

	public boolean deleteDoctor(String id, String userId);

}
