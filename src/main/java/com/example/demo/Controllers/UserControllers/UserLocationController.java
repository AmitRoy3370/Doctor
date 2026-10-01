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

import com.example.demo.Models.UserModels.UserLocation;
import com.example.demo.Services.UserServices.UserLocationService;

@RestController
@RequestMapping("/api/user-location")
public class UserLocationController {

    @Autowired
    private UserLocationService userLocationService;

    // ---------------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------------

    /**
     * POST /api/user-location/add/{userId}
     * Body: { "userId": "...", "locationName": "...", "lattitude": 0.0, "longititude": 0.0 }
     */
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addUserLocation(@RequestBody UserLocation userLocation,
                                             @PathVariable("userId") String userId) {
        try {
            UserLocation saved = userLocationService.addUserLocation(userLocation, userId);
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
     * PUT /api/user-location/update/{id}/{userId}
     * Body: { "userId": "...", "locationName": "...", "lattitude": 0.0, "longititude": 0.0 }
     */
    @PutMapping("/update/{id}/{userId}")
    public ResponseEntity<?> updateUserLocation(@RequestBody UserLocation userLocation,
                                                @PathVariable("userId") String userId,
                                                @PathVariable("id") String id) {
        try {
            UserLocation updated = userLocationService.updateUserLocation(userLocation, userId, id);
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
     * GET /api/user-location/find/{id}
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        try {
            UserLocation location = userLocationService.findById(id);
            return new ResponseEntity<>(location, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-location/all
     */
    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        try {
            List<UserLocation> list = userLocationService.findAll();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-location/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable("userId") String userId) {
        try {
            UserLocation location = userLocationService.findByUserId(userId);
            return new ResponseEntity<>(location, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-location/search?locationName=Dhaka
     */
    @GetMapping("/search")
    public ResponseEntity<?> findByLocationNameContainingIgnoreCase(
            @RequestParam("locationName") String locationName) {
        try {
            List<UserLocation> list = userLocationService
                    .findByLocationNameContainingIgnoreCase(locationName);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/user-location/by-coordinates?lattitude=23.8&longititude=90.4
     */
    @GetMapping("/by-coordinates")
    public ResponseEntity<?> findByLattitudeOrLongititude(
            @RequestParam("lattitude") double lattitude,
            @RequestParam("longititude") double longititude) {
        try {
            List<UserLocation> list = userLocationService
                    .findByLattitudeOrLongititude(lattitude, longititude);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ---------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------

    /**
     * DELETE /api/user-location/delete/{id}/{userId}
     */
    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<?> deleteUserLocation(@PathVariable("id") String id,
                                                @PathVariable("userId") String userId) {
        try {
            boolean deleted = userLocationService.deleteUserLocation(id, userId);
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