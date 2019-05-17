package com.example.backend.utilities;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Helper {

	public static ResponseEntity<?> createResponse(boolean success, Object data, String message, HttpStatus code) {

		HashMap<String, Object> response = new HashMap<>();
		response.put("success", success);
		response.put("data", data);
		response.put("message", message);
		response.put("code", code);

		return new ResponseEntity<>(response, code);
	}

}
