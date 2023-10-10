package com.example.resumeprocessor;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.resumeprocessor.services.FilesStorageService;

@SpringBootApplication
public class ResumeProcessorApplication implements CommandLineRunner{

	 @Resource
	 FilesStorageService storageService;
	 
	public static void main(String[] args) {
		SpringApplication.run(ResumeProcessorApplication.class, args);
	}
	
	@Override
	public void run(String... arg) throws Exception {
	    storageService.deleteAll();

	  }

}
