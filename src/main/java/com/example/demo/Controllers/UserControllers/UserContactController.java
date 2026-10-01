package com.example.demo.Controllers.UserControllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.UserModels.UserContact;
import com.example.demo.Services.UserServices.UserContactService;

@RestController
@RequestMapping("/api/user-contacts")
public class UserContactController {

    @Autowired
    private UserContactService userContactService;

    // ===============================
    // CREATE ENDPOINTS
    // ===============================

    /**
     * Create user contact
     * POST /api/user-contacts
     */
    @PostMapping
    public ResponseEntity<?> createUserContact(
            @RequestBody UserContact userContact,
            @RequestParam String userId) {
        try {
            UserContact created = userContactService.createUserContact(userContact, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (ArithmeticException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to create contact: " + e.getMessage()));
        }
    }

    // ===============================
    // GET ENDPOINTS
    // ===============================

    /**
     * Get contact by ID
     * GET /api/user-contacts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getContactById(@PathVariable String id) {
        try {
            UserContact contact = userContactService.findContactById(id);
            return ResponseEntity.ok(contact);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contact: " + e.getMessage()));
        }
    }

    /**
     * Get contact by userId
     * GET /api/user-contacts/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getContactByUserId(@PathVariable String userId) {
        try {
            UserContact contact = userContactService.findByUserId(userId);
            return ResponseEntity.ok(contact);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contact: " + e.getMessage()));
        }
    }

    /**
     * Get contact by email
     * GET /api/user-contacts/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getContactByEmail(@PathVariable String email) {
        try {
            UserContact contact = userContactService.findByEmail(email);
            return ResponseEntity.ok(contact);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contact: " + e.getMessage()));
        }
    }

    /**
     * Get contact by email (case insensitive)
     * GET /api/user-contacts/email/ignorecase/{email}
     */
    @GetMapping("/email/ignorecase/{email}")
    public ResponseEntity<?> getContactByEmailIgnoreCase(@PathVariable String email) {
        try {
            UserContact contact = userContactService.findByEmailIgnoreCase(email);
            return ResponseEntity.ok(contact);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contact: " + e.getMessage()));
        }
    }

    /**
     * Get contact by phone
     * GET /api/user-contacts/phone/{phone}
     */
    @GetMapping("/phone/{phone}")
    public ResponseEntity<?> getContactByPhone(@PathVariable String phone) {
        try {
            UserContact contact = userContactService.findByPhone(phone);
            return ResponseEntity.ok(contact);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contact: " + e.getMessage()));
        }
    }

    /**
     * Get contact by phone (case insensitive)
     * GET /api/user-contacts/phone/ignorecase/{phone}
     */
    @GetMapping("/phone/ignorecase/{phone}")
    public ResponseEntity<?> getContactByPhoneIgnoreCase(@PathVariable String phone) {
        try {
            UserContact contact = userContactService.findByPhoneIgnoreCase(phone);
            return ResponseEntity.ok(contact);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contact: " + e.getMessage()));
        }
    }

    // ===============================
    // SEARCH ENDPOINTS
    // ===============================

    /**
     * Search contacts by keyword
     * GET /api/user-contacts/search?keyword=john
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchContacts(@RequestParam String keyword) {
        try {
            List<UserContact> contacts = userContactService.searchContacts(keyword);
            return ResponseEntity.ok(contacts);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Search failed: " + e.getMessage()));
        }
    }

    /**
     * Search contacts by email prefix
     * GET /api/user-contacts/search/email?email=john
     */
    @GetMapping("/search/email")
    public ResponseEntity<?> searchByEmailPrefix(@RequestParam String email) {
        try {
            List<UserContact> contacts = userContactService.findBysearchByEmailPrefix(email);
            return ResponseEntity.ok(contacts);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Search failed: " + e.getMessage()));
        }
    }

    /**
     * Search contacts by phone prefix
     * GET /api/user-contacts/search/phone?phone=123
     */
    @GetMapping("/search/phone")
    public ResponseEntity<?> searchByPhonePrefix(@RequestParam String phone) {
        try {
            List<UserContact> contacts = userContactService.findByPhonePrefix(phone);
            return ResponseEntity.ok(contacts);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Search failed: " + e.getMessage()));
        }
    }

    /**
     * Search user contacts by userId and keyword
     * GET /api/user-contacts/search/user?userId=123&keyword=john
     */
    @GetMapping("/search/user")
    public ResponseEntity<?> searchUserContacts(
            @RequestParam String userId,
            @RequestParam String keyword) {
        try {
            List<UserContact> contacts = userContactService.findByUserContactKeyWord(userId, keyword);
            return ResponseEntity.ok(contacts);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Search failed: " + e.getMessage()));
        }
    }

    // ===============================
    // GET ALL ENDPOINTS WITH ORDERING
    // ===============================

    /**
     * Get all contacts ordered by userId ascending
     * GET /api/user-contacts/all/userid/asc
     */
    @GetMapping("/all/userid/asc")
    public ResponseEntity<?> getAllContactsOrderByUserIdAsc() {
        try {
            List<UserContact> contacts = userContactService.getAllContactsOrderByUserIdAsc();
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contacts: " + e.getMessage()));
        }
    }

    /**
     * Get all contacts ordered by userId descending
     * GET /api/user-contacts/all/userid/desc
     */
    @GetMapping("/all/userid/desc")
    public ResponseEntity<?> getAllContactsOrderByUserIdDesc() {
        try {
            List<UserContact> contacts = userContactService.getAllContactsOrderByUserIdDesc();
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contacts: " + e.getMessage()));
        }
    }

    /**
     * Get all contacts ordered by email ascending
     * GET /api/user-contacts/all/email/asc
     */
    @GetMapping("/all/email/asc")
    public ResponseEntity<?> getAllContactsOrderByEmailAsc() {
        try {
            List<UserContact> contacts = userContactService.getAllContactsOrderByEmailAsc();
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contacts: " + e.getMessage()));
        }
    }

    /**
     * Get all contacts ordered by email descending
     * GET /api/user-contacts/all/email/desc
     */
    @GetMapping("/all/email/desc")
    public ResponseEntity<?> getAllContactsOrderByEmailDesc() {
        try {
            List<UserContact> contacts = userContactService.getAllContactsOrderByEmailDesc();
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contacts: " + e.getMessage()));
        }
    }

    // ===============================
    // FILTER ENDPOINTS
    // ===============================

    /**
     * Get users with email
     * GET /api/user-contacts/filter/with-email
     */
    @GetMapping("/filter/with-email")
    public ResponseEntity<?> findUsersWithEmail() {
        try {
            List<UserContact> contacts = userContactService.findUsersWithEmail();
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contacts: " + e.getMessage()));
        }
    }

    /**
     * Get users with phone
     * GET /api/user-contacts/filter/with-phone
     */
    @GetMapping("/filter/with-phone")
    public ResponseEntity<?> findUsersWithPhone() {
        try {
            List<UserContact> contacts = userContactService.findUsersWithPhone();
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contacts: " + e.getMessage()));
        }
    }

    /**
     * Get users without contact info
     * GET /api/user-contacts/filter/without-contact
     */
    @GetMapping("/filter/without-contact")
    public ResponseEntity<?> findUsersWithoutContactInfo() {
        try {
            List<UserContact> contacts = userContactService.findUsersWithoutContactInfo();
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contacts: " + e.getMessage()));
        }
    }

    /**
     * Get users with both email and phone
     * GET /api/user-contacts/filter/with-both
     */
    @GetMapping("/filter/with-both")
    public ResponseEntity<?> findUsersWithBothEmailAndPhone() {
        try {
            List<UserContact> contacts = userContactService.findUsersWithBothEmailAndPhone();
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contacts: " + e.getMessage()));
        }
    }

    /**
     * Get users with missing contact info
     * GET /api/user-contacts/filter/with-missing
     */
    @GetMapping("/filter/with-missing")
    public ResponseEntity<?> findUsersWithMissingContactInfo() {
        try {
            List<UserContact> contacts = userContactService.findUsersWithMissingContactInfo();
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch contacts: " + e.getMessage()));
        }
    }

    // ===============================
    // UPDATE ENDPOINTS
    // ===============================

    /**
     * Update email by userId
     * PUT /api/user-contacts/{userId}/email
     */
    @PutMapping("/{userId}/email")
    public ResponseEntity<?> updateEmailByUserId(
            @PathVariable String userId,
            @RequestParam String email) {
        try {
            UserContact updated = userContactService.updateEmailByUserId(email, userId);
            return ResponseEntity.ok(updated);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (ArithmeticException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Update failed: " + e.getMessage()));
        }
    }

    /**
     * Update phone by userId
     * PUT /api/user-contacts/{userId}/phone
     */
    @PutMapping("/{userId}/phone")
    public ResponseEntity<?> updatePhoneByUserId(
            @PathVariable String userId,
            @RequestParam String phone) {
        try {
            UserContact updated = userContactService.updatePhoneByUserId(phone, userId);
            return ResponseEntity.ok(updated);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (ArithmeticException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Update failed: " + e.getMessage()));
        }
    }

    /**
     * Update full contact by userId
     * PUT /api/user-contacts/{userId}/contact
     */
    @PutMapping("/{userId}/contact")
    public ResponseEntity<?> updateContactByUserId(
            @PathVariable String userId,
            @RequestBody UserContact userContact) {
        try {
            UserContact updated = userContactService.updateContactByUserId(userId, userContact);
            return ResponseEntity.ok(updated);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (ArithmeticException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Update failed: " + e.getMessage()));
        }
    }

    // ===============================
    // REMOVE ENDPOINTS
    // ===============================

    /**
     * Remove email by userId
     * DELETE /api/user-contacts/{userId}/email
     */
    @DeleteMapping("/{userId}/email")
    public ResponseEntity<?> removeEmailByUserId(@PathVariable String userId) {
        try {
            boolean removed = userContactService.removeEmailByUserId(userId);
            if (removed) {
                return ResponseEntity.ok(successResponse("Email removed successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse("Failed to remove email"));
            }
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Remove failed: " + e.getMessage()));
        }
    }

    /**
     * Remove phone by userId
     * DELETE /api/user-contacts/{userId}/phone
     */
    @DeleteMapping("/{userId}/phone")
    public ResponseEntity<?> removePhoneByUserId(@PathVariable String userId) {
        try {
            boolean removed = userContactService.removePhoneByUserId(userId);
            if (removed) {
                return ResponseEntity.ok(successResponse("Phone removed successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse("Failed to remove phone"));
            }
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Remove failed: " + e.getMessage()));
        }
    }

    // ===============================
    // DELETE ENDPOINTS
    // ===============================

    /**
     * Delete contact by ID
     * DELETE /api/user-contacts/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContactById(@PathVariable String id) {
        try {
            boolean deleted = userContactService.deleteContactById(id);
            if (deleted) {
                return ResponseEntity.ok(successResponse("Contact deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse("Failed to delete contact"));
            }
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Delete failed: " + e.getMessage()));
        }
    }

    /**
     * Delete contact by userId
     * DELETE /api/user-contacts/user/{userId}
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteContactByUserId(@PathVariable String userId) {
        try {
            boolean deleted = userContactService.deleteContactByUserId(userId);
            if (deleted) {
                return ResponseEntity.ok(successResponse("Contact deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse("Failed to delete contact"));
            }
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Delete failed: " + e.getMessage()));
        }
    }

    /**
     * Delete contact by email
     * DELETE /api/user-contacts/email/{email}
     */
    @DeleteMapping("/email/{email}")
    public ResponseEntity<?> deleteContactByEmail(@PathVariable String email) {
        try {
            boolean deleted = userContactService.deleteContactByEmail(email);
            if (deleted) {
                return ResponseEntity.ok(successResponse("Contact deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse("Failed to delete contact"));
            }
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (ArithmeticException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Delete failed: " + e.getMessage()));
        }
    }

    /**
     * Delete contact by phone
     * DELETE /api/user-contacts/phone/{phone}
     */
    @DeleteMapping("/phone/{phone}")
    public ResponseEntity<?> deleteContactByPhone(@PathVariable String phone) {
        try {
            boolean deleted = userContactService.deleteContactByPhone(phone);
            if (deleted) {
                return ResponseEntity.ok(successResponse("Contact deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse("Failed to delete contact"));
            }
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (ArithmeticException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Delete failed: " + e.getMessage()));
        }
    }

    /**
     * Delete contacts without info
     * DELETE /api/user-contacts/without-info
     */
    @DeleteMapping("/without-info")
    public ResponseEntity<?> deleteContactsWithoutInfo() {
        try {
            boolean deleted = userContactService.deleteContactsWithoutInfo();
            if (deleted) {
                return ResponseEntity.ok(successResponse("Contacts without info deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse("Failed to delete contacts"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Delete failed: " + e.getMessage()));
        }
    }

    /**
     * Delete contacts without email
     * DELETE /api/user-contacts/without-email
     */
    @DeleteMapping("/without-email")
    public ResponseEntity<?> deleteContactsWithoutEmail() {
        try {
            boolean deleted = userContactService.deleteContactsWithoutEmail();
            if (deleted) {
                return ResponseEntity.ok(successResponse("Contacts without email deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse("Failed to delete contacts"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Delete failed: " + e.getMessage()));
        }
    }

    /**
     * Delete contacts without phone
     * DELETE /api/user-contacts/without-phone
     */
    @DeleteMapping("/without-phone")
    public ResponseEntity<?> deleteContactsWithoutPhone() {
        try {
            boolean deleted = userContactService.deleteContactsWithoutPhone();
            if (deleted) {
                return ResponseEntity.ok(successResponse("Contacts without phone deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse("Failed to delete contacts"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Delete failed: " + e.getMessage()));
        }
    }

    // ===============================
    // EXISTS ENDPOINTS
    // ===============================

    /**
     * Check if userId exists
     * GET /api/user-contacts/exists/user/{userId}
     */
    @GetMapping("/exists/user/{userId}")
    public ResponseEntity<?> existsByUserId(@PathVariable String userId) {
        try {
            boolean exists = userContactService.existUserId(userId);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to check existence: " + e.getMessage()));
        }
    }

    /**
     * Check if email exists
     * GET /api/user-contacts/exists/email?email=test@test.com
     */
    @GetMapping("/exists/email")
    public ResponseEntity<?> existsByEmail(@RequestParam String email) {
        try {
            boolean exists = userContactService.existEmail(email);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to check existence: " + e.getMessage()));
        }
    }

    /**
     * Check if phone exists
     * GET /api/user-contacts/exists/phone?phone=1234567890
     */
    @GetMapping("/exists/phone")
    public ResponseEntity<?> existsByPhone(@RequestParam String phone) {
        try {
            boolean exists = userContactService.phoneExist(phone);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to check existence: " + e.getMessage()));
        }
    }

    /**
     * Check if userId and email exists
     * GET /api/user-contacts/exists/user-email?userId=123&email=test@test.com
     */
    @GetMapping("/exists/user-email")
    public ResponseEntity<?> existsByUserIdAndEmail(
            @RequestParam String userId,
            @RequestParam String email) {
        try {
            boolean exists = userContactService.existUserIdAndEmail(userId, email);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to check existence: " + e.getMessage()));
        }
    }

    /**
     * Check if userId and phone exists
     * GET /api/user-contacts/exists/user-phone?userId=123&phone=1234567890
     */
    @GetMapping("/exists/user-phone")
    public ResponseEntity<?> existsByUserIdAndPhone(
            @RequestParam String userId,
            @RequestParam String phone) {
        try {
            boolean exists = userContactService.existUserIdAndPhone(userId, phone);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to check existence: " + e.getMessage()));
        }
    }

    // ===============================
    // COUNT ENDPOINTS
    // ===============================

    /**
     * Get total contacts count
     * GET /api/user-contacts/count/all
     */
    @GetMapping("/count/all")
    public ResponseEntity<?> countAllContacts() {
        try {
            long count = userContactService.countAllContacts();
            return ResponseEntity.ok(Map.of("totalContacts", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to count contacts: " + e.getMessage()));
        }
    }

    /**
     * Get count by userId
     * GET /api/user-contacts/count/user/{userId}
     */
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<?> countByUserId(@PathVariable String userId) {
        try {
            long count = userContactService.countByUserId(userId);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to count: " + e.getMessage()));
        }
    }

    /**
     * Get count of users with email
     * GET /api/user-contacts/count/with-email
     */
    @GetMapping("/count/with-email")
    public ResponseEntity<?> countUsersWithEmail() {
        try {
            long count = userContactService.countUsersWithEmail();
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to count: " + e.getMessage()));
        }
    }

    /**
     * Get count of users with phone
     * GET /api/user-contacts/count/with-phone
     */
    @GetMapping("/count/with-phone")
    public ResponseEntity<?> countUsersWithPhone() {
        try {
            long count = userContactService.countUsersWithPhone();
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to count: " + e.getMessage()));
        }
    }

    /**
     * Get count of users without contact info
     * GET /api/user-contacts/count/without-contact
     */
    @GetMapping("/count/without-contact")
    public ResponseEntity<?> countUsersWithoutContactInfo() {
        try {
            long count = userContactService.countUsersWithoutContactInfo();
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to count: " + e.getMessage()));
        }
    }

    // ===============================
    // HELPER METHODS
    // ===============================

    private Map<String, Object> errorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    private Map<String, Object> successResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}