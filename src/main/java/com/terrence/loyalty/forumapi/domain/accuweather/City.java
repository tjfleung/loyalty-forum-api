package com.terrence.loyalty.forumapi.domain.accuweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity for the AccuWeather city response
 * Minimum fields required are:
 *  - key -> this is needed to call the forecasts API
 *  - geoPosition -> contains the lat and long values
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class City {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("GeoPosition")
    private GeoPosition geoPosition;
}
