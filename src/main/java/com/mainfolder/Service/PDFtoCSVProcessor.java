package com.mainfolder.Service;

import com.aspose.pdf.Document;
import com.aspose.pdf.ExcelSaveOptions;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.apache.commons.csv.CSVParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@Component
public class PDFtoCSVProcessor {

	private static final String[] HEADERS = { "ﺭﺻﻴﺪ ﻣﺒﺎﻟﻎ ﻣﺠﻤﺪﻩ", "ﺭﺻﻴﺪ ﻣﺒﺎﻟﻎ ﺗﺤﺖ ﺍﻟﺘﺤﺼﻴﻞ", "ﻣﺒﺎﻟﻎ ﻣﺴﺘﺤﻘﺔ",
			"ﺭﺻﻴﺪ ﺍﻟﻤﺒﺎﻟﻎ" };


	public void convertion(String inputPdf,String outputCsv ) throws Exception {
		convertPdfToCsv(inputPdf, outputCsv);
		cleanCsv(outputCsv, "parsed_pdf.csv");
	}

	

	public static void convertPdfToCsv(String inputPdf, String outputCsv) throws IOException {
		Document document = new Document(inputPdf);
		document.getPages().delete(1);

		ExcelSaveOptions saveOptions = new ExcelSaveOptions();
		saveOptions.setFormat(ExcelSaveOptions.ExcelFormat.CSV);

		document.save(outputCsv, saveOptions);
		System.out.println("Intermediate CSV file created successfully.");
	}

	public static void cleanCsv(String inputCsv, String outputCsv) {
		try (Reader reader = new InputStreamReader(new FileInputStream(inputCsv), StandardCharsets.UTF_16BE);
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
				BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputCsv), StandardCharsets.UTF_16BE);
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(HEADERS))) {
			for (CSVRecord record : csvParser) {
				List<String> cleanedRow = cleanRow(record);
				if (!cleanedRow.isEmpty() && isDesiredRow(cleanedRow)) {
					csvPrinter.printRecord(cleanedRow);
				}
			}

			System.out.println("Cleaned CSV file created successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<String> cleanRow(CSVRecord record) {
		List<String> cleanedRow = new ArrayList<>();
		boolean foundFirstNonEmpty = false;

		for (String value : record) {
			if (!value.trim().isEmpty()) {
				foundFirstNonEmpty = true;
				cleanedRow.add(value.trim());
			} else if (foundFirstNonEmpty) {
				cleanedRow.add(value.trim());
			}
		}

		return cleanedRow;
	}

	private static boolean isDesiredRow(List<String> row) {
		if (row.size() == 4) {
			String firstColumn = row.get(0).replace(",", "");
			return firstColumn.matches("\\d+");
		}
		return false;
	}
}
