package com.example.demo.Services.DoctorsServices;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTOFiles.DoctorJoinRequestPageResponse;
import com.example.demo.DTOFiles.DoctorJoinRequestResponse;
import com.example.demo.Models.DoctorsModels.Doctor;
import com.example.demo.Models.DoctorsModels.DoctorJoinRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface DoctorsJoinRequestService {

	public DoctorJoinRequest sendRequest(DoctorJoinRequest request, MultipartFile cv, String userId);
	public DoctorJoinRequest updateRequest(String id, DoctorJoinRequest request, MultipartFile cv, String userId);
	public Doctor acceptRequest(String id, String userId, boolean status);
	
	public DoctorJoinRequestResponse findById(String id);
	
	public List<DoctorJoinRequestResponse> findAll();
	public DoctorJoinRequestPageResponse findAll(Pageable page, HttpServletRequest http);
	
	public DoctorJoinRequestResponse findByUserId(String userId);
	public List<DoctorJoinRequestResponse> findByUserIdIn(List<String> usersId);
	public List<DoctorJoinRequestResponse> findByBioContainingIgnoreCase(String bio);
	public DoctorJoinRequestResponse findByBmdcRegistrationNumber(String bmdcRegistrationNumber);
	
	public List<DoctorJoinRequestResponse> findByQualificationContainingIgnoreCase(String qualification);
	public DoctorJoinRequestPageResponse findByQualificationContainingIgnoreCase(String qualification, Pageable pagebale, HttpServletRequest http);
	
	public List<DoctorJoinRequestResponse> findByHospitalsContainingIgnoreCase(String hospitals);
	public DoctorJoinRequestPageResponse findByHospitalsContainingIgnoreCase(String hospitals, Pageable pageable, HttpServletRequest http);
	
	public List<DoctorJoinRequestResponse> findBySpecialistContainingIgnoreCase(String specialist);
	public DoctorJoinRequestPageResponse findBySpecialistContainingIgnoreCase(String specialist, Pageable pageable, HttpServletRequest http);
	
	public List<DoctorJoinRequestResponse> findByCreatedAtAfter(Instant createdAt);
	public DoctorJoinRequestPageResponse findByCreatedAtAfter(Instant createdAt, Pageable page, HttpServletRequest http);
	
	public List<DoctorJoinRequestResponse> findByCreatedAtBefore(Instant createdAt);
	public DoctorJoinRequestPageResponse findByCreatedAtBefore(Instant createdAt, Pageable page, HttpServletRequest http);
	
	public List<DoctorJoinRequestResponse> findByUpdatedAtAfter(Instant updatedAt);
	public DoctorJoinRequestPageResponse findByUpdatedAtAfter(Instant updatedAt, Pageable page, HttpServletRequest http);
	
	public List<DoctorJoinRequestResponse> findByUpdatedAtBefore(Instant updatedAt);
	public DoctorJoinRequestPageResponse findByUpdatedAtBefore(Instant updatedAt, Pageable page, HttpServletRequest http);
	
	
	public List<DoctorJoinRequestResponse> findByUploadedCV(String uploadedCV);
	
	public boolean removeRequest(String id, String userId);
	
}
