
package com.example.demo.Services.SupabaseServices;

import com.example.demo.Configs.SupabaseProperties;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SupabaseStorageService {

    private final SupabaseProperties supabaseProperties;
    private final OkHttpClient client = new OkHttpClient();

    public SupabaseStorageService(
            SupabaseProperties supabaseProperties) {
        this.supabaseProperties = supabaseProperties;
    }

    private String getBaseUrl() {
        String url = supabaseProperties.getUrl();

        if (url == null || url.isBlank()) {
            throw new IllegalStateException(
                    "Supabase Project URL is missing.");
        }

        url = url.trim().replaceAll("/+$", "");

        if (!url.startsWith("https://")
                && !url.startsWith("http://")) {
            throw new IllegalStateException(
                    "Supabase URL must start with http:// or https://");
        }

        return url;
    }

    private String getKey() {
        String key = supabaseProperties.getKey();

        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "Supabase API key is missing.");
        }

        return key.trim();
    }

    public String uploadFile(
            String bucketName,
            String fileName,
            byte[] fileBytes,
            String contentType) {

        try {
            if (bucketName == null || bucketName.isBlank()
                    || fileName == null || fileName.isBlank()) {
                throw new IllegalArgumentException(
                        "Bucket name and file name are required.");
            }

            if (fileBytes == null || fileBytes.length == 0) {
                throw new IllegalArgumentException(
                        "File is empty.");
            }

            String baseUrl = getBaseUrl();
            String key = getKey();

            String url = baseUrl
                    + "/storage/v1/object/"
                    + bucketName + "/" + fileName;

            MediaType mediaType = MediaType.parse(
                    contentType != null
                            ? contentType
                            : "application/octet-stream");

            RequestBody requestBody =
                    RequestBody.create(fileBytes, mediaType);

            Request request = new Request.Builder()
                    .url(url)
                    .header("apikey", key)
                    .header("Authorization", "Bearer " + key)
                    .header("Content-Type",
                            mediaType.toString())
                    .post(requestBody)
                    .build();

            try (Response response =
                         client.newCall(request).execute()) {

                String responseBody = response.body() != null
                        ? response.body().string()
                        : "";

                if (!response.isSuccessful()) {
                    throw new IOException(
                            "Supabase upload failed. HTTP "
                            + response.code() + ": "
                            + responseBody);
                }
            }

            // This URL is publicly accessible only if the
            // bucket is configured as public.
            return baseUrl
                    + "/storage/v1/object/public/"
                    + bucketName + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to upload file to Supabase: "
                    + e.getMessage(), e);
        }
    }

    public void deleteFile(
            String bucketName,
            String fileName) {

        try {
            String url = getBaseUrl()
                    + "/storage/v1/object/"
                    + bucketName + "/" + fileName;

            Request request = new Request.Builder()
                    .url(url)
                    .header("apikey", getKey())
                    .header("Authorization",
                            "Bearer " + getKey())
                    .delete()
                    .build();

            try (Response response =
                         client.newCall(request).execute()) {

                String responseBody = response.body() != null
                        ? response.body().string()
                        : "";

                if (!response.isSuccessful()) {
                    throw new IOException(
                            "Supabase delete failed. HTTP "
                            + response.code() + ": "
                            + responseBody);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to delete file from Supabase: "
                    + e.getMessage(), e);
        }
    }
}
