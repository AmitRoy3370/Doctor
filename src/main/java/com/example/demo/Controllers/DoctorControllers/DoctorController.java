package com.example.demo.Controllers.DoctorControllers;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.DTOFiles.DoctorPageResponse;
import com.example.demo.DTOFiles.DoctorResponse;
import com.example.demo.Models.DoctorsModels.Doctor;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Services.DoctorsServices.DoctorService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // ==================== UPDATE (PUT) ====================

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable String id,
            @RequestPart("doctor") Doctor doctor,
            @RequestPart(value = "cv", required = false) MultipartFile cv,
            @AuthenticationPrincipal User user) {
        try {
            String userId = user.getId();
            System.out.println("sending userId:- " + userId);
            System.out.println("requested doctor:- " + doctor.toString());
            Doctor updatedDoctor = doctorService.updateDoctor(id, doctor, userId, cv);
            return ResponseEntity.ok(updatedDoctor);
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    // ==================== DELETE ====================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        try {
            String userId = user.getId();
            boolean deleted = doctorService.deleteDoctor(id, userId);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    // ==================== GET: FIND BY ID / USER ====================

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(doctorService.findById(id));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(doctorService.findByUserId(userId));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> findByUserIdIn(@RequestParam List<String> userIds) {
        try {
            return ResponseEntity.ok(doctorService.findByUserIdIn(userIds));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/bmdc/{bmdcNumber}")
    public ResponseEntity<?> findByBmdcRegistrationNumber(@PathVariable String bmdcNumber) {
        try {
            return ResponseEntity.ok(doctorService.findByBmdcRegistrationNumber(bmdcNumber));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/cv/{uploadedCV}")
    public ResponseEntity<?> findByUploadedCV(@PathVariable String uploadedCV) {
        try {
            return ResponseEntity.ok(doctorService.findByUploadedCV(uploadedCV));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    // ==================== GET: FIND ALL (PAGINATED & LIST) ====================

    @GetMapping
    public ResponseEntity<?> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDir);
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            return ResponseEntity.ok(doctorService.findAll(pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAllList() {
        try {
            return ResponseEntity.ok(doctorService.findAll());
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    // ==================== GET: SEARCHES ====================

    @GetMapping("/search/bio")
    public ResponseEntity<?> findByBio(@RequestParam String bio) {
        try {
            return ResponseEntity.ok(doctorService.findByBioContainingIgnoreCase(bio));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/bio/paged")
    public ResponseEntity<?> findByBioPaged(
            @RequestParam String bio,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(doctorService.findByBioContainingIgnoreCase(bio, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/qualification")
    public ResponseEntity<?> findByQualification(@RequestParam String qualification) {
        try {
            return ResponseEntity.ok(doctorService.findByQualificationContainingIgnoreCase(qualification));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/qualification/paged")
    public ResponseEntity<?> findByQualificationPaged(
            @RequestParam String qualification,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(doctorService.findByQualificationContainingIgnoreCase(qualification, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

        // ==================== ACCEPTED USER SEARCH ====================

    @GetMapping("/accepted-user/{accepted_user}")
    public ResponseEntity<?> findByAccepted_user(@PathVariable String accepted_user) {
        try {
            return ResponseEntity.ok(doctorService.findByAccepted_user(accepted_user));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/accepted-user/{accepted_user}/paged")
    public ResponseEntity<?> findByAccepted_userPaged(
            @PathVariable String accepted_user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            // Standard camelCase since this uses JPQL, not Native Query
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(doctorService.findByAccepted_user(accepted_user, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/hospital")
    public ResponseEntity<?> findByHospital(@RequestParam String hospital) {
        try {
            return ResponseEntity.ok(doctorService.findByHospitalsContainingIgnoreCase(hospital));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/hospital/paged")
    public ResponseEntity<?> findByHospitalPaged(
            @RequestParam String hospital,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            // ⚠️ Using DB column name because this repository method is a Native Query
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(doctorService.findByHospitalsContainingIgnoreCase(hospital, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/specialist")
    public ResponseEntity<?> findBySpecialist(@RequestParam String specialist) {
        try {
            return ResponseEntity.ok(doctorService.findBySpecialistContainingIgnoreCase(specialist));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/specialist/paged")
    public ResponseEntity<?> findBySpecialistPaged(
            @RequestParam String specialist,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            // ⚠️ Using DB column name because this repository method is a Native Query
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(doctorService.findBySpecialistContainingIgnoreCase(specialist, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    // ==================== GET: TIME SEARCHES (CreatedAt / UpdatedAt) ====================

    @GetMapping("/search/created-after")
    public ResponseEntity<?> findByCreatedAtAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAt) {
        try {
            return ResponseEntity.ok(doctorService.findByCreatedAtAfter(createdAt));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/created-after/paged")
    public ResponseEntity<?> findByCreatedAtAfterPaged(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(doctorService.findByCreatedAtAfter(createdAt, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/created-before")
    public ResponseEntity<?> findByCreatedAtBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAt) {
        try {
            return ResponseEntity.ok(doctorService.findByCreatedAtBefore(createdAt));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/created-before/paged")
    public ResponseEntity<?> findByCreatedAtBeforePaged(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(doctorService.findByCreatedAtBefore(createdAt, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/updated-after")
    public ResponseEntity<?> findByUpdatedAtAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant updatedAt) {
        try {
            return ResponseEntity.ok(doctorService.findByUpdatedAtAfter(updatedAt));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/updated-after/paged")
    public ResponseEntity<?> findByUpdatedAtAfterPaged(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant updatedAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(doctorService.findByUpdatedAtAfter(updatedAt, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/updated-before")
    public ResponseEntity<?> findByUpdatedAtBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant updatedAt) {
        try {
            return ResponseEntity.ok(doctorService.findByUpdatedAtBefore(updatedAt));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/updated-before/paged")
    public ResponseEntity<?> findByUpdatedAtBeforePaged(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant updatedAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(doctorService.findByUpdatedAtBefore(updatedAt, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    // ==================== ERROR HANDLING HELPER ====================

    /**
     * Helper method to convert Java Exceptions into clean, structured JSON responses.
     */
    private ResponseEntity<?> buildErrorResponse(Exception e) {
        String errorMessage = e.getMessage() != null ? e.getMessage() : "An unexpected error occurred";

        // Map specific exceptions to proper HTTP Status codes
        HttpStatus status;
        if (e instanceof NoSuchElementException) {
            status = HttpStatus.NOT_FOUND; // 404
        } else if (e instanceof NullPointerException || e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST; // 400
        } else if (e instanceof ArithmeticException) {
            status = HttpStatus.CONFLICT; // 409
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        }

        // You can return a Map or a custom DTO here. A Map is easiest.
        return ResponseEntity.status(status).body(
                java.util.Map.of(
                        "status", status.value(),
                        "error", status.getReasonPhrase(),
                        "message", errorMessage
                )
        );
    }
}
