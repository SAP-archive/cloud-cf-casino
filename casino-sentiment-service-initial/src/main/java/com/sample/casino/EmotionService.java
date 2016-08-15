package com.sample.casino;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class EmotionService {

	private static final Logger log = LoggerFactory.getLogger(EmotionService.class);

	// URL to the emotion service exposed via YAAS
	private final static String EMOTION_SERVICE_URL = "https://api.yaas.io/hybris/emotionservice/v1/emotion";

}
