package com.example.resumeprocessor.controller;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class WordReaderController {

    @GetMapping("/read-word")
    public ResponseEntity<String> readWordDocument(@RequestParam("filePath") String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            XWPFDocument document = new XWPFDocument(fis);

            StringBuilder content = new StringBuilder();
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                content.append(paragraph.getText()).append("\n");
            }

            fis.close();

            System.out.println("Word Document Content:");
            System.out.println(content);

            return ResponseEntity.ok("Word document content printed to console.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read the Word document.");
        }
    }
}

