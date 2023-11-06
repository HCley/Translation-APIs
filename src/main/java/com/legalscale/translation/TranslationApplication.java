package com.legalscale.translation;

import com.legalscale.translation.services.EdenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TranslationApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TranslationApplication.class, args);
	}

	@Autowired
	EdenAIService translateService;

	@Override
	public void run(String... args) throws Exception {
		translateService.translate("Sample_2.pdf");
	}
}
