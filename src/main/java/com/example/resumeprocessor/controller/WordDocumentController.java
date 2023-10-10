package com.example.resumeprocessor.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.resumeprocessor.constant.Constants;
import com.example.resumeprocessor.services.FilesStorageService;
import com.example.resumeprocessor.services.WordDocumentService;

@RestController
@RequestMapping("/word")
public class WordDocumentController {
	
	@Autowired
	private WordDocumentService service;
	
	@Autowired
	FilesStorageService storageService;
	
	@GetMapping("/status")
	public String getStatus() {
		return "Working......";
	}
	
	@PostMapping(value =  "/upload")
	public ResponseEntity<String> uploadResume(@RequestParam("files") MultipartFile[] files,@RequestParam("name") String uploaderName) {
		
		String message = "";
	    try {
	      List<String> fileNames = new ArrayList<>();

	      storageService.init(uploaderName);
	      
	      System.out.println("Path ======== "+storageService.getFolderPath());
	      Arrays.asList(files).stream().forEach(file -> {
	        storageService.save(file);
	        fileNames.add(file.getOriginalFilename());
	      });

	      message = "Uploaded the files successfully: " + fileNames;
	      return ResponseEntity.status(HttpStatus.OK).body(message);
	    } catch (Exception e) {
	      message = "Fail to upload files!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
	    }
		
		
			
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		if(!file.isEmpty()) {
//			
//			String fileName = file.getOriginalFilename();
//			String path = "C:\\Users\\shegonde\\Documents\\resumes\\";
//			
//			try {
//				file.transferTo(new File(path+fileName));
//				String response = service.processWordDocument(path+fileName);
//				System.out.println(response);
//				
//			}
//			catch (Exception e) {
//			      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		    } 
//			
//		}
//	    
//		return ResponseEntity.ok("File uploaded successfully.");
		
	}
	
	
	@GetMapping(value = "/process")
	public String processAllUploadedDocuments() {
		
		
		
		return "Hello";
	}

}
