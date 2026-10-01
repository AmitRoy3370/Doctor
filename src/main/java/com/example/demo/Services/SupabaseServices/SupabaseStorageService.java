package com.example.demo.Services.SupabaseServices;


import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Configs.SupabaseProperties;

import java.io.IOException;

@Service
public class SupabaseStorageService {
    
    @Autowired
    private SupabaseProperties supabaseProperties;
    
    private final OkHttpClient client = new OkHttpClient();
    
    public String uploadFile(String bucketName, String fileName, byte[] fileBytes, String contentType) {
        try {
            String url = supabaseProperties.getUrl() + "/storage/v1/object/" + bucketName + "/" + fileName;
            
            RequestBody requestBody = RequestBody.create(fileBytes, MediaType.parse(contentType));
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + supabaseProperties.getKey())
                    .header("Content-Type", contentType)
                    .post(requestBody)
                    .build();
            
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                
                
                return supabaseProperties.getUrl() + "/storage/v1/object/public/" + bucketName + "/" + fileName;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Supabase: " + e.getMessage());
        }
    }
    
    public void deleteFile(String bucketName, String fileName) {
        try {
            String url = supabaseProperties.getUrl() + "/storage/v1/object/" + bucketName + "/" + fileName;
            
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + supabaseProperties.getKey())
                    .delete()
                    .build();
            
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from Supabase: " + e.getMessage());
        }
    }
}
