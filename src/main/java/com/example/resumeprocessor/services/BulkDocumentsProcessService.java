package com.example.resumeprocessor.services;

import org.springframework.stereotype.Service;

@Service
public interface BulkDocumentsProcessService {
	
	public String processMultipleDocuments(String filePath);	

}
