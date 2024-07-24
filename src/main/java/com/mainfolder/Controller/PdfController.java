package com.mainfolder.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mainfolder.Service.PDFtoCSVProcessor;

@RestController
public class PdfController {
	
	@Autowired
	private PDFtoCSVProcessor processor;
	
	@GetMapping("/convertion")
	public ResponseEntity converionOfFiles() throws Exception
	{
		String inputPdf = "D:\\Bin\\INPUT&OUTPUT FILES\\PDF File.pdf";
		String outputCsv = "D:\\Bin\\INPUT&OUTPUT FILES\\FinalOutput.csv";
		
		processor.convertion(inputPdf, outputCsv);
		
		return ResponseEntity.status(HttpStatus.OK).body("ConversionSuccess");
	}


}
