package com.example.demo.Repositories.DoctorsRepository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.DoctorsModels.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {

	// ---------- userId ----------
	// ✅ JPQL (Standard) - Hibernate auto-maps camelCase to snake_case for DB
	@Query("SELECT d FROM doctor d WHERE d.userId = :userId")
	public Doctor findByUserId(@Param("userId") String userId);

	@Query("SELECT d FROM doctor d WHERE d.userId IN :usersId")
	public List<Doctor> findByUserIdIn(@Param("usersId") List<String> usersId);

		// ---------- accepted_user ----------
	@Query("SELECT d FROM doctor d WHERE d.accepted_user = :accepted_user")
	public List<Doctor> findByAccepted_user(@Param("accepted_user") String accepted_user);

	@Query("SELECT d FROM doctor d WHERE d.accepted_user = :accepted_user")
	public Page<Doctor> findByAccepted_user(@Param("accepted_user") String accepted_user, Pageable page);

	// ---------- bio ----------
	@Query("SELECT d FROM doctor d WHERE LOWER(d.bio) LIKE LOWER(CONCAT('%', :bio, '%'))")
	public List<Doctor> findByBioContainingIgnoreCase(@Param("bio") String bio);

	@Query("SELECT d FROM doctor d WHERE LOWER(d.bio) LIKE LOWER(CONCAT('%', :bio, '%'))")
	public Page<Doctor> findByBioContainingIgnoreCase(@Param("bio") String bio, Pageable page);

	// ---------- bmdc ----------
	@Query("SELECT d FROM doctor d WHERE d.bmdcRegistrationNumber = :bmdcRegistrationNumber")
	public Doctor findByBmdcRegistrationNumber(@Param("bmdcRegistrationNumber") String bmdcRegistrationNumber);

	// ---------- qualification ----------
	@Query("SELECT d FROM doctor d WHERE LOWER(d.qualification) LIKE LOWER(CONCAT('%', :qualification, '%'))")
	public List<Doctor> findByQualificationContainingIgnoreCase(@Param("qualification") String qualification);

	@Query("SELECT d FROM doctor d WHERE LOWER(d.qualification) LIKE LOWER(CONCAT('%', :qualification, '%'))")
	public Page<Doctor> findByQualificationContainingIgnoreCase(@Param("qualification") String qualification, Pageable page);

	// ---------- hospitals (text[]) -> MUST use nativeQuery ----------
	@Query(value = "SELECT * FROM \"doctor\" WHERE EXISTS (SELECT 1 FROM unnest(\"hospitals\") AS h WHERE LOWER(h) = LOWER(:hospitals))", nativeQuery = true)
	public List<Doctor> findByHospitalsContainingIgnoreCase(@Param("hospitals") String hospitals);

	// ✅ Added countQuery for Native Pagination
	@Query(
		value = "SELECT * FROM \"doctor\" WHERE EXISTS (SELECT 1 FROM unnest(\"hospitals\") AS h WHERE LOWER(h) = LOWER(:hospitals))",
		countQuery = "SELECT COUNT(*) FROM \"doctor\" WHERE EXISTS (SELECT 1 FROM unnest(\"hospitals\") AS h WHERE LOWER(h) = LOWER(:hospitals))",
		nativeQuery = true
	)
	public Page<Doctor> findByHospitalsContainingIgnoreCase(@Param("hospitals") String hospitals, Pageable page);

	// ---------- specialist (text[]) -> MUST use nativeQuery ----------
	@Query(value = "SELECT * FROM \"doctor\" WHERE EXISTS (SELECT 1 FROM unnest(\"specialist\") AS s WHERE LOWER(s) = LOWER(:specialist))", nativeQuery = true)
	public List<Doctor> findBySpecialistContainingIgnoreCase(@Param("specialist") String specialist);

	// ✅ Added countQuery for Native Pagination
	@Query(
		value = "SELECT * FROM \"doctor\" WHERE EXISTS (SELECT 1 FROM unnest(\"specialist\") AS s WHERE LOWER(s) = LOWER(:specialist))",
		countQuery = "SELECT COUNT(*) FROM \"doctor\" WHERE EXISTS (SELECT 1 FROM unnest(\"specialist\") AS s WHERE LOWER(s) = LOWER(:specialist))",
		nativeQuery = true
	)
	public Page<Doctor> findBySpecialistContainingIgnoreCase(@Param("specialist") String specialist, Pageable page);

	// ---------- createdAt ----------
	@Query("SELECT d FROM doctor d WHERE d.createdAt > :createdAt")
	public List<Doctor> findByCreatedAtAfter(@Param("createdAt") Instant createdAt);

	@Query("SELECT d FROM doctor d WHERE d.createdAt > :createdAt")
	public Page<Doctor> findByCreatedAtAfter(@Param("createdAt") Instant createdAt, Pageable page);

	@Query("SELECT d FROM doctor d WHERE d.createdAt < :createdAt")
	public List<Doctor> findByCreatedAtBefore(@Param("createdAt") Instant createdAt);

	@Query("SELECT d FROM doctor d WHERE d.createdAt < :createdAt")
	public Page<Doctor> findByCreatedAtBefore(@Param("createdAt") Instant createdAt, Pageable page);

	// ---------- updatedAt ----------
	@Query("SELECT d FROM doctor d WHERE d.updatedAt > :updatedAt")
	public List<Doctor> findByUpdatedAtAfter(@Param("updatedAt") Instant updatedAt);

	@Query("SELECT d FROM doctor d WHERE d.updatedAt > :updatedAt")
	public Page<Doctor> findByUpdatedAtAfter(@Param("updatedAt") Instant updatedAt, Pageable page);

	@Query("SELECT d FROM doctor d WHERE d.updatedAt < :updatedAt")
	public List<Doctor> findByUpdatedAtBefore(@Param("updatedAt") Instant updatedAt);

	@Query("SELECT d FROM doctor d WHERE d.updatedAt < :updatedAt")
	public Page<Doctor> findByUpdatedAtBefore(@Param("updatedAt") Instant updatedAt, Pageable page);

	// ---------- uploadedCV ----------
	@Query("SELECT d FROM doctor d WHERE d.uploadedCV = :uploadedCV")
	public List<Doctor> findByUploadedCV(@Param("uploadedCV") String uploadedCV);

}
