package com.example.demo.Repositories.DoctorsRepository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.DoctorsModels.DoctorJoinRequest;

@Repository
public interface DoctorsJoinRequestRepository extends JpaRepository<DoctorJoinRequest, String> {

	// ---------- userId ----------
	// ✅ JPQL (Standard) - Hibernate auto-maps camelCase to snake_case for DB
	@Query("SELECT d FROM doctor_join_request d WHERE d.userId = :userId")
	public DoctorJoinRequest findByUserId(@Param("userId") String userId);

	@Query("SELECT d FROM doctor_join_request d WHERE d.userId IN :usersId")
	public List<DoctorJoinRequest> findByUserIdIn(@Param("usersId") List<String> usersId);

	// ---------- bio ----------
	@Query("SELECT d FROM doctor_join_request d WHERE LOWER(d.bio) LIKE LOWER(CONCAT('%', :bio, '%'))")
	public List<DoctorJoinRequest> findByBioContainingIgnoreCase(@Param("bio") String bio);

	@Query("SELECT d FROM doctor_join_request d WHERE LOWER(d.bio) LIKE LOWER(CONCAT('%', :bio, '%'))")
	public Page<DoctorJoinRequest> findByBioContainingIgnoreCase(@Param("bio") String bio, Pageable page);

	// ---------- bmdc ----------
	@Query("SELECT d FROM doctor_join_request d WHERE d.bmdcRegistrationNumber = :bmdcRegistrationNumber")
	public DoctorJoinRequest findByBmdcRegistrationNumber(@Param("bmdcRegistrationNumber") String bmdcRegistrationNumber);

	// ---------- qualification ----------
	@Query("SELECT d FROM doctor_join_request d WHERE LOWER(d.qualification) LIKE LOWER(CONCAT('%', :qualification, '%'))")
	public List<DoctorJoinRequest> findByQualificationContainingIgnoreCase(@Param("qualification") String qualification);

	@Query("SELECT d FROM doctor_join_request d WHERE LOWER(d.qualification) LIKE LOWER(CONCAT('%', :qualification, '%'))")
	public Page<DoctorJoinRequest> findByQualificationContainingIgnoreCase(@Param("qualification") String qualification, Pageable page);

	// ---------- hospitals (text[]) -> MUST use nativeQuery ----------
	@Query(value = "SELECT * FROM \"doctor_join_request\" WHERE EXISTS (SELECT 1 FROM unnest(\"hospitals\") AS h WHERE LOWER(h) = LOWER(:hospitals))", nativeQuery = true)
	public List<DoctorJoinRequest> findByHospitalsContainingIgnoreCase(@Param("hospitals") String hospitals);

	// ✅ Added countQuery for Native Pagination
	@Query(
		value = "SELECT * FROM \"doctor_join_request\" WHERE EXISTS (SELECT 1 FROM unnest(\"hospitals\") AS h WHERE LOWER(h) = LOWER(:hospitals))"
        , nativeQuery = true
	)
	public Page<DoctorJoinRequest> findByHospitalsContainingIgnoreCase(@Param("hospitals") String hospitals, Pageable page);

	// ---------- specialist (text[]) -> MUST use nativeQuery ----------
	@Query(value = "SELECT * FROM \"doctor_join_request\" WHERE EXISTS (SELECT 1 FROM unnest(\"specialist\") AS s WHERE LOWER(s) = LOWER(:specialist))", nativeQuery = true)
	public List<DoctorJoinRequest> findBySpecialistContainingIgnoreCase(@Param("specialist") String specialist);

	// ✅ Added countQuery for Native Pagination
	@Query(
		value = "SELECT * FROM \"doctor_join_request\" WHERE EXISTS (SELECT 1 FROM unnest(\"specialist\") AS s WHERE LOWER(s) = LOWER(:specialist))"
        , nativeQuery = true
	)
	public Page<DoctorJoinRequest> findBySpecialistContainingIgnoreCase(@Param("specialist") String specialist, Pageable page);

	// ---------- createdAt ----------
	@Query("SELECT d FROM doctor_join_request d WHERE d.createdAt > :createdAt")
	public List<DoctorJoinRequest> findByCreatedAtAfter(@Param("createdAt") Instant createdAt);

	@Query("SELECT d FROM doctor_join_request d WHERE d.createdAt > :createdAt")
	public Page<DoctorJoinRequest> findByCreatedAtAfter(@Param("createdAt") Instant createdAt, Pageable page);

	@Query("SELECT d FROM doctor_join_request d WHERE d.createdAt < :createdAt")
	public List<DoctorJoinRequest> findByCreatedAtBefore(@Param("createdAt") Instant createdAt);

	@Query("SELECT d FROM doctor_join_request d WHERE d.createdAt < :createdAt")
	public Page<DoctorJoinRequest> findByCreatedAtBefore(@Param("createdAt") Instant createdAt, Pageable page);

	// ---------- updatedAt ----------
	@Query("SELECT d FROM doctor_join_request d WHERE d.updatedAt > :updatedAt")
	public List<DoctorJoinRequest> findByUpdatedAtAfter(@Param("updatedAt") Instant updatedAt);

	@Query("SELECT d FROM doctor_join_request d WHERE d.updatedAt > :updatedAt")
	public Page<DoctorJoinRequest> findByUpdatedAtAfter(@Param("updatedAt") Instant updatedAt, Pageable page);

	@Query("SELECT d FROM doctor_join_request d WHERE d.updatedAt < :updatedAt")
	public List<DoctorJoinRequest> findByUpdatedAtBefore(@Param("updatedAt") Instant updatedAt);

	@Query("SELECT d FROM doctor_join_request d WHERE d.updatedAt < :updatedAt")
	public Page<DoctorJoinRequest> findByUpdatedAtBefore(@Param("updatedAt") Instant updatedAt, Pageable page);

	// ---------- uploadedCV ----------
	@Query("SELECT d FROM doctor_join_request d WHERE d.uploadedCV = :uploadedCV")
	public List<DoctorJoinRequest> findByUploadedCV(@Param("uploadedCV") String uploadedCV);

}
