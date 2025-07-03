package com.se114p12.backend.services.delivery;

import com.se114p12.backend.configs.MapboxConfig;
import com.se114p12.backend.dtos.delivery.DeliveryResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class MapServiceImpl implements MapService {

    private final RestTemplate restTemplate;
    private final MapboxConfig mapboxConfig;

    public MapServiceImpl(MapboxConfig mapboxConfig) {
        this.mapboxConfig = mapboxConfig;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public DeliveryResponseDTO calculateExpectedDeliveryTime(double originLat, double originLng, double destLat, double destLng) {
        String url = String.format(
                "%s/directions/v5/mapbox/driving/%f,%f;%f,%f?access_token=%s&overview=false",
                mapboxConfig.getBaseUrl(), originLng, originLat, destLng, destLat, mapboxConfig.getAccessToken()
        );

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            List<?> routes = (List<?>) body.get("routes");

            if (routes != null && !routes.isEmpty()) {
                Map<?, ?> route = (Map<?, ?>) routes.get(0);
                double durationInSeconds = (Double) route.get("duration");

                // Tính thời điểm giao dự kiến với múi giờ Việt Nam
                ZonedDateTime nowInVN = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
                ZonedDateTime expectedTime = nowInVN.plusSeconds((long) durationInSeconds);
                String formattedExpectedTime = expectedTime.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));

                return new DeliveryResponseDTO(
                        (int) durationInSeconds,
                        formatDuration(durationInSeconds),
                        formattedExpectedTime
                );
            }
        }

        throw new RuntimeException("Unable to calculate delivery time.");
    }

    private String formatDuration(double seconds) {
        int minutes = (int) (seconds / 60);
        return minutes + " phút";
    }
}