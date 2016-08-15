package com.sample.casino;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sample.casino.model.AggregationResult;
import com.sample.casino.model.DataUri;
import com.sample.casino.model.QueryResult;
import com.sample.casino.model.SlotMachineImage;


/**
 * This class implements the REST API of the casino-sentiment-service. 
 * 
 * Spring MVC is used to annotate the class and its methods as REST endpoints. 
 */
@RestController
@RequestMapping("/sentiment")
@EnableCircuitBreaker
public class RootController {
	
    private static final Logger log = LoggerFactory.getLogger(RootController.class);

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    private EmotionService emotionService;

    /**
     * Returns aggregated sentiment values for all casinos.
     */
    @RequestMapping(path = "/casinos", method = RequestMethod.GET, produces={"application/json"})
    public ResponseEntity<String> getSentimentForCasinos() {       
		final List<QueryResult> slotMachines = jdbcTemplate.query(
				"SELECT casinoid, COUNT(*), SUM(sentiment) FROM slotmachines GROUP BY casinoid",
				new BeanPropertyRowMapper<QueryResult>(QueryResult.class));

        StringBuilder builder = new StringBuilder();
        builder.append("{ \"casinos\" : [");
        for (Iterator<QueryResult> iterator = slotMachines.iterator(); iterator.hasNext();) {
        	QueryResult query = iterator.next();
        	
			log.info("CasinoId: " + query.getCasinoid() + ", count: " + query.getCount() + ", sentiment sum: " + query.getSum());
		
			builder.append(casinoToJson(query.getCasinoid(), query.getAverageSentiment()));
			if (iterator.hasNext()) {
				builder.append(",");
			}
		}
        builder.append("]}");

		final ResponseEntity<String> response = new ResponseEntity<String>(builder.toString(), HttpStatus.OK);
        return response;
    }

    /**
     * Returns aggregated sentiment value for the specified casino.
     */
    @RequestMapping(path = "/casinos/{casinoId}", method = RequestMethod.GET, produces={"application/json"})
    public  ResponseEntity<String> getSentimentForCasino(@PathVariable String casinoId) {
        casinoId = urlDecode(casinoId); 
        
        final List<QueryResult> slotMachines = jdbcTemplate.query(
				"SELECT casinoid, COUNT(*), SUM(sentiment) FROM slotmachines WHERE casinoid=? GROUP BY casinoid",
				new String[] { casinoId }, new BeanPropertyRowMapper<QueryResult>(QueryResult.class));

        StringBuilder builder = new StringBuilder();
        for (Iterator<QueryResult> iterator = slotMachines.iterator(); iterator.hasNext();) {
        	QueryResult query = iterator.next();
        	
			log.info("CasinoId: " + casinoId + ", count: " + query.getCount() + ", sentiment sum: " + query.getSum());
			
			builder.append(casinoToJson(casinoId, query.getAverageSentiment()));
			if (iterator.hasNext()) {
				throw new IllegalStateException("just one record expected as result");
			}
		}

        log.info("Aggregated sentiment value for casino " + casinoId + ": " + builder.toString());

		final ResponseEntity<String> response = new ResponseEntity<String>(builder.toString(), HttpStatus.OK);
        return response;
    }

    /**
     * Returns average sentiment values for each slot machine of the given casino.
     */
    @RequestMapping(path = "/casinos/{casinoId}/slotMachines", method = RequestMethod.GET, produces={"application/json"})
    public ResponseEntity<String> getSentimentOfSlotMachinesForCasino(@PathVariable String casinoId) {
        casinoId = urlDecode(casinoId);
    	
		final List<AggregationResult> slotMachines = jdbcTemplate.query(
				"SELECT id, COUNT(*), SUM(sentiment) FROM slotmachines WHERE casinoid=? GROUP BY id",
				new String[] { casinoId }, new BeanPropertyRowMapper<AggregationResult>(AggregationResult.class));

        StringBuilder builder = new StringBuilder();
        builder.append("{ \"slotmachines\" :[");
        for (Iterator<AggregationResult> iterator = slotMachines.iterator(); iterator.hasNext();) {
			AggregationResult query = iterator.next();
			
			log.info("CasinoId: " + query.getCasinoid() + ", slotMachineId: " + query.getId() + ", count: " + query.getCount() + ", sentiment sum: " + query.getSum());
			
			builder.append(slotMachineToJson(casinoId, query.getId(), query.getAverageSentiment()));
			if (iterator.hasNext()) {
				builder.append(",");
			}
		}
        builder.append("]}");

        log.info("Average sentiment value for all slot machines of casino " + casinoId + ": " + builder.toString());

		final ResponseEntity<String> response = new ResponseEntity<String>(builder.toString(), HttpStatus.OK);
        return response;
    }

    /**
     * Returns average sentiment value for the specified slot machine of the given casino.
     */
    @RequestMapping(path = "/casinos/{casinoId}/slotMachines/{slotMachineId}", method = RequestMethod.GET, produces={"application/json"})
    public ResponseEntity<String> getSentimentOfSlotMachineForCasino(@PathVariable String casinoId, @PathVariable String slotMachineId) {
        casinoId = urlDecode(casinoId);
        slotMachineId = urlDecode(slotMachineId);

        final List<QueryResult> slotMachines = jdbcTemplate.query("SELECT id, casinoid, sentiment FROM slotmachines WHERE casinoid=? AND id=?", new String[] { casinoId, slotMachineId },
                new BeanPropertyRowMapper<QueryResult>(QueryResult.class));

        double sumSentiment = 0.0;
        for (Iterator<QueryResult> iterator = slotMachines.iterator(); iterator.hasNext();) {
			QueryResult slotMachine = (QueryResult) iterator.next();
			sumSentiment += slotMachine.getSentiment();
		}
        double avgSentiment = sumSentiment/slotMachines.size();

        log.info("Average sentiment value of slotmachine " + slotMachineId + " for casino " + casinoId + ": " + avgSentiment);

		final ResponseEntity<String> response = new ResponseEntity<String>(slotMachineToJson(casinoId, slotMachineId, avgSentiment),
				HttpStatus.OK);

        return response;
    }

    /**
     * Method is used to post a new image to the sentiment service. The sentiment service internally calls the 
     * emotion service to retrieve the emotion value detected in the image, and stores it in the <code>slotmachines</code> 
     * table for the given casino and slot machine id. 
     */
	@RequestMapping(path = "/casinos/{casinoId}/slotMachines/{slotMachineId}/image", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<String> addSentimentForSlotMachine(@PathVariable String casinoId,
			@PathVariable String slotMachineId, @Validated @RequestBody SlotMachineImage image, BindingResult result) {
		if (result.hasErrors()) {
            return new ResponseEntity<String>(result.toString(), HttpStatus.BAD_REQUEST);
        }
		
		return emotionService.addSentimentValue(urlDecode(casinoId), urlDecode(slotMachineId), image.getImageUrl());
    }

	/**
     * Method is used to post a new image as data URI to the sentiment service. The sentiment service internally stores the image 
     * in a file and then calls the emotion service to retrieve the emotion value detected in the image, and stores it in the 
     * <code>slotmachines</code> table for the given casino and slot machine id. 
     */
	@RequestMapping(path = "/casinos/{casinoId}/slotMachines/{slotMachineId}/datauri", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<String> addSentimentForSlotMachine(@PathVariable String casinoId,
			@PathVariable String slotMachineId, @Validated @RequestBody DataUri dataUri, BindingResult result) {
		if (result.hasErrors()) {
            return new ResponseEntity<String>(result.toString(), HttpStatus.BAD_REQUEST);
        }

		// store dataUri in an image file under the resources; URL to image can then be sent to the emotion service
		String img64 = dataUri.getDataUri();
		if (img64==null) {
			throw new RuntimeException("No 'dataUri' found in body of POST request. Check the request body.");
		}

		// cut off prefix "data:image/jpeg;base64," from data uri and base64-decode image
        img64 = img64.replaceFirst("data:image/jpeg;base64,","");
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(img64);
        
        // store image in file
		File imageFile = null; 
        try {
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(decodedBytes));
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			String filename = "image_" + uuid + ".jpg";
			imageFile = new File(filename);
			ImageIO.write(bufferedImage, "jpg", imageFile);

			String imageUrl = getServerAddress(filename);
			SlotMachineImage image = new SlotMachineImage();
			image.setImageUrl(imageUrl);
			
			return emotionService.addSentimentValue(urlDecode(casinoId), urlDecode(slotMachineId), image.getImageUrl());
		} catch (Exception e) {
			log.error("Exception occured in RootController.addSentimentValueForSlotMachine: ", e);
			throw new RuntimeException(e);
		} finally {
			if (imageFile!=null) {
				// delete file again to clean up
				imageFile.delete();
			}
		}
    }
	
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
