package com.legalscale.translation;

import com.legalscale.translation.services.EdenAIService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.legalscale.translation.services.GoogleTranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class TranslationApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(TranslationApplication.class, args);
	}

	@Autowired
	GoogleTranslateService googleTranslateService;

	@Autowired
	EdenAIService edenTranslateService;


	@Override
	public void run(String... args) throws Exception {
//		edenTranslateService.translate("Sample_2.pdf");
		googleTranslateService.translate("Sample_2.pdf");
	}
}
