package com.se114p12.backend.controllers.storage;

import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.services.general.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Storage Module", description = "APIs for retrieving stored image resources")
@RestController
@RequestMapping(AppConstant.API_BASE_PATH + "/storage")
@RequiredArgsConstructor
public class StorageController {

  private final StorageService storageService;

  @Operation(
          summary = "Get image resource by file URL",
          description = "Returns an image or file stored in the server as a stream using the given relative URL path."
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Image found and returned as stream",
                  content = @Content(mediaType = "application/octet-stream", schema = @Schema(type = "string", format = "binary"))),
          @ApiResponse(responseCode = "404", description = "Image not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @ErrorResponse
  @GetMapping(value = "/images", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<Resource> getImageStoragePath(
          @Parameter(description = "Relative path to the image resource", required = true, example = "product-images/abc.jpg")
          @RequestParam("url") String url
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(storageService.loadAsResource(url));
  }
}