package com.legalscale.translation.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import okhttp3.*;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.io.File;

@Service
public class EdenAIService {

    @Value("${EDEN_API_KEY}")
    String edenApiKey;

    @Value("${EDEN_PROVIDER}")
    String PROVIDER;


    public void translate(String filename) {
        OkHttpClient client = new OkHttpClient();

        String token = edenApiKey;
        String url = "https://api.edenai.run/v2/translation/document_translation";
        String sourceLanguage = "pt";
        String targetLanguage = "en";
        String filePath = "./" + filename;

        MediaType mediaType = MediaType.parse("application/pdf");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("show_original_response", "false")
                .addFormDataPart("fallback_providers", "")
                .addFormDataPart("providers", PROVIDER)
                .addFormDataPart("source_language", sourceLanguage)
                .addFormDataPart("target_language", targetLanguage)
                .addFormDataPart("file", filename,
                        RequestBody.create(new File(filePath), mediaType))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonBody = new JSONObject(Objects.requireNonNull(response.body()).string());

                    String fileURL = jsonBody.getJSONObject(PROVIDER).getString("document_resource_url");
                    double cost = jsonBody.getJSONObject(PROVIDER).getDouble("cost");

                    System.out.println("Cost: " + cost);
                    System.out.println("Doc-url: " + fileURL);

                    dumpFile("Result.pdf", fileURL);

                } else {
                    System.out.println("Request failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Request failed: " + e.getMessage());
            }
        });
    }

    private void dumpFile(String filename, String fileUrl) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // handle exception
        }
        System.out.println("Dumped");
    }
}