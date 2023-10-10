package com.example.resumeprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class ResumeSkillsCheck {

	public static void main(String[] args) {

		File file = null;

		XWPFWordExtractor extractor = null;

		Map<String, Integer> skillCountMap = new HashMap<String, Integer>();

		List<String> skills = Arrays.asList("Java", "Spring", "RESTful", "SQL");

		try

		{

			file = new File(
					"C:\\Users\\shegonde\\Documents\\resume-processor\\parser-resume-master\\parser-resume-master\\PDF\\Kushal Chandak Resume.docx");

			FileInputStream fis = new FileInputStream(file.getAbsolutePath());

			XWPFDocument document = new XWPFDocument(fis);

			extractor = new XWPFWordExtractor(document);

			String fileData = extractor.getText();

			skills.stream().forEach(e -> {
				skillCountMap.put(e, countOccurrences(fileData, e));
			});

		}

		catch (Exception ex)

		{

			ex.printStackTrace();

		}
		System.out.println(skillCountMap);

	}

	static int countOccurrences(String str, String word) {

		// Splitting the string into words
		List<String> wordslist = Arrays.asList(str.split("\\s+"));

		// Counting the frequency of the given word
		return Collections.frequency(wordslist, word);
	}

}