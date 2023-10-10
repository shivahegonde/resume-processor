package com.example.resumeprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.util.StopWatch;

public class ResumeProcess {
	public static void main(String[] args) {
		
		StopWatch stopWatch = new StopWatch();
		
		
		String sourceFolder = "C:\\Users\\shegonde\\Documents\\AllResumes";
		String shortlistedFolder = "C:\\Users\\shegonde\\Documents\\ShortlistedResumes\\"
				+ Calendar.getInstance().getTimeInMillis();
		String rejectedFolder = "C:\\Users\\shegonde\\Documents\\RejectedResumes\\"
				+ Calendar.getInstance().getTimeInMillis();
		String reValidateFolder = "C:\\Users\\shegonde\\Documents\\Revalidate\\"
				+ Calendar.getInstance().getTimeInMillis();

		double threshHoldPercent = 50.0;

		List<String> mandatoryKeywords = new ArrayList<>();
		mandatoryKeywords.add("java");
		mandatoryKeywords.add("Spring");

		List<String> preferredKeywords = new ArrayList<>();
		preferredKeywords.add("JPA");
		preferredKeywords.add("Boot");
		preferredKeywords.add("AWS");

		Map<String, List<String>> optionalKeywords = new HashMap<>();
		optionalKeywords.put("cloud",
				(List<String>) Arrays.asList("PCF", "AWS", "Azure", "Pivotal", "cloud", "Amazon", "Google"));
		optionalKeywords.put("queues", (List<String>) Arrays.asList("Kafka", "RabbitMQ", "Rabbit", "MQ", "JMS"));
		// optionalKeywords.put("frontend", (List<String>) Arrays.asList("Angular",
		// "React"));
		optionalKeywords.put("API", (List<String>) Arrays.asList("REST", "GraphQl", "gRPC"));
		optionalKeywords.put("spring", (List<String>) Arrays.asList("Spring", "Boot", "Springboot", "Spring-boot"));
		optionalKeywords.put("microservice",
				(List<String>) Arrays.asList("Microservice", "Micro-services", "Microservices", "Micro"));
		// optionalKeywords.put("CICD", (List<String>) Arrays.asList("CICD", "CI-CD",
		// "CI/CD"));
		optionalKeywords.put("Data", (List<String>) Arrays.asList("Hibernate", "JPA"));
		// optionalKeywords.put("SQL", (List<String>) Arrays.asList("Oracle", "MySQL",
		// "SQL", "PostgreSQL"));
		// optionalKeywords.put("NoSQL", (List<String>) Arrays.asList("Mongo",
		// "Cassandra"));
		optionalKeywords.put("Cache", (List<String>) Arrays.asList("Cache", "Redis"));
		optionalKeywords.put("DB",
				(List<String>) Arrays.asList("Oracle", "MySQL", "SQL", "PostgreSQL", "Mongo", "Cassandra", "Redis"));
		optionalKeywords.put("Microservice Components", (List<String>) Arrays.asList("gateway", "registry", "discovery",
				"load", "balancing", "circuit", "breaker", "hystrix", "ribbon", "zuul"));
		optionalKeywords.put("Coding", (List<String>) Arrays.asList("logical", "data-structure", "structure",
				"algorithm", "algorithms", "optimization"));
		
		stopWatch.start();
		System.out.println("----------------------------------------Started Execution------------------------------");

		File sourceDir = new File(sourceFolder);
		File[] files = sourceDir.listFiles();

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					try {
						String fileName = file.getName();
						String filePath = file.getAbsolutePath();

						String fileContent = "";

						if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
							fileContent = readWordFile2(filePath);
						} else if (fileName.endsWith(".pdf")) {
							fileContent = readPdfFile(filePath);
						}

						StringBuilder matchingString = new StringBuilder();
						double percentageMatch = calculatePercentageMatch(fileContent, mandatoryKeywords,
								preferredKeywords, optionalKeywords, matchingString);

						if (percentageMatch == 0.0) {
							File destinationFile = new File(reValidateFolder, fileName);
							FileUtils.copyFile(file, destinationFile, StandardCopyOption.REPLACE_EXISTING);
							System.out.println("File '" + fileName + "' needs to revalidated. " + "Percentage match  : "
									+ percentageMatch + " Matching keywords : " + matchingString);
						} else if (percentageMatch > threshHoldPercent) { // Adjust the threshold as needed
							File destinationFile = new File(shortlistedFolder, fileName);
							FileUtils.copyFile(file, destinationFile, StandardCopyOption.REPLACE_EXISTING);
							System.out.println("File '" + fileName + "' is shortlisted. " + "Percentage match  : "
									+ percentageMatch + " Matching keywords : " + matchingString);
						} else {
							File destinationFile = new File(rejectedFolder, fileName);
							FileUtils.copyFile(file, destinationFile);
							System.out.println("File '" + fileName + "' is rejected." + "Percentage match  : "
									+ percentageMatch + " Matching keywords : " + matchingString);
						}
					} catch (IOException e) {
						System.out.println("*****Error in handling file name  : " + file.getName());
						e.printStackTrace();
					}
				}
			}
		}
		
		stopWatch.stop();
		System.out.println("Time Taken to execute == "+stopWatch.getLastTaskTimeNanos()+ " in miliseconds == "+stopWatch.getLastTaskTimeMillis());
	}

	private static String readWordFile(String filePath) throws IOException {

		String text = "";

		if (filePath.endsWith(".doc")) {
			FileInputStream fis = new FileInputStream(filePath);
			HWPFDocument document = new HWPFDocument(fis);
			WordExtractor extractor = new WordExtractor(document);
			text = extractor.getText();
			extractor.close();
			fis.close();
		} else if (filePath.endsWith(".docx")) {
			FileInputStream fis = new FileInputStream(filePath);
			try (XWPFDocument document = new XWPFDocument(fis)) {
				List<XWPFParagraph> paragraphs = document.getParagraphs();
				for (XWPFParagraph paragraph : paragraphs) {
					List<XWPFRun> runs = paragraph.getRuns();
					for (XWPFRun run : runs) {
						text += run.getText(0);
					}
				}
			}
			fis.close();
		}

		return text;
	}

	private static String readWordFile2(String filePath) throws IOException {

		String fileData = "";

		if (filePath.endsWith(".doc")) {
			FileInputStream fis = new FileInputStream(filePath);
			HWPFDocument document = new HWPFDocument(fis);
			WordExtractor extractor = new WordExtractor(document);
			fileData = extractor.getText();
			extractor.close();
			fis.close();
		} else if (filePath.endsWith(".docx")) {
			FileInputStream fis = new FileInputStream(filePath);
			XWPFDocument document = new XWPFDocument(fis);

			XWPFWordExtractor extractor = new XWPFWordExtractor(document);

			fileData = extractor.getText();
		
			extractor.close();
			fis.close();
		}

		return fileData;
	}

	private static String readPdfFile(String filePath) throws IOException {
		PDDocument document = Loader.loadPDF(new File(filePath));
		PDFTextStripper pdfStripper = new PDFTextStripper();
		String text = pdfStripper.getText(document);
		document.close();
		return text;
	}

	private static double calculatePercentageMatch(String text, List<String> mandatoryKeywords,
			List<String> preferredKeywords, Map<String, List<String>> optionalKeywordsMap,
			StringBuilder matchingString) {
		int totalKeywords = 0;
		int matches = 0;
		boolean qualifiedMandatory = true;
		boolean isOptionPresent = false;

		Set<String> wordSet = new HashSet<>(Arrays.asList(text.toLowerCase().split("[\\s\\p{Punct}]+")));

		System.out.println(">>>>>>>>> " + wordSet);

		for (String keyword : mandatoryKeywords) {
			if (!wordSet.contains(keyword.toLowerCase())) {
				qualifiedMandatory = false;
				break;
			} else {
				matchingString.append(keyword);
			}
		}

		if (qualifiedMandatory) {
			if (preferredKeywords != null && !preferredKeywords.isEmpty()) {
				totalKeywords = preferredKeywords.size();
				for (String keyword : preferredKeywords) {
					if (wordSet.contains(keyword.toLowerCase())) {
						matches++;
						matchingString.append(" ").append(keyword);
					}
				}
			} else {
				// since only mandatory match is possible so 100% match considered till now
				totalKeywords = mandatoryKeywords.size();
				matches = mandatoryKeywords.size();
			}

			// optionalKeywords check
//			if (optionalKeywordsMap != null && !optionalKeywordsMap.isEmpty()) {
//				totalKeywords += optionalKeywordsMap.size();
//				for (String optionKey : optionalKeywordsMap.keySet()) {
//					isOptionPresent = false;
//					for (String optionalKeyWord : optionalKeywordsMap.get(optionKey)) {
//						if (wordSet.contains(optionalKeyWord.toLowerCase())) {
//							isOptionPresent = true;
//							matchingString.append(" ").append(optionalKeyWord);
//						}
//					}
//					if (isOptionPresent) {
//						matches++;
//					}
//				}
//			}
//		} else {
//			return 0;
			System.out.println("matches = " + matches + " total Keywords = " + totalKeywords);
		}
		return (double) matches / totalKeywords * 100.0;
	}
}
