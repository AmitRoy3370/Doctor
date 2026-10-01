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

import com.example.demo.Models.UserModels.UserGender;
import com.example.demo.Services.UserServices.UserGenderService;

@RestController
@RequestMapping("/api/user-gender")
public class UserGenderController {

    @Autowired
    private UserGenderService userGenderService;

    // ---------------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------------

    /**
     * POST /api/user-gender/add/{userId}
     * Body: { "userId": "...", "gender": "MALE" }
     */
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addUserGender(@RequestBody UserGender gender,
                                           @PathVariable("userId") String userId) {
        try {
            UserGender saved = userGenderService.addUserGender(gender, userId);
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
     * PUT /api/user-gender/update/{id}/{userId}
     * Body: { "userId": "...", "gender": "FEMALE" }
     */
    @PutMapping("/update/{id}/{userId}")
    public ResponseEntity<?> updateUserGender(@RequestBody UserGender gender,
                                              @PathVariable("userId") String userId,
                                              @PathVariable("id") String id) {
        try {
            UserGender updated = userGenderService.updateUserGender(gender, userId, id);
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
     * GET /api/user-gender/find/{id}
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        try {
            UserGender gender = userGenderService.findById(id);
            return new ResponseEntity<>(gender, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-gender/all
     */
    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        try {
            List<UserGender> list = userGenderService.findAll();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-gender/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findUserGenderByUserIdNative(@PathVariable("userId") String userId) {
        try {
            UserGender gender = userGenderService.findUserGenderByUserIdNative(userId);
            return new ResponseEntity<>(gender, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-gender/by-gender?gender=MALE
     */
    @GetMapping("/by-gender")
    public ResponseEntity<?> findByGender(@RequestParam("gender") String gender) {
        try {
            List<UserGender> list = userGenderService.findByGender(gender);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-gender/count-grouped
     */
    @GetMapping("/count-grouped")
    public ResponseEntity<?> countGroupedByGender() {
        try {
            List<Object[]> result = userGenderService.countGroupedByGender();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/user-gender/search?gender=MALE&pattern=USR%
     */
    @GetMapping("/search")
    public ResponseEntity<?> findByGenderAndUserIdPattern(
            @RequestParam("gender") String gender,
            @RequestParam("pattern") String pattern) {
        try {
            List<UserGender> list = userGenderService.findByGenderAndUserIdPattern(gender, pattern);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-gender/exists/{userId}
     */
    @GetMapping("/exists/{userId}")
    public ResponseEntity<?> existsByUserIdNative(@PathVariable("userId") String userId) {
        try {
            boolean exists = userGenderService.existsByUserIdNative(userId);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ---------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------

    /**
     * DELETE /api/user-gender/delete-by-user/{userId}
     */
    @DeleteMapping("/delete-by-user/{userId}")
    public ResponseEntity<?> deleteByUserIdNative(@PathVariable("userId") String userId) {
        try {
            boolean deleted = userGenderService.deleteByUserIdNative(userId);
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
     * DELETE /api/user-gender/delete/{id}/{userId}
     */
    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<?> deleteUserGender(@PathVariable("id") String id,
                                              @PathVariable("userId") String userId) {
        try {
            boolean deleted = userGenderService.deleteUserGender(id, userId);
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
    // GLOBAL EXCEPTION HANDLER (optional, catches anything unhandled)
    // ---------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        return new ResponseEntity<>("Unexpected error: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}