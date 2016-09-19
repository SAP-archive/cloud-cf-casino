# Exercise 08: Make Service Resilient by Using Netflix Hystrix

##Estimated time

10 min

##Objective

In this exercise you'll learn how to make the Casino Sentiment Service which is a distributed service that depends on the Emotion Service more resilient by using [Netflix Hystrix](https://github.com/Netflix/Hystrix/wiki). It allows you to apply the so-called [circuit breaker pattern](http://martinfowler.com/bliki/CircuitBreaker.html) and use additional fault tolerance logic that kicks-in in case of failures in a distributed service.

#Exercise description

## 1. Use Netflix Hystrix when accessing Emotion Service

1. Open the ```HCPWebApplication``` class in Eclipse and add the annotation ```@EnableHystrixDashboard``` to the class:
   ```
   @SpringBootApplication
   @EnableHystrixDashboard
   public class HCPWebApplication extends SpringBootServletInitializer implements CommandLineRunner {
   ```
   Press ```Ctrl+Shift+O``` to add the missing import statements.
   <br>
   This annotation tells your Spring Boot application to use the Hystrix Dashboard to monitor the health of each circuit breaker in real time. You can then later on access the dashboard using the ```/hystrix.stream``` endpoint in the Casino Sentiment Service  application.

2. Now open the ```EmotionService``` class in Eclipse and add following annotation to the ```addSentimentValue``` method:
   ```
   @HystrixCommand(fallbackMethod = "addSentimentValueFallback", commandProperties = {
              @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
          })
   public ResponseEntity<String> addSentimentValue(String casinoId, String slotMachineId, String imageUrl) { ...
   ```
   Press ```Ctrl+Shift+O``` to add the missing import statements.
   <br>
   This annotation tells the Spring framework to automatically wrap the Spring bean ```EmotionService``` in a proxy that is connected to the Hystrix circuit breaker. The circuit breaker calculates when to open and close the circuit, and what to do in case of a failure. The annotation also specifies that there is a fallback method ```addSentimentValueFallback``` that should be called in case of a failure. The ```commandProperties``` attribute in the annotation is used to configure the ```@HystrixCommand``` with a list of @HystrixProperty annotations. The list of available properties can be found [here](https://github.com/Netflix/Hystrix/wiki/Configuration).

3. Now add the fallback method that should be called in case of a failure in the ```addSentimentValue``` method:
   ```
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
   ```

4. Finally, open the ```RootController``` class in Eclipse, and add the annotation ```@EnableCircuitBreaker``` to the class:
   ```
   @RestController
   @RequestMapping("/sentiment")
   @EnableCircuitBreaker
   public class RootController
   ```
   Press ```Ctril+Shift+O``` to add needed import statements.
   <br>
   This annotation tells the Spring framework that the ```RootController``` class uses circuit breakers and to enable their monitoring, opening, and closing (behavior supplied, in our case, by Hystrix).

## 2. Build and deploy the project

1. To build the project, select the project in the Project Explorer, open its context menu and click on ```Run As``` > ```Maven build```. The build should end without any errors.

2. To deploy the project, switch again in your command shell that you opened already in the previous exercise and enter again ```cf push```. After 1 to 2 minutes, the new project state should have been deployed.

3. Now open in a new browser tab the Hystrix Dashboard using following URL
  ```
  https://sentiment-service-<number>.cfapps.us10.hana.ondemand.com/hystrix
  ```
  In the URL field of the dashboard, enter the URL to the Hystrix stream: 
  ```
  https://sentiment-service-<number>.cfapps.us10.hana.ondemand.com/hystrix.stream
  ```
  This brings up the Hystrix Dashboard showing results for the Hystrix circuit breaker that we have added in this exercise. For more information about the Hystrix Dashboard, see also this [link](https://github.com/Netflix/Hystrix/wiki/Dashboard). 

##Summary

In this exercise you have learned to use Netflix Hystrix to make the service more resilient to failures of a depending remote service. In the next exercise, you will finally use your own Casino Sentiment Service in the Slot Machine Simulation application that you have deployed already in your own HCP account: [exercise09](../exercise09).
