package com.example.resumeprocessor.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import com.example.resumeprocessor.services.BulkDocumentsProcessService;

@Service
public class WordDocumentServiceImpl implements BulkDocumentsProcessService{

	@Override
	public String processMultipleDocuments(String filePath) {
		try {
			
            FileInputStream fis = new FileInputStream(new File(filePath));
            XWPFDocument document = new XWPFDocument(fis);

            StringBuilder content = new StringBuilder();
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                content.append(paragraph.getText()).append("\n");
            }

            document.close();
            fis.close();

            return "Word document content printed to console.";
        } catch (IOException e) {
            return "Failed to read the Word document.";
        }
	}

}
