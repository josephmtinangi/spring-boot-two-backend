package com.example.backend.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class Helper {

	public static ResponseEntity<?> createResponse(boolean success, Object data, String message, HttpStatus code) {

		HashMap<String, Object> response = new HashMap<>();
		response.put("success", success);
		response.put("data", data);
		response.put("message", message);
		response.put("code", code);

		return new ResponseEntity<>(response, code);
	}

	public static String saveFile(MultipartFile file, String destination, String rootStorage) throws Exception {

		String fileName = "";
		String fileHashName = "";

		new File(rootStorage + "/" + destination).mkdirs();

		try {
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();

			fileName = file.getOriginalFilename();

			// System.out.println(fileName);

			String fileNameTokens[] = fileName.split("\\.");

			fileHashName = destination + "/" + UUID.randomUUID().toString();

			if (fileNameTokens.length > 1) {
				fileHashName += "." + fileNameTokens[fileNameTokens.length - 1];
			}

			Path path = Paths.get(rootStorage + "/" + fileHashName);

			Files.write(path, bytes);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileHashName;

	}

	public static String getFileExtension(String fileName) {

		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}

		return extension;
	}

}
