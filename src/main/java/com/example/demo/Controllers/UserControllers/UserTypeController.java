package com.example.demo.Controllers.UserControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.UserModels.UserTypes;
import com.example.demo.Services.UserServices.UserTypeService;

@RestController
@RequestMapping("/api/user-type")
public class UserTypeController {

    @Autowired
    private UserTypeService userTypeService;

    // ---------------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------------

    /**
     * POST /api/user-type/add/{userId}
     * Body: { "userId": "...", "type": "USER" }
     */
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addUserTypes(@RequestBody UserTypes types,
                                          @PathVariable("userId") String userId) {
        try {
            UserTypes saved = userTypeService.addUserTypes(types, userId);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (ArithmeticException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------------

    /**
     * PUT /api/user-type/update/{id}/{userId}
     * Body: { "userId": "...", "type": "ADMIN" }
     */
    @PutMapping("/update/{id}/{userId}")
    public ResponseEntity<?> updateUserTypes(@RequestBody UserTypes types,
                                             @PathVariable("userId") String userId,
                                             @PathVariable("id") String id) {
        try {
            UserTypes updated = userTypeService.updateUserTypes(types, userId, id);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (ArithmeticException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------------------------------------------------------
    // READ
    // ---------------------------------------------------------------

    /**
     * GET /api/user-type/find/{id}
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        try {
            UserTypes types = userTypeService.findById(id);
            return new ResponseEntity<>(types, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-type/all
     */
    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        try {
            List<UserTypes> list = userTypeService.findAll();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-type/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable("userId") String userId) {
        try {
            UserTypes types = userTypeService.findByUserId(userId);
            return new ResponseEntity<>(types, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-type/search?type=ADMIN
     */
    @GetMapping("/search")
    public ResponseEntity<?> findByTypeContainingIgnoreCase(
            @RequestParam("type") String type) {
        try {
            List<UserTypes> list = userTypeService.findByTypeContainingIgnoreCase(type);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-type/exists/{userId}
     */
    @GetMapping("/exists/{userId}")
    public ResponseEntity<?> existsByUserIdNative(@PathVariable("userId") String userId) {
        try {
            boolean exists = userTypeService.existsByUserIdNative(userId);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------

    /**
     * DELETE /api/user-type/delete-by-user/{userId}
     */
    @DeleteMapping("/delete-by-user/{userId}")
    public ResponseEntity<?> deleteByUserIdNative(@PathVariable("userId") String userId) {
        try {
            boolean deleted = userTypeService.deleteByUserIdNative(userId);
            if (deleted) {
                return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("Nothing to delete", HttpStatus.NOT_FOUND);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * DELETE /api/user-type/delete/{id}/{userId}
     */
    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<?> deleteUserType(@PathVariable("id") String id,
                                            @PathVariable("userId") String userId) {
        try {
            boolean deleted = userTypeService.deleteUserType(id, userId);
            if (deleted) {
                return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("Nothing to delete", HttpStatus.NOT_FOUND);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ---------------------------------------------------------------
    // GLOBAL EXCEPTION HANDLER
    // ---------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        return new ResponseEntity<>("Unexpected error: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}