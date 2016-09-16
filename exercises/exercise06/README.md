# Exercise 06: Calling the Emotion Service Exposed via YaaS

##Estimated time

15 min

##Objective

In this exercise you'll learn how to use a remote service that is exposed through SAP Hybris as a Service (YaaS). To be more precise, you will extend the Casino Sentiment Service to call the Emotion Service that is accessible through YaaS (see [tutorial overview  picture](../../img/scenario_overview.png)).

#Exercise description

## 1. Extend the Casino Sentiment Service to use the Emotion Service accessibly via YaaS

1. First, you will implement the class ```EmotionService```. This class is meant to encapsulate the remote call to the YaaS Emotion Service. Open the class in the Eclipse Java editor: For this, press ```Ctrl+Shift+T``` and enter ```EmotionService``` in the input field.

2. Now add following method to the class:
   ```
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

 			// PLACEHOLDER CODE SNIPPET 2 		

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
   ```
   Press ```Ctrl+Shift+O``` to add the required import statements (select the classes ```org.springframework.http.HttpHeaders```, ```org.springframework.http.HttpEntity```, ```org.springframework.http.HttpStatus```, ```java.sql.Timestamp```, ```org.springframework.http.MediaType``` when you get asked)..
   <br>
   In the method above, the ```restTemplate``` instance is used to make a POST request against the ```Emotion Service``` accessible through YaaS. The class ```RestTemplate``` is Spring's central class for synchronous client-side HTTP access that facilitates interacting with most RESTful services.

   The URL of the Emotion Service is stored in the constant ```EMOTION_SERVICE_URL```, and its value is ```https://api.yaas.io/hybris/emotionservice/v1/emotion```. Note that the host name of the URL is ```api.yaas.io``` - this is the host of the YaaS proxy that is used when calling business services exposed via YaaS.
   The Emotion Service used in this example doesn't require any authentication for simplicity. However, YaaS allows you to use OAuth2 to protect published services. For more information how to do this in YaaS, please have a look in the [documentation](https://devportal.yaas.io/services/oauth2/latest/index.html).  

## 2. Implement the POST methods of the RootController

3. Now, open again the class ```RootController``` to implement the two POST methods. First, add following property to the class:
   ```
   @Autowired
   private EmotionService emotionService;
   ```
   This allows the class to access an instance of the ```EmotionService``` class.

4. Now search for ```// PLACEHOLDER CODE SNIPPET 3``` and replace the complete body of the respective method by following coding:
   ```
   if (result.hasErrors()) {
      return new ResponseEntity<String>(result.toString(), HttpStatus.BAD_REQUEST);
   }
   return emotionService.addSentimentValue(urlDecode(casinoId), urlDecode(slotMachineId), image.getImageUrl());
   ```
   Press ```Ctrl+Shift+O``` to add needed import statements.

5. Next, search for ```// PLACEHOLDER CODE SNIPPET 4``` and replace the complete body of the respective method by following coding:
   ```
   if (result.hasErrors()) {
      return new ResponseEntity<String>(result.toString(), HttpStatus.BAD_REQUEST);
   }

   // store dataUri in an image file under the resources; URL to image can
   // then be sent to the emotion service
   String img64 = dataUri.getDataUri();
   if (img64 == null) {
      throw new RuntimeException("No 'dataUri' found in body of POST request. Check the request body.");
   }

   // cut off prefix "data:image/jpeg;base64," from data uri and
   // base64-decode image
   img64 = img64.replaceFirst("data:image/jpeg;base64,", "");
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
      if (imageFile != null) {
        // delete file again to clean up
        imageFile.delete();
      }
   }   
   ```
   Press ```Ctrl+Shift+O``` to add the missing import statements (select the classes ```java.io.File``` when you get asked).
   <br>
   This method gets the image as a [data URI](https://en.wikipedia.org/wiki/Data_URI_scheme), stores the data URI in a file in the Web application, then calls the Emotion Service with the image URL, and finally deletes the stored image again.

## 3. Build and deploy the project

1. To build the project, select the project in the Project Explorer, open its context menu and click on ```Run As``` > ```Maven build```. The build should end without any errors.

2. To deploy the project, switch again in your command shell that you opened already in the previous exercise and enter again ```cf push```. After 1 to 2 minutes, the new project state should have been deployed.

3. To test the POST methods of the service, open the REST client program ```Postman```: In your Chrome browser, open a new tab and call the URL ```chrome://apps```. This brings up the page with the browser extensions. Click on the ```Postman``` entry in the list to start Postman.
   ![postman](../../img/img06_01.png?raw=true)

  In Postman, enter the following values: 
  * URL: ```https://sentiment-service-<number>.cfapps.us10.hana.ondemand.com/sentiment/casinos/Las+Vegas+Gamble+Bamba/slotMachines/Blackjack/image```
  * Method: ```POST```
  * Headers:
    ```
     Content-Type = application/json
    ```
  * Body (chose ```raw``` format in Postman):
    ```
     { "imageUrl":"http://superiorplatform.com/pictures-of/faces/angry/03b-angry_kids.jpg"}
    ```
   ![postman example](../../img/img06_02.png?raw=true)
   
  With the URL specified above, you should get back a JSON response containing the property ```"emotion": "anger"```.

##Summary

In this exercise you have learned how you can use services available through YaaS in the implementation of your own microservice. As next step, you will see how to use the Cloud Foundry PostgreSQL backing service to store data in a relational database: [exercise07](../exercise07).
