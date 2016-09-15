# Exercise 05: Develop the Casino Sentiment Service REST API

##Estimated time

15 min

##Objective

In this exercise you'll learn how to develop the REST API of the Casino Sentiment Service using the Spring framework. You will also deploy and run the service in your HCP Cloud Foundry account and test the REST API.

#Exercise description

## 1. Declare the project to become a Spring Boot application

1. In Eclipse, open the class ```HCPWebApplication```: press ```Ctrl+Shift+T```, then type ```HCPWebApplication``` in the input field, and select the class. Note that the class is annotated with ```@SpringBootApplication``` and the class declaration extends ```SpringBootServletInitializer```. This declares the class to be a [Spring Boot application](http://projects.spring.io/spring-boot/). Spring Boot packages applications as normal JAR files which contain an embedded Tomcat runtime container and thus can be started as standalone Java applications. Also note that the Spring Boot application get launched in the ```main(...)``` method.
<br><br>
![Eclipse in Windows Start menu](../../img/img05_01.png?raw=true)
<br><br>

## 2. Define the REST API of the service

1. In Eclipse, open the class ```RootController``` (you can do this e.g. by entering ```Ctrl+Shift+T``` and then typing ```RootController``` in the input field). Annotate the class with ```@RestController``` and ```@RequestMapping("/sentiment")```.  
<br><br>
![Eclipse in Windows Start menu](../../img/img05_02.png?raw=true)
<br><br>
   Press ```Ctrl+Shift+O``` to add the missing import statements.
   The ```@RestController``` annotation declares the class as a component to handle HTTP requests, and the ```@RequestMapping("/sentiment")``` annotation tells Spring to map HTTP requests to URL path "/sentiment" to this controller class.

2. Now add following method to the class:
   ```
   /**
     * Returns aggregated sentiment values for all casinos.
     */
    @RequestMapping(path = "/casinos", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<String> getSentimentForCasinos() {
       // PLACEHOLDER CODE SNIPPET 4
       final ResponseEntity<String> response = new ResponseEntity<String>("{ \"text\": \"Hello World 1\"}", HttpStatus.OK);
       return response;
    }
    ```
    Press ```Ctrl+Shift+O``` to add the missing import statements (select class ```org.springframework.http.HttpStatus``` if you are asked for selection).
    <br>
    The ```@RequestMapping(...)``` annotation declares the method to be the handler for GET requests to ```/sentiment/casinos``` with the expected response type to be a JSON object. In this stage, the method returns a hard-coded ```Hello World 1``` value marshalled in a JSON object.

3. Analogously, now add the additional REST endpoints of the service. Copy and paste following methods into the ```RootController``` class:
   ```
   /**
   * Returns aggregated sentiment value for the specified casino.
   */
  @RequestMapping(path = "/casinos/{casinoId}", method = RequestMethod.GET, produces = { "application/json" })
  public ResponseEntity<String> getSentimentForCasino(@PathVariable String casinoId) {
    // PLACEHOLDER CODE SNIPPET 5
    final ResponseEntity<String> response = new ResponseEntity<String>("{ \"text\": \"Hello World 2\"}", HttpStatus.OK);
    return response;
  }

  /**
   * Returns average sentiment values for each slot machine of the given
   * casino.
   */
  @RequestMapping(path = "/casinos/{casinoId}/slotMachines", method = RequestMethod.GET, produces = {
      "application/json" })
  public ResponseEntity<String> getSentimentOfSlotMachinesForCasino(@PathVariable String casinoId) {
    // PLACEHOLDER CODE SNIPPET 6
    final ResponseEntity<String> response = new ResponseEntity<String>("{ \"text\": \"Hello World 3\"}", HttpStatus.OK);
    return response;
  }

  /**
   * Returns average sentiment value for the specified slot machine of the
   * given casino.
   */
  @RequestMapping(path = "/casinos/{casinoId}/slotMachines/{slotMachineId}", method = RequestMethod.GET, produces = {
      "application/json" })
  public ResponseEntity<String> getSentimentOfSlotMachineForCasino(@PathVariable String casinoId,
      @PathVariable String slotMachineId) {
    // PLACEHOLDER CODE SNIPPET 7
    final ResponseEntity<String> response = new ResponseEntity<String>("{ \"text\": \"Hello World 4\"}", HttpStatus.OK);
    return response;
  }

  /**
   * Method is used to post a new image to the sentiment service. The
   * sentiment service internally calls the emotion service to retrieve the
   * emotion value detected in the image, and stores it in the
   * <code>slotmachines</code> table for the given casino and slot machine id.
   */
  @RequestMapping(path = "/casinos/{casinoId}/slotMachines/{slotMachineId}/image", method = RequestMethod.POST, produces = {
      "application/json" })
  public ResponseEntity<String> addSentimentForSlotMachine(@PathVariable String casinoId,
      @PathVariable String slotMachineId, @Validated @RequestBody SlotMachineImage image, BindingResult result) {
    // PLACEHOLDER CODE SNIPPET 3
    final ResponseEntity<String> response = new ResponseEntity<String>("{ \"text\": \"Hello World 5\"}", HttpStatus.OK);
    return response;
  }

  /**
   * Method is used to post a new image as data URI to the sentiment service.
   * The sentiment service internally stores the image in a file and then
   * calls the emotion service to retrieve the emotion value detected in the
   * image, and stores it in the <code>slotmachines</code> table for the given
   * casino and slot machine id.
   */
  @RequestMapping(path = "/casinos/{casinoId}/slotMachines/{slotMachineId}/datauri", method = RequestMethod.POST, produces = {
      "application/json" })
  public ResponseEntity<String> addSentimentForSlotMachine(@PathVariable String casinoId,
      @PathVariable String slotMachineId, @Validated @RequestBody DataUri dataUri, BindingResult result) {
    // PLACEHOLDER CODE SNIPPET 4
    final ResponseEntity<String> response = new ResponseEntity<String>("{ \"text\": \"Hello World 6\"}", HttpStatus.OK);
    return response;
  }
   ```
   Press ```Ctrl+Shift+O``` to add the missing import statements (select class ```org.springframework.http.HttpStatus``` if you are asked for selection).
   <br>
   Note that the API consists of four GET methods and two POST methods. In the method declarations, the ```@PathVariable``` annotation is used to provide URL path parameters as input to the methods. Analogously, the body of a request is provided as input parameter to a method by using the ```@RequestBody``` annotation. Spring transforms a request body that contains a JSON object automatically into a Bean class, like in the last method above that uses the ```DataUri``` Bean.

## 3. Build and deploy the project

1. To build the project, select the project in the Project Explorer, open its context menu and click on ```Run As``` > ```Maven build```. The build should end without any errors.

2. To deploy the project, switch again in your command shell that you opened already in the previous exercise and enter again ```cf push```. After 1 to 2 minutes, the new project state should have been deployed.

3. Now refresh the application in the Web browser and test whether the REST API is alive: try out the URLs to the GET methods, with ```<number>``` again replaced by your workplace number:
   ```
   https://https://sentiment-service-<number>.cfapps.us10.hana.ondemand.com/sentiment/casinos
   https://https://sentiment-service-<number>.cfapps.us10.hana.ondemand.com/sentiment/casinos/Las+Vegas+1
   https://https://sentiment-service-<number>.cfapps.us10.hana.ondemand.com/sentiment/casinos/Las+Vegas+1/slotMachines
   https://https://sentiment-service-<number>.cfapps.us10.hana.ondemand.com/sentiment/casinos/Las+Vegas+1/slotMachines/test1
   ```
   As response, you should see the different "Hello World X" strings in the browser.

##Summary

In this exercise you have learned how to use the Spring framework to define a Spring Boot application and the REST API of the Casino Sentiment Service. Continue now to implement the POST methods which calculate the emotion value detected on the human face of a posted image. This method will use the Emotion Service which is available through YaaS: [exercise06](../exercise06).
