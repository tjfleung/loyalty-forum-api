package com.terrence.loyalty.forumapi.domain.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrence.loyalty.forumapi.domain.accuweather.City;
import com.terrence.loyalty.forumapi.domain.accuweather.Forecast;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class LocationService {

    private static final String apikey = "xamdlbTIESc68OO6GCrG8JGt6OeHyWcP";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final static String CELSIUS = "C";

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

        String cityKey = "";

        City city = getCity(loc);
        if (city != null) {
            cityKey = city.getKey();
            location.setLatitude(String.valueOf(city.getGeoPosition().getLatitude()));
            location.setLongitude(String.valueOf(city.getGeoPosition().getLongitude()));
        } else {
            location.setLatitude("-");
            location.setLongitude("-");
        }

        Forecast forecast = getForecast(cityKey);
        if (forecast != null) {
            // AccuWeather responds with temperature values in fahrenheit, convert to celsius
            if (forecast.getTemperature().getUnit().equals("F")) {
                double celsiusValue = fahrenheitToCelsius(forecast.getTemperature().getValue());
                forecast.getTemperature().setValue(celsiusValue);
                forecast.getTemperature().setUnit(CELSIUS);
            }
            location.setTemperature(forecast.getTemperature().getValue() + forecast.getTemperature().getUnit());
        } else {
            location.setTemperature("-");
        }

        location.setLocation(loc);
        return location;
    }

    private City getCity(String loc) {
        // fetch city - return null if we cannot find the city
        try {
            ResponseEntity<String> cityResponse = restTemplate.getForEntity(
                    "http://dataservice.accuweather.com/locations/v1/cities/search?apikey={apiKey}&q={loc}",
                    String.class,
                    apikey, loc);
            String res = cityResponse.getBody();

            if (res == null) {
                log.error("Could not find location information for {}", loc);
                throw new RestClientException("Could not find location information for " + loc);
            }

            List<City> cities = objectMapper.readValue(res, new TypeReference<List<City>>() {});

            if (cities != null && cities.size() > 0 && cities.get(0) != null && cities.get(0).getGeoPosition() != null) {
                return cities.get(0);
            } else {
                log.warn("Could not obtain City for {}", loc);
                throw new RestClientException("Could not obtain City for " + loc);
            }
        } catch (RestClientException ex) {
            log.warn("Location request failure.", ex);
        } catch (JsonProcessingException e) {
            log.warn("ObjectMapper failure during location mapping");
            e.printStackTrace();
        }

        return null;
    }

    private Forecast getForecast(String cityKey) {
        // get forecasts - return null if we cannot obtain forecast data
        try {
            ResponseEntity<String> forecastResponse = restTemplate.getForEntity(
                    "http://dataservice.accuweather.com/forecasts/v1/hourly/1hour/{cityKey}?apikey={apiKey}",
                    String.class,
                    cityKey, apikey);
            String res = forecastResponse.getBody();

            if (res == null) {
                log.error("Could not find forecast information for key {}", cityKey);
                throw new RestClientException("Could not find forecast information for " + cityKey);
            }

            List<Forecast> forecasts = objectMapper.readValue(res, new TypeReference<List<Forecast>>() {});

            if (forecasts != null && forecasts.size() > 0 && forecasts.get(0) != null && forecasts.get(0).getTemperature() != null) {
                return forecasts.get(0);
            } else {
                log.warn("Could not obtain forecast for key {}", cityKey);
                throw new RestClientException("Could not obtain forecast for key " + cityKey);
            }
        } catch (RestClientException ex) {
            log.warn("Forecasts request failure.", ex);
        } catch (JsonProcessingException e) {
            log.warn("ObjectMapper failure during forecast mapping");
            e.printStackTrace();
        }

        return null;
    }

    public double fahrenheitToCelsius(double fahrenheit) {
        return (double) Math.round((fahrenheit - 32) * (5.0/9.0));
    }

}
