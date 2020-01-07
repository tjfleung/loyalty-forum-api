package com.terrence.loyalty.forumapi.domain.message;

import com.terrence.loyalty.forumapi.domain.accuweather.City;
import com.terrence.loyalty.forumapi.domain.accuweather.Forecast;
import com.terrence.loyalty.forumapi.domain.location.Location;
import com.terrence.loyalty.forumapi.exception.ForumException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageService {

    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    private MessageAdapter messageAdapter;

    public MessageService(MessageRepository messageRepository, MessageAdapter messageAdapter) {
        this.messageRepository = messageRepository;
        this.messageAdapter = messageAdapter;
    }

    public List<MessageDto> getAllMessages() {
        List<MessageDto> parentMessageDtos = new ArrayList<>();
        parentMessageDtos.addAll(
                messageRepository.findAllByParentIdEqualsOrderByPostedDateDesc(0)
                        .stream()
                        .map(message -> messageAdapter.adapt(message))
                        .collect(Collectors.toList())
        );

        for (MessageDto parent : parentMessageDtos) {
            parent.addChildMessages(findChildMessages(parent.getId()));
        }

        return parentMessageDtos;
    }

    public MessageDto save(MessageDto messageDto) throws ForumException {
        if (messageDto.getUsername() == null || messageDto.getUsername().isEmpty()) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "No username provided");
        }
        if (messageDto.getComment() == null || messageDto.getComment().isEmpty()) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "No comment to post");
        }

        Location location = null;
        if (messageDto.getLocation() != null) {
            location = getLocationDetails(messageDto.getLocation().getLocation());
        }

        Message savedMessage = messageRepository.save(new Message(messageDto.getUsername(), LocalDateTime.now(), messageDto.getComment(), location));
        log.info("Saved new message: {}, From: {}", savedMessage.getComment(), savedMessage.getUsername());
        return messageAdapter.adapt(savedMessage);
    }

    public List<MessageDto> getMessagesByUsername(String username) {
        return messageRepository.findAllByUsernameOrderByPostedDateDesc(username)
                .stream()
                .map(message -> messageAdapter.adapt(message))
                .collect(Collectors.toList());
    }

    public MessageDto saveReply(MessageDto messageDto, long parentId) {
        if (parentId <= 0) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "Invalid comment to reply");
        }
        if (messageDto.getUsername() == null || messageDto.getUsername().isEmpty()) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "No username provided");
        }
        if (messageDto.getComment() == null || messageDto.getComment().isEmpty()) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "No comment to post");
        }

        Location location = null;
        if (messageDto.getLocation() != null && !messageDto.getLocation().getLocation().isEmpty()) {
            location = getLocationDetails(messageDto.getLocation().getLocation());
        }

        Message reply = messageRepository.save(new Message(messageDto.getUsername(), LocalDateTime.now(), messageDto.getComment(), parentId, location));
        log.info("Saved new reply: {}, from {}, replying to Message {}", reply.getComment(), reply.getUsername(), reply.getParentId());
        return messageAdapter.adapt(reply);
    }

    private List<MessageDto> findChildMessages(long parentId) {
        List<Message> childMessages = messageRepository.findAllByParentIdEqualsOrderByPostedDateDesc(parentId);
        if (childMessages == null) {
            return null;
        }

        List<MessageDto> childDtos = childMessages
                .stream()
                .map(message -> messageAdapter.adapt(message))
                .collect(Collectors.toList());

        for (MessageDto parent : childDtos) {
            parent.addChildMessages(findChildMessages(parent.getId()));
        }

        return childDtos;
    }


    private List<MessageDto> findChildMessages(String username, long parentId) {
        List<Message> childMessages = messageRepository.findAllByUsernameAndParentIdOrderByPostedDateDesc(username, parentId);
        if (childMessages == null) {
            return null;
        }

        List<MessageDto> childDtos = childMessages
                .stream()
                .map(message -> messageAdapter.adapt(message))
                .collect(Collectors.toList());

        for (MessageDto parent : childDtos) {
            parent.addChildMessages(findChildMessages(username, parent.getId()));
        }

        return childDtos;
    }

    /**
     * API key: xamdlbTIESc68OO6GCrG8JGt6OeHyWcP. 50 calls per day at free tier.
     * Design:
     * Make one call to locations endpoint to get lat and long and locationKey
     * Make a second call to forecasts endpoint using locationKey to get temperature
     */
    private Location getLocationDetails(String loc) {
        Location location = new Location();

        String apikey = "xamdlbTIESc68OO6GCrG8JGt6OeHyWcP";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json;charset=utf-8");
        HttpEntity entity = new HttpEntity(headers);

        String locationsUrl = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey="
                + apikey +"&q=" + loc;
        String cityKey = "";
        try {
            ResponseEntity<List<City>> cityResponse = restTemplate.exchange(locationsUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<City>>() {
                    });
            List<City> cities = cityResponse.getBody();
            if (cities != null && cities.get(0) != null && cities.get(0).getGeoPosition() != null) {
                cityKey = cities.get(0).getKey();
                location.setLatitude(String.valueOf(cities.get(0).getGeoPosition().getLatitude()));
                location.setLongitude(String.valueOf(cities.get(0).getGeoPosition().getLongitude()));
            } else {
                throw new RestClientException("cities null values");
            }
        } catch (RestClientException ex) {
            log.info("Location request failed.", ex);
            //default values to Toronto for development purposes only
            cityKey = "55488";
            location.setLatitude("43.64");
            location.setLongitude("-79.38");
        }

        String forecastsUrl = "http://dataservice.accuweather.com/forecasts/v1/hourly/1hour/" + cityKey
                + "?apikey=" + apikey +"&q=" + loc;
        try {
            ResponseEntity<List<Forecast>> forecastResponse = restTemplate.exchange(forecastsUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Forecast>>() {});
            List<Forecast> forecasts = forecastResponse.getBody();
            if (forecasts != null && forecasts.get(0) != null && forecasts.get(0).getTemperature() != null) {
                String temp = forecasts.get(0).getTemperature().getValue() + forecasts.get(0).getTemperature().getUnit();
                location.setTemperature(temp);
            } else {
                throw new RestClientException("Cannot parse forecasts");
            }
        } catch (RestClientException ex) {
            log.info("Forecasts request failed", ex);
            location.setTemperature("-1C");
        }

        location.setLocation(loc);
        return location;
    }
}
