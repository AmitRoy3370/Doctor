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
import com.example.demo.DTOFiles.DoctorJoinRequestPageResponse;
import com.example.demo.DTOFiles.DoctorJoinRequestResponse;
import com.example.demo.Models.DoctorsModels.Doctor;
import com.example.demo.Models.DoctorsModels.DoctorJoinRequest;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Services.DoctorsServices.DoctorsJoinRequestService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/doctor/join-requests")
public class DoctorJoinRequestController {

    @Autowired
    private DoctorsJoinRequestService joinRequestService;

    // ==================== CREATE / UPDATE ====================

    @PostMapping
    public ResponseEntity<?> sendRequest(
            @RequestPart("request") DoctorJoinRequest request,
            @RequestPart(value = "cv", required = false) MultipartFile cv,
            @AuthenticationPrincipal User user) {
        try {
            String userId = user.getId();
            DoctorJoinRequest savedRequest = joinRequestService.sendRequest(request, cv, userId);
            return ResponseEntity.ok(savedRequest);
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRequest(
            @PathVariable String id,
            @RequestPart("request") DoctorJoinRequest request,
            @RequestPart(value = "cv", required = false) MultipartFile cv,
            @AuthenticationPrincipal User user) {
        try {
            String userId = user.getId();
            DoctorJoinRequest updatedRequest = joinRequestService.updateRequest(id, request, cv, userId);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<?> acceptRequest(
            @PathVariable String id,
            @RequestParam boolean status,
            @AuthenticationPrincipal User user) {
        try {
            String userId = user.getId();

            Doctor doctor = joinRequestService.acceptRequest(id, userId, status);
            return ResponseEntity.ok(doctor);
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeRequest(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        try {
            String userId = user.getId();
            boolean removed = joinRequestService.removeRequest(id, userId);

            if (removed) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    // ==================== FIND BY ID / USER ====================

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(joinRequestService.findById(id));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(joinRequestService.findByUserId(userId));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> findByUserIdIn(@RequestBody List<String> userIds) {
        try {
            return ResponseEntity.ok(joinRequestService.findByUserIdIn(userIds));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/bmdc/{bmdcNumber}")
    public ResponseEntity<?> findByBmdcRegistrationNumber(@PathVariable String bmdcNumber) {
        try {
            return ResponseEntity.ok(joinRequestService.findByBmdcRegistrationNumber(bmdcNumber));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/cv/{uploadedCV}")
    public ResponseEntity<?> findByUploadedCV(@PathVariable String uploadedCV) {
        try {
            return ResponseEntity.ok(joinRequestService.findByUploadedCV(uploadedCV));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    // ==================== FIND ALL (PAGINATED & LIST) ====================

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
            return ResponseEntity.ok(joinRequestService.findAll(pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAllList() {
        try {
            return ResponseEntity.ok(joinRequestService.findAll());
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    // ==================== SEARCHES ====================

    @GetMapping("/search/bio")
    public ResponseEntity<?> findByBio(@RequestParam String bio) {
        try {
            return ResponseEntity.ok(joinRequestService.findByBioContainingIgnoreCase(bio));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/qualification")
    public ResponseEntity<?> findByQualification(@RequestParam String qualification) {
        try {
            return ResponseEntity.ok(joinRequestService.findByQualificationContainingIgnoreCase(qualification));
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
            return ResponseEntity.ok(joinRequestService.findByQualificationContainingIgnoreCase(qualification, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/hospital")
    public ResponseEntity<?> findByHospital(@RequestParam String hospital) {
        try {
            return ResponseEntity.ok(joinRequestService.findByHospitalsContainingIgnoreCase(hospital));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/hospital/paged")
    public ResponseEntity<?> findByHospitalPaged(
            @RequestParam String hospital,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(joinRequestService.findByHospitalsContainingIgnoreCase(hospital, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/specialist")
    public ResponseEntity<?> findBySpecialist(@RequestParam String specialist) {
        try {
            return ResponseEntity.ok(joinRequestService.findBySpecialistContainingIgnoreCase(specialist));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/specialist/paged")
    public ResponseEntity<?> findBySpecialistPaged(
            @RequestParam String specialist,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest http) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(joinRequestService.findBySpecialistContainingIgnoreCase(specialist, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    // ==================== TIME SEARCHES (CreatedAt / UpdatedAt) ====================

    @GetMapping("/search/created-after")
    public ResponseEntity<?> findByCreatedAtAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAt) {
        try {
            return ResponseEntity.ok(joinRequestService.findByCreatedAtAfter(createdAt));
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
            return ResponseEntity.ok(joinRequestService.findByCreatedAtAfter(createdAt, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/created-before")
    public ResponseEntity<?> findByCreatedAtBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAt) {
        try {
            return ResponseEntity.ok(joinRequestService.findByCreatedAtBefore(createdAt));
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
            return ResponseEntity.ok(joinRequestService.findByCreatedAtBefore(createdAt, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/updated-after")
    public ResponseEntity<?> findByUpdatedAtAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant updatedAt) {
        try {
            return ResponseEntity.ok(joinRequestService.findByUpdatedAtAfter(updatedAt));
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
            return ResponseEntity.ok(joinRequestService.findByUpdatedAtAfter(updatedAt, pageable, http));
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/search/updated-before")
    public ResponseEntity<?> findByUpdatedAtBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant updatedAt) {
        try {
            return ResponseEntity.ok(joinRequestService.findByUpdatedAtBefore(updatedAt));
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
            return ResponseEntity.ok(joinRequestService.findByUpdatedAtBefore(updatedAt, pageable, http));
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
