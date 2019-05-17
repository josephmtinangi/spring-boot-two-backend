package com.example.backend.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/attachments")
public class AttachmentController {

	@Value("${ROOT_STORAGE}")
	private String rootStorage;

	@RequestMapping(path = "/{location}/{section}/{filename:.+}", method = RequestMethod.GET)
	public ResponseEntity<?> download(@PathVariable String location, @PathVariable String section,
			@PathVariable String filename, HttpServletRequest request) throws IOException {

		// String filenameForHumans = "";

		String filePath = rootStorage + "/" + location + "/" + section + "/" + filename;

		File file = new File(filePath);

		Path path = Paths.get(file.getAbsolutePath());

		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

		String mimeType = URLConnection.guessContentTypeFromName(file.getName());

		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		HttpHeaders httpHeaders = new HttpHeaders();
		// httpHeaders.set("Content-Disposition","attachment; filename=" +
		// filenameForHumans + "." + Helper.getFileExtension(filename));

		return ResponseEntity.ok().contentLength(file.length()).contentType(MediaType.parseMediaType(mimeType))
				.headers(httpHeaders).body(resource);
	}

}
