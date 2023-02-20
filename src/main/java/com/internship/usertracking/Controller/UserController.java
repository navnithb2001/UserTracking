package com.internship.usertracking.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.internship.usertracking.Entity.Activity;
import com.internship.usertracking.Entity.User;
import com.internship.usertracking.ResonseRequestBodies.JwtResponse;
import com.internship.usertracking.ResonseRequestBodies.LoginRequest;
import com.internship.usertracking.Service.UserService;

@RestController
@RequestMapping("")
public class UserController {
    @Autowired
    private UserService userService;
    
    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error registering user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error authenticating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("activities")
    public ResponseEntity<?> getAllUserActivities(@RequestParam String username) {
        try {
            List<Activity> activities = userService.getAllUserActivities(username);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return new ResponseEntity<>("Error getting users: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
