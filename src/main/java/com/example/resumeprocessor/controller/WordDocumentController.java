package com.example.resumeprocessor.controller;

import java.io.File;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/word")
public class WordDocumentController {
	
	@GetMapping("/status")
	public String getStatus() {
		return "Working......";
	}
	
	@PostMapping(value =  "/upload")
	public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file) {
		
		if(!file.isEmpty()) {
			
			String fileName = file.getOriginalFilename();
			
			try {
				file.transferTo(new File("C:\\Users\\shegonde\\Documents\\resumes\\"+fileName));
			}
			catch (Exception e) {
			      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		    } 
			
		}
	    
		return ResponseEntity.ok("File uploaded successfully.");
		
	}

}
