package com.terrence.loyalty.forumapi.domain.location;

import com.terrence.loyalty.forumapi.domain.accuweather.City;
import com.terrence.loyalty.forumapi.domain.accuweather.Forecast;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class LocationService {

    private static final String apikey = "xamdlbTIESc68OO6GCrG8JGt6OeHyWcP";

    /**
     * API key: xamdlbTIESc68OO6GCrG8JGt6OeHyWcP. 50 calls per day at free tier.
     * Design:
     * Make one call to locations endpoint to get lat and long and locationKey.
     * Constraint:
     * Due to only expecting a city name, locations will return a list of cities and only the first city will be consumed.
     * Make a second call to forecasts endpoint using locationKey to get temperature.
     */
    public Location getLocation(String loc) {
        Location location = new Location();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json;charset=utf-8");
        HttpEntity entity = new HttpEntity(headers);

        String cityKey = "";

        // fetch cities
        try {
            ResponseEntity<List<City>> cityResponse = restTemplate.exchange(
                    "http://dataservice.accuweather.com/locations/v1/cities/search?apikey={apiKey}&q={loc}",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<City>>() {},
                    apikey, loc);
            List<City> cities = cityResponse.getBody();
            if (cities != null && cities.get(0) != null && cities.get(0).getGeoPosition() != null) {
                cityKey = cities.get(0).getKey();
                location.setLatitude(String.valueOf(cities.get(0).getGeoPosition().getLatitude()));
                location.setLongitude(String.valueOf(cities.get(0).getGeoPosition().getLongitude()));
            } else {
                throw new RestClientException("Cannot obtain city");
            }
        } catch (RestClientException ex) {
            log.info("Location request failed.", ex);
            location.setLatitude("0");
            location.setLongitude("0");
        }

        // get forecasts
        try {
            ResponseEntity<List<Forecast>> forecastResponse = restTemplate.exchange(
                    "http://dataservice.accuweather.com/forecasts/v1/hourly/1hour/{cityKey}?apikey={apiKey}",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Forecast>>() {},
                    cityKey, apikey);
            List<Forecast> forecasts = forecastResponse.getBody();
            if (forecasts != null && forecasts.get(0) != null && forecasts.get(0).getTemperature() != null) {
                String temp = forecasts.get(0).getTemperature().getValue() + forecasts.get(0).getTemperature().getUnit();
                location.setTemperature(temp);
            } else {
                throw new RestClientException("Cannot parse forecasts");
            }
        } catch (RestClientException ex) {
            log.info("Forecasts request failed", ex);
            location.setTemperature("0");
        }

        location.setLocation(loc);
        return location;
    }
}
