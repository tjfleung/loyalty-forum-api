package com.terrence.loyalty.forumapi.domain.accuweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Temperature {
    @JsonProperty("Value")
    private double value;
    @JsonProperty("Unit")
    private String unit;
}
