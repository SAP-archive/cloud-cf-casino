# Exercise 04: Setup Casino Sentiment Service in your Local Environment

##Estimated time

7 min

##Objective

In this exercise you'll set up the initial project of the Casino Sentiment Service in Eclipse, build the project locally and then deploy it into your Cloud Foundry account.

#Exercise description

In [exercise01](../exercise01/README.md) you have already cloned the git repository of the sample project to your local computer. Now import the casino-sentiment-service project into your local Eclipse, build the project, and deploy it to your Cloud Foundry account.   

## 1. Import casino-sentiment-service project into Eclipse

1. Open the Windows Start menu and enter ```DEV262``` in the input field. Under ```Programs``` you will see ```ABAP in Eclipse - DEV62```. Click on this entry to open Eclipse.
<br><br>
![Eclipse in Windows Start menu](../../img/img04_01.png?raw=true)
<br><br>

2. Now import the initial state of the Casino Sentiment Service project as Maven project into your Eclipse workspace: In the Eclipse menu, chose ```File```> ```Import...```.
<br><br>
![File Import](../../img/img04_02.png?raw=true)
<br><br>

3. In the ```Import``` wizard, select to ```Maven``` > ```Existing Maven Projects``` and click ```Next```.
<br><br>
![Import existing project](../../img/img04_03.png?raw=true)
<br><br>

4. In the next step of the ```Import Maven Projects``` popup, click ```Browse```, navigate into the ```casino-sentiment-service-initial``` project in your student directory folder (```D:\Files\Session\DEV262```), then click ```Finish```.
<br><br>
![Import existing project](../../img/img04_04.png?raw=true)
<br><br>

5. The project is now imported in Eclipse. You should see the project in the Project Explorer like in the screenshot below.   
<br><br>
![Project in Eclipse](../../img/img04_05.png?raw=true)
<br><br>

## 2. Build the project in Eclipse using Maven  

6. Now build the project in Eclipse: Select the project in the Project Explorer, open its context menu and click on ```Run As``` > ```Maven build...```.
<br><br>
![Maven build](../../img/img04_06.png?raw=true)
<br><br>

7. In the ```Edit Configuration```popup, enter ```clean install``` into the ```Goals```field and click ```Run```. The Maven build should finish without errors.
<br><br>
![Maven build](../../img/img04_07.png?raw=true)
<br><br>

## 3. Deploy the project to Cloud Foundry  

8. Now open the manifest.yml file in the root directory of the casino-sentiment-service project, and change the ```name``` property to a unique value: ```sentiment-service-<number>``` where ```<number>``` should be the number of your working place. Save the change by pressing  ```Ctrl+S```.
<br><br>
![manifest.yml](../../img/img04_08.png?raw=true)
<br><br>

9. Next, deploy the service to your Cloud Foundry space. For this go again into your command terminal (in case you closed it: see [exercise03](../exercise03) for how to open the terminal and how to connect to your Cloud Foundry org/space). In the terminal, navigate into the casino-sentiment-service project directory (D:\Files\Session\DEV262\cloud-cf-casino\casino-sentiment-service-initial). To start the deployment,  enter ```cf push```. The deployment of the casino-sentiment-service will take 1 or 2 minutes and should finish successfully.

10. Once the project has been deployed, you can run the service in a browser with following URL (replace ```<number>``` by the number of your workplace): ```https://sentiment-service-<number>.cfapps.us10.hana.ondemand.com```. You should now see a documentation page that lists the to-be-developed REST API of the casino-sentiement-service.
<br><br>
![Service REST API](../../img/img04_09.png?raw=true)
<br><br>

##Summary

In this exercise you have set up the initial project of the Casino Sentiment Service in Eclipse, built it and deployed it into your HCP Cloud Foundry account. You are now ready to add the REST API of the service in [exercise05](../exercise05).
