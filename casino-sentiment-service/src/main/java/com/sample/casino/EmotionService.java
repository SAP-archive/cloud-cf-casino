package com.sample.casino;

import java.io.IOException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sample.casino.model.Emotion;
import com.sample.casino.model.EmotionEnum;

@Service
public class EmotionService {

	private static final Logger log = LoggerFactory.getLogger(EmotionService.class);

	// URL to the emotion service exposed via YAAS
	private final static String EMOTION_SERVICE_URL = "https://api.yaas.io/hybris/emotionservice/v1/emotion";

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@HystrixCommand(fallbackMethod = "addSentimentValueFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
        })
	public ResponseEntity<String> addSentimentValue(String casinoId, String slotMachineId, String imageUrl) {
		log.info("Calling emotion service..."); 

		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("{ \"image_url\" : \"" + imageUrl + "\" }", headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> serviceResponse = restTemplate.exchange(EMOTION_SERVICE_URL, HttpMethod.POST, entity,
				String.class);
		
		if (serviceResponse.getStatusCode() == HttpStatus.OK || serviceResponse.getStatusCode() == HttpStatus.CREATED) {
			ObjectMapper mapper = new ObjectMapper();
			Emotion emotion;
			try {
				emotion = mapper.readValue(serviceResponse.getBody(), Emotion.class);
			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			log.info("Emotion service called: "
					+ String.format("image:%s, emotion: %s", imageUrl, emotion.getEmotion()));

			Timestamp now = new Timestamp(System.currentTimeMillis());
			jdbcTemplate.update("INSERT INTO slotmachines(id, imageUrl, date, casinoId, sentiment) VALUES ('"
					+ slotMachineId + "','" + imageUrl + "','" + now + "','" + casinoId + "','"
					+ EmotionEnum.get(emotion).getValue() + "')");

			log.info("Sentiment value added to database for: casinoId=" + casinoId + ", slotMachine="
					+ slotMachineId + ", imageUrl=" + imageUrl);

			EmotionEnum emoti = EmotionEnum.get(emotion);

			final ResponseEntity<String> response = new ResponseEntity<String>(
					String.format(
							"{ \"casinoid\": \"%s\", \"slotmachineid\": \"%s\", \"sentiment\": \"%.1f\", \"emotion\":\"%s\", \"imageurl\":\"%s\", \"timestamp\":\"%s\"  }",
							casinoId, slotMachineId, emoti.getValue(), emoti.toString(), imageUrl, now.toString()),
					HttpStatus.CREATED);

			return response;
		} else {
			throw new RuntimeException(
					"Bad request: " + serviceResponse.getStatusCode() + ". Message: " + serviceResponse.getBody());
		}
	}

	/**
	 * Fallback method called by Hystrix in case emotion service is down.
	 */
	public ResponseEntity<String> addSentimentValueFallback(String casinoId, String slotMachineId, String imageUrl) {		            
		log.info("addSentimentValueFallback: emotion service seems to be down, fallback applied.");
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		final ResponseEntity<String> response = new ResponseEntity<String>(String.format(
				"{ \"casinoid\": \"%s\", \"slotmachineid\": \"%s\", \"sentiment\": \"%.1f\", \"emotion\":\"%s\", \"imageurl\":\"%s\", \"timestamp\":\"%s\"  }",
				casinoId, slotMachineId, EmotionEnum.neutral.getValue(), EmotionEnum.neutral.toString(), imageUrl,
				now.toString()), HttpStatus.SERVICE_UNAVAILABLE);
		return response;
	}
}
