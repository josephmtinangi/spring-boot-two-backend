package com.example.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.utilities.Helper;

@RestController
@RequestMapping(path = "/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Value("${ROOT_STORAGE}")
	private String rootStorage;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<?> index() {

		List<User> users = userRepository.findAll();

		return Helper.createResponse(true, users, "success", HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> show(@PathVariable Long id) {

		Optional<User> user = userRepository.findById(id);

		if (user == null) {
			return Helper.createResponse(false, null, "Not found", HttpStatus.BAD_REQUEST);
		}

		return Helper.createResponse(true, user, "success", HttpStatus.OK);
	}

	@RequestMapping(path = "/username/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> showByUsername(@PathVariable String username) {

		User user = userRepository.findFirstByUsername(username);

		if (user == null) {
			return Helper.createResponse(false, null, "Not found", HttpStatus.BAD_REQUEST);
		}

		return Helper.createResponse(true, user, "success", HttpStatus.OK);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<?> store(@ModelAttribute User user, @RequestParam(required = false) MultipartFile file) {


		if (file != null && !file.isEmpty()) {
			String destination = "users/photos";
			String photoPath;
			try {
				photoPath = Helper.saveFile(file, destination, rootStorage);
				user.setPhoto(photoPath);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		user.setPassword(bcrypt.encode(user.getPassword()));

		User newUser = userRepository.save(user);

		return Helper.createResponse(true, newUser, "success", HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<?> update(@PathVariable Long id) {

		Optional<User> user = userRepository.findById(id);

		if (user == null) {
			return Helper.createResponse(false, null, "Not found", HttpStatus.BAD_REQUEST);
		}

		return Helper.createResponse(true, user, "success", HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> destroy(@PathVariable Long id) {

		Optional<User> user = userRepository.findById(id);

		if (user == null) {
			return Helper.createResponse(false, null, "Not found", HttpStatus.BAD_REQUEST);
		}

		return Helper.createResponse(true, null, "success", HttpStatus.OK);
	}
}
