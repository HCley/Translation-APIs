package com.legalscale.translation.services;

// Imports the Google Cloud Translation library.
import com.google.cloud.translate.v3.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public class GoogleTranslateService {

    @Value("${GOOGLE_PROJECT_ID}")
    private String GOOGLE_PROJECT_ID;

    @Value("${GOOGLE_API_KEY}")
    private String GOOGLE_API_KEY;


    // Translate text to target language.
    public void translate(String filename) throws IOException {
        // Supported Languages: https://cloud.google.com/translate/docs/languages
        String sourceLanguage = "pt";
        String targetLanguage = "en";

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            // Supported Locations: `global`, [glossary location], or [model location]
            // Glossaries must be hosted in `us-central1`
            // Custom Models must use the same location as your model. (us-central1)
            LocationName parent = LocationName.of(GOOGLE_PROJECT_ID, "global");


            // Building file input configuration
            ByteString documentByteString = ByteString.copyFrom(convertFileToByteArray(filename));
            DocumentInputConfig originalDocument =
                    DocumentInputConfig.newBuilder()
                            .setMimeType("application/pdf")
                            .setContent(documentByteString)
                            .build();


            // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
            TranslateDocumentRequest request =
                    TranslateDocumentRequest.newBuilder()
                            .setParent(parent.toString())
                            .setSourceLanguageCode(sourceLanguage)
                            .setTargetLanguageCode(targetLanguage)
                            .setDocumentInputConfig(originalDocument)
                            .build();

//            TranslateTextRequest request =
//                    TranslateTextRequest.newBuilder()
//                            .setParent(parent.toString())
//                            .setMimeType("text/plain")
//                            .setTargetLanguageCode(targetLanguage)
//                            .addContents(text)
//                            .build();



            TranslateDocumentResponse response = client.translateDocument(request);

            // Display the translation for each input text provided
            DocumentTranslation document = response.getDocumentTranslation();
            ByteString file = ByteString.copyFrom(document.getByteStreamOutputsList());

            FileUtils.writeByteArrayToFile(new File(filename + "-Google.pdf"), file.toByteArray());
            System.out.println("there we are.");
        }
    }

    private byte[] convertFileToByteArray(String filename) throws IOException {
        return FileUtils.readFileToByteArray(new File("./" + filename));
    }
}