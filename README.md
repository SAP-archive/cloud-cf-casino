[![REUSE status](https://api.reuse.software/badge/github.com/SAP-samples/cloud-cf-casino)](https://api.reuse.software/info/github.com/SAP-samples/cloud-cf-casino)

# cloud-cf-casino

Sample of a microservice running on SAP HANA Cloud Platform, Cloud Foundry services.

This tutorial shows 
* how to get access to the Cloud Foundry services beta version of HCP,
* how to administer your Cloud Foundry account using the HCP cloud cockpit,
* how to use basic concept of Cloud Foundry, like backing services, 
* how to use application frameworks like Spring, Spring Boot and Spring Cloud to efficiently develop microservices,
* how to use Hystrix Netflix in your cloud service to make it more resilient against failures,
* how to use a service that runs on Cloud Foundry from existing HCP runtimes, like HTML5 applications.

# Scenario

Put yourself into the shoes of a Casino chain owner who wants to know which gambling and slot machines in the casinos are used most frequently by the customers. The owner wants to make a sentiment analysis of the gamblers who are using the slot machines in order to find out whether there is a correlation between the usage during of the slot machines and the emotions of the gamblers. 

For this reason, the owner has equipped all slot machines of the casinos with cameras which take photos of gamblers every few seconds. The owner got to know that there is an Emotion Service offered by SAP Hybris as a Service (YaaS) that is able to extract emotions from images of human faces – this is exactly what he was looking for. He now hires a Software Developer – you! – who should develop a Sentiment Service on a cloud platform in a short period of time that makes use of the Emotion Service to track the emotions of gamblers for the single slot machines and casinos over time. The developer should furthermore develop a test application that simulates a slot machine and its camera, as well as Casino and Slot Machines Overview applications which provide an overview of the sentiments on casino and slot machine level. As platform of choice, the casino owner has selected SAP HANA Cloud Platform, as he heard that it is built on open standards and provides a Cloud Foundry offering, so he assumes that the task to find an experienced developer should be easy.    
  
![scenario_overview](https://github.com/SAP/cloud-cf-casino/blob/master/img/scenario_overview.png)

The involved software components shown in the figure above are:

1.	**Emotion Service:** Service gets an image of a human face as input and returns the detected emotion (e.g. happy, sad, anger, surprise...). The service runs on HCP Cloud Foundry and is accessible via YaaS.

2.	**Casino Sentiment Service:** this is the service that will be developed during the hands-on session; it should track the sentiment of the gamblers for the slot machines of the monitored casinos over time, and provide methods to retrieve average values for each slot machine and aggregated values on casino level. The service should run on HCP Cloud Foundry and should be developed as a self-contained microservice.

3.	**Slot Machine Simulation App:** This is an HTML5 application that runs on HCP and is able to take image URLs and then call the Casino Sentiment Service with the provided image for test purposes. 

4.	**Casino Overview App:** This is an HTML5 application that runs on HCP and uses the Casino Sentiment Service to aggregate results of the collected sentiment data on the level of the single casinos.

5.	**Slot Machines Overview App:** This is an HTML5 application that runs on HCP and uses the Casino Sentiment Service to show results of the sentiment analysis on the level of a slot machine for a specific casino.

# Exercises

**Exercise 01: Getting an HCP developer account and cloning sample from Github** 

In this exercise, you will sign up for a free HCP developer account which you can use to deploy and run the HTML5 applications shown in Figure 1 above. You will also clone the Git repository that contains the complete sample project from github into your local environment: [exercise01](./exercises/exercise01) 

**Exercise 02: Deploy and run the applications**

In this exercise, you will deploy the prepared applications shown in Figure 1 above into your HCP account and see them running. The applications are the Slot Machine Simulation App, the Casino Overview app, and the Slot Machines Overview app. In their initially deployed version, they make use of a centrally deployed Casino Sentiment Service, i.e. they are fully operational: [exercise02](./exercises/exercise02)

**Exercise 03: Getting access to HCP Cloud Foundry services (beta)**

In this exercise, you will sign up for a beta account of the starter edition of the HCP Cloud Foundry services and start using Cloud Foundry: [exercise03](./exercises/exercise03)

**Exercise 04: Setup Casino Sentiment Service in your local environment**

In this exercise, you will set up the initial state of the Casino Sentiment Service project in your local Eclipse environment and deploy the application into your Cloud Foundry account: [exercise04](./exercises/exercise04)

**Exercise 05: Develop the Casino Sentiment Service REST API**

In this exercise, you will develop and complement the REST API of the Casino Sentiment Service in Java locally using the Spring framework and then deploy & run it again in your Cloud Foundry account: [exercise05](./exercises/exercise05)

**Exercise 06: Calling the Emotion Service exposed via YaaS**

In this exercise, you will extend the Casino Sentiment Service to call the Emotion Service exposed via YaaS: [exercise06](./exercises/exercise06)

**Exercise 07: using PostgreSQL service to track sentiment values**

In this exercise, you will use the PostgreSQL backing service on Cloud Foundry to track emotion results in a relational database. After this exercise, the service is functionally complete, i.e. you will be able to test the full REST API: [exercise07](./exercises/exercise07)

**Exercise 08: Make service resilient by using Netflix Hystrix**

In this exercise, we improve the Casino Sentiment Service by using Hystrix Circuit Breaker to make the service more resilient: [exercise08](./exercises/exercise08)

**Exercise 09: Change Slot Machine Simulation App to use “own” casino sentiment service**

In this exercise, you will change the Slot Machine Simulation Application that you have deployed into your HCP account in exercise 02 to use your own Casino Sentiment Service: [exercise09](./exercises/exercise09)
