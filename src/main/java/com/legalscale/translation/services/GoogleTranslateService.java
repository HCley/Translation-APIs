package com.legalscale.translation.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleTranslateService {

    @Value("${GOOGLE_API_KEY}")
    private String GOOGLE_API_KEY;

    public void translate(String filename) {

    }

}
