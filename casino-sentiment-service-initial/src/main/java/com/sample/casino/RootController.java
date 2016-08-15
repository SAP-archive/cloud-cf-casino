package com.sample.casino;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * This class implements the REST API of the casino-sentiment-service. 
 * 
 * Spring MVC is used to annotate the class and its methods as REST endpoints. 
 */
public class RootController {
	
    private static final Logger log = LoggerFactory.getLogger(RootController.class);
   
	
	private String getServerAddress(String filename) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes!=null && requestAttributes instanceof ServletRequestAttributes) {
		  HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
		  StringBuffer url = request.getRequestURL();
		  String uri = request.getRequestURI();
		  String hostUrl = url.substring(0, url.indexOf(uri));
		  String imageUri = hostUrl + "/" + filename;
		  log.info("URL to image file: " + imageUri);
		  return imageUri;
		}
		throw new IllegalStateException("unexpected state");
	}
	
	private String urlDecode(String value) {
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String casinoToJson(String casinoId, double sentiment) {
		return String.format("{ \"casinoid\": \"%s\", \"sentiment\": %.1f }", casinoId, sentiment);
	}
	
	private String slotMachineToJson(String casinoId, String slotMachineId, double sentiment) {
		return String.format("{ \"casinoid\": \"%s\", \"slotmachineid\": \"%s\", \"sentiment\": %.1f }",
				casinoId, slotMachineId, sentiment);
	}
	
}
