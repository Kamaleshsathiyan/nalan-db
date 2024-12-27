package com.nalanbatters.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nalanbatters.exception.UserNotFoundException;
import com.nalanbatters.model.NalanBattersModel;
import com.nalanbatters.model.User;
import com.nalanbatters.service.NalanBattersService;

import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class NalanBattersController {

    @Autowired
    private NalanBattersService nalanBattersService;

    private final static String status="status";
    
	private final static String message="message";
	
	private final static String error="error";
    

    @PostMapping("/order")
    public ResponseEntity<Map<String, String>> orderInfo(@RequestBody NalanBattersModel nalanBattersModel) {
        Map<String, String> response = new HashMap<>();
        try {
            nalanBattersService.requestData(nalanBattersModel);
            response.put(message, "Order Confirmation Mail Sent Successfully.");
            response.put(status, "success");
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            response.put(error, "Failed to send confirmation email: " + e.getMessage());
            response.put(status, "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (DataAccessException e) {
            response.put(error, "Database operation failed: " + e.getMessage());
            response.put(status, "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.put(error, "Unexpected error occurred: " + e.getMessage());
            response.put(status, error);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/show")
    public List<NalanBattersModel> getMethodName() {
    	
    	
        return nalanBattersService.getAllData();
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            // Validate user credentials
            nalanBattersService.validateUser(user.getEmail(), user.getPassword());
            
            response.put(message, "Login successful.");
            response.put(status, "success");
            
            return ResponseEntity.ok(response);
        }catch (UserNotFoundException ex) {
            response.put(message, ex.getMessage());
            response.put(status, error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException ex) {
            response.put(message, ex.getMessage());
            response.put(status, error);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }   catch (Exception ex) {
            response.put(message, "An unexpected error occurred.");
            response.put(status, error);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
