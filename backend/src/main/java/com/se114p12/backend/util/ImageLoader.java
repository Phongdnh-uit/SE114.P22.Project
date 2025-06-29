package com.se114p12.backend.util;

import com.se114p12.backend.services.general.StorageService;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ImageLoader {

  private final StorageService storageService;

  // Util method to save a image file from a URL
  public String saveImageFromUrl(String imageUrl, String folderName) {
    try {
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(imageUrl)).GET().build();
      try {
        HttpResponse<InputStream> response =
            httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() != 200) {
          throw new RuntimeException("Failed to fetch image from URL: " + imageUrl);
        }
        try (InputStream inputStream = response.body()) {
          return storageService.storeByInputStream(inputStream, folderName);
        } catch (IOException e) {
          throw new RuntimeException("Failed to save image from URL: " + imageUrl, e);
        }
      } catch (MalformedURLException e) {
        throw new RuntimeException("Invalid image URL: " + imageUrl, e);
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Error fetching image from URL: " + imageUrl, e);
    }
  }
}
