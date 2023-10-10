package com.example.resumeprocessor.services;

import org.springframework.stereotype.Service;

@Service
public interface WordDocumentService {
	
	public String processWordDocument(String filePath);
	
	

}
