# Exercise 07: Using PostgreSQL to Track Sentiment Values

##Estimated time

15 min

##Objective

In this exercise you'll learn how to use the PostgreSQL service provided on HCP Cloud Foundry to store sentiment values associated to casinos and slot machines in a relational database, as a general example how to use backing services of Cloud Foundry.

#Exercise description

## 1. Create PostgreSQL service instance and bind it to applciation   

1. In the ```cf``` command shell, enter ```cf marketplace```. This lists the backing services available on the platform. Note that it contains an entry ```postgresql``` like the following:
   ```
   service      plans            description   
   postgresql   v9.4-container   PostgreSQL object-relational database system  
   ```

2. In your terminal, enter ```cf create-service postgresql v9.4-container postgres-service```. This creates a service instance with name ```postrgres-service``` of the ```postgresql``` backing service, using the service plan ```v9.4-container```. Note, that the name of the service plan (in the example here ```v9.4-container```) will change over time when new versions of the service become available. At the time when you run through this exercise, chose the ```xyz-container``` service plan that is available then.  

3. Now, switch into Eclipse again and open the Cloud Foundry application manifest [manifest.yml](https://docs.cloudfoundry.org/devguide/deploy-apps/manifest.html) that you find in the root directory of the project. At the end of the file, add following entry:
   ```
   services:
   - postgres-service
   ```
   This binds the postgres-service instance that you have just created, to your Casino Sentiment Service application. Next time the application gets deployed, the application can make use of the bound PostgreSQL service instance. The Spring framework will also detect this service binding and auto-wire the PostgreSQL service instance to a ```@JdbcTemplate``` property (see next section).   

4. Open the ```pom.xml``` file that you find in the root directory of the project, and uncomment following dependency to the Spring Boot JPA package:
  ```
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  ```

## 2. Create the database schema

1. In Eclipse, open the ```HCPWebApplication``` class and add following property to the class:
   ```
   @Autowired
   JdbcTemplate jdbcTemplate;
   ```
   Press again ```Ctrl+Shift+O``` to add the needed import statements.
   <br>
   The Spring framework uses ```JdbcTemplate``` as its central JDBC abstraction to interact with relational databases via JDBC. You can find more information on it [here](http://www.tutorialspoint.com/spring/spring_jdbc_framework.htm). The property is automatically initialized with the ```postgres-service``` instance that you bound to the application and declared in the ```manifest.yml```.

2. In the class, search and replace ```// PLACEHOLDER CODE SNIPPET 1``` by following code block:
    ```
    // create table for stickiness values of slot machines
    jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS slotmachines(" +
              "id VARCHAR(64), " +
          "imageUrl VARCHAR(256), " +
              "date TIMESTAMP, " +
              "casinoid VARCHAR(64), " +
              "sentiment FLOAT CHECK (sentiment>=0.0 AND sentiment<=100.0), " +
              "emotion VARCHAR(32))");
    ```
    This method is called once the Spring Boot application is started. It creates a table with name ```slotmachines```. This table will be used in this sample service to store sentiment values associated to casinos (represented as ```casinoid```) and slot machines (represented as ```id```).

## 3. Write sentiment values to the database  

1. Now open the ```EmotionService``` class in Eclipse again and add following property to the class:
   ```
   @Autowired
   JdbcTemplate jdbcTemplate;
   ```
   Press again ```Ctrl+Shift+O``` to add the needed import statements.

2. Next search for ```// PLACEHOLDER CODE SNIPPET 2``` in the class and replace it by following code block:
   ```
   jdbcTemplate.update("INSERT INTO slotmachines(id, imageUrl, date, casinoId, sentiment, emotion) VALUES ('"
       + slotMachineId + "','" + imageUrl + "','" + now + "','" + casinoId + "','"
       + EmotionEnum.get(emotion).getValue() + "','" + emotion.getEmotion() + "')");

   log.info("Sentiment value added to database for: casinoId=" + casinoId + ", slotMachine="
       + slotMachineId + ", imageUrl=" + imageUrl);
   ```
   Press ```Ctrl+Shift+O``` to add the needed import statements.
   <br>
   The SQL INSERT statement writes s new record into the slotmachines table, containing information of the newly POSTED image and its detected emotion value.

## 4. Complement the GET methods of the REST API    

1. Open the class ```RootController``` in Eclipse and add following property to the class:
   ```
   @Autowired
   JdbcTemplate jdbcTemplate;
   ```
   The Spring framework will automatically initialize this property with the ```postgres-service``` instance that is bound to this application.

2. Now search for ```// PLACEHOLDER CODE SNIPPET 5``` and replace the complete method body with following code block:
   ```
   final List<QueryResult> slotMachines = jdbcTemplate.query(
				"SELECT casinoid, COUNT(*), SUM(sentiment) FROM slotmachines GROUP BY casinoid",
				new BeanPropertyRowMapper<QueryResult>(QueryResult.class));

	 StringBuilder builder = new StringBuilder();
	 builder.append("{ \"casinos\" : [");
	 for (Iterator<QueryResult> iterator = slotMachines.iterator(); iterator.hasNext();) {
		 QueryResult query = iterator.next();

		 log.info("CasinoId: " + query.getCasinoid() + ", count: " + query.getCount() + ", sentiment sum: "
	 			 + query.getSum());

		 builder.append(casinoToJson(query.getCasinoid(), query.getAverageSentiment()));
		 if (iterator.hasNext()) {
			 builder.append(",");
		 }
		}
		builder.append("]}");

		final ResponseEntity<String> response = new ResponseEntity<String>(builder.toString(), HttpStatus.OK);
		return response;
    ```
    Press ```Ctrl+Shift+O``` to add the needed import statements (select class ```java.util.Iterator``` and ```java.util.List``` when you get asked).
    <br>
    The method first uses the ```jdbcTemplate``` to query data from the slotmachines table and then creates the JSON response with  aggregated sentiment values for each casino.

3. Next, search for ```// PLACEHOLDER CODE SNIPPET 6``` and replace the complete method body with following code block:
   ```
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
   ```
   Press ```Ctrl+Shift+O``` to add the needed import statements.

4. Next, search for ```// PLACEHOLDER CODE SNIPPET 7``` and replace the complete method body with following code block:
   ```
   casinoId = urlDecode(casinoId);

   final List<AggregationResult> slotMachines = jdbcTemplate.query(
       "SELECT id, COUNT(*), SUM(sentiment) FROM slotmachines WHERE casinoid=? GROUP BY id",
       new String[] { casinoId }, new BeanPropertyRowMapper<AggregationResult>(AggregationResult.class));

   StringBuilder builder = new StringBuilder();
   builder.append("{ \"slotmachines\" :[");
   for (Iterator<AggregationResult> iterator = slotMachines.iterator(); iterator.hasNext();) {
     AggregationResult query = iterator.next();

     log.info("CasinoId: " + query.getCasinoid() + ", slotMachineId: " + query.getId() + ", count: "
         + query.getCount() + ", sentiment sum: " + query.getSum());

     builder.append(slotMachineToJson(casinoId, query.getId(), query.getAverageSentiment()));
     if (iterator.hasNext()) {
       builder.append(",");
     }
   }
   builder.append("]}");

   log.info("Average sentiment value for all slot machines of casino " + casinoId + ": " + builder.toString());

   final ResponseEntity<String> response = new ResponseEntity<String>(builder.toString(), HttpStatus.OK);
   return response;
   ```
   Press ```Ctrl+Shift+O``` to add the needed import statements.

5. Next, search for ```// PLACEHOLDER CODE SNIPPET 8``` and replace the complete method body with following code block:
   ```
   casinoId = urlDecode(casinoId);
   slotMachineId = urlDecode(slotMachineId);

   final List<QueryResult> slotMachines = jdbcTemplate.query(
       "SELECT id, casinoid, sentiment FROM slotmachines WHERE casinoid=? AND id=?",
       new String[] { casinoId, slotMachineId }, new BeanPropertyRowMapper<QueryResult>(QueryResult.class));

   double sumSentiment = 0.0;
   for (Iterator<QueryResult> iterator = slotMachines.iterator(); iterator.hasNext();) {
     QueryResult slotMachine = (QueryResult) iterator.next();
     sumSentiment += slotMachine.getSentiment();
   }
   double avgSentiment = sumSentiment / slotMachines.size();

   log.info("Average sentiment value of slotmachine " + slotMachineId + " for casino " + casinoId + ": "
       + avgSentiment);

   final ResponseEntity<String> response = new ResponseEntity<String>(
       slotMachineToJson(casinoId, slotMachineId, avgSentiment), HttpStatus.OK);

   return response;
   ```
   Press ```Ctrl+Shift+O``` to add the needed import statements.

   The REST API of the class ```RootController``` is now fully implemented.

## 5. Build and deploy the project

1. To build the project, select the project in the Project Explorer, open its context menu and click on ```Run As``` > ```Maven build```. The build should end without any errors.

2. To deploy the project, switch again in your command shell that you opened already in the previous exercise and enter again ```cf push```. After 1 to 2 minutes, the new project state should have been deployed.

3. To test the persistence layer that you have developed, first post again an image. With ```Postman```, trigger a request to:

   URL: ```https://sentiment-service-<number>.cfapps.us10.hana.ondemand.com/sentiment/casinos/Las+Vegas+Gamble+Bamba/slotMachines/Blackjack/image```

   Method: ```POST```

   Headers:
   ```
   Content-Type = application/json
   ```

   Body (chose ```raw``` format in Postman):
   ```
   { "imageUrl":"http://superiorplatform.com/pictures-of/faces/angry/03b-angry_kids.jpg"}
   ```

  Now try to read the data again by using a GET method: In your Chrome browser, open a new tab and call the following URL:
  ```
  https://sentiment-service-<number>.cfapps.us10.hana.ondemand.com/sentiment/casinos/Las+Vegas+Gamble+Bamba/slotMachines/Blackjack
  ```

  This should return a list of JSON objects which are already persisted for the specified casino and slot machine.   

##Summary

In this exercise you have learned to use the PostgreSQL backing service of HCP Cloud Foundry to store sentiment values of gamblers. In the next exercise you will use Hystrix to make the service more resilient: [exercise08](../exercise08).
