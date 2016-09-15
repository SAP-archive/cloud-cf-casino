# Exercise 02: Deploy and Run the Applications

##Estimated time
15 minutes

##Objective

Deploy the following three HTML5 applications on your free developer account on the SAP HANA Cloud Platform:

  * **Slot Machine Simulation App**: This application allows you to enter URLs to images of human faces which can then be sent to the Casino Sentiment Service for test purposes to determine the emotion in the faces.
  * **Casino Overview App** (optional step): This application uses the Casino Sentiment Service to aggregate results of the sentiment analysis on the level of the single casinos.
  * **Slot Machines Overview App** (optional step): This application uses the Casino Sentiment Service to show results of the sentiment analysis on the level of the single slot machines of a specific casino.

#Exercise description

## 1. Import the destination to the sentiment service

1. Open your free developer account in the browser via the link [https://account.hanatrial.ondemand.com/cockpit](https://account.hanatrial.ondemand.com/cockpit) so that you get to the HCP cockpit - the landing page for your HCP account.
<br><br>
![Account landing page](../../img/img02_1_001.png?raw=true)
<br><br>
2. Click on the tab ```Destinations``` on the left hand side navigation pane.
<br><br>
![Select destination](../../img/img02_1_002.png?raw=true)
<br><br>
3. Click on the link ```Import Destination```, select the file ```./cloud-cf-casino/html5apps/destination_sentimentservice.txt``` in  your **student directory folder** and click on the ```Save``` button.
<br><br>
![Import Destination](../../img/img02_1_003.png?raw=true)
<br><br>
4. Once done you should see the destination in your account.
<br><br>
![Imported destination](../../img/img02_1_004.png?raw=true)
<br><br>

## 2. Import the HTML5 apps to your HCP account

### 2.1 Import Slot Machine Simulation App

1. In the cockpit click on the ```Services``` entry on the navigation pane and scroll down in the list of services until you see the tile called ```SAP Web IDE```. Click on that tile.
<br><br>
![Account landing page](../../img/img02_2_001.png?raw=true)
<br><br>
2. If the SAP Web IDE is not enabled yet on your account, please click on the ```activate```button. If it is already enabled click on the link ```Open SAP Web IDE```.
<br><br>
![Account landing page](../../img/img02_2_002.png?raw=true)
<br><br>
3. In case you get a welcome message just close it by clicking on the ```Close```button.
<br><br>
![Account landing page](../../img/img02_2_003.png?raw=true)
<br><br>
4. Now right-click on the Workspace folder and select the commands ```Import``` > ```From File System```.
<br><br>
![Account landing page](../../img/img02_2_004.png?raw=true)
<br><br>
5. Click on the ```Browse``` button and select the file ```./cloud-cf-casino/html5apps/slotmachinesim.zip``` from your student directory folder. This is the **Slot Machine Simulation App**. Click on the ```OK``` button to finalize the import.
<br><br>
![Account landing page](../../img/img02_2_005.png?raw=true)
<br><br>
6. If this worked out fine you should see the app folder ```slotmachinesim``` under the ```Workspace``` folder:
<br><br>
![Account landing page](../../img/img02_2_006.png?raw=true)
<br><br>

### 2.2 Optional: Import Slot Machine Overview App

This application is not further used in the exercises of this tutorial - that's why deploying this application is optional.

7. Right-click on the Workspace folder again and select the commands ```Import``` > ```From File System```.
<br><br>
![Account landing page](../../img/img02_2_007.png?raw=true)
<br><br>
8. Click on the ```Browse``` button and select the file ```.cloud-cf-casino/html5apps/slotmachineoverview.zip``` from your student directory folder. This is the **Slot Machine Overview App**. Click on the ```OK``` button to finalize the import.
<br><br>
![Account landing page](../../img/img02_2_008.png?raw=true)
<br><br>

### 2.3 Optional: Import Casino Machine Overview App

This application is not further used in the exercises of this tutorial - that's why deploying this application is optional.

9. For importing the 3rd app right-click on the Workspace folder again and select the commands ```Import``` > ```From File System```.   
<br><br>
![Account landing page](../../img/img02_2_007.png?raw=true)
<br><br>
10. Click on the ```Browse``` button and select the file ```./cloud-cf-casino/html5apps/casinooverview.zip``` from your student directory folder. This is the **Casino Overview App**. Click on the ```OK``` button to finalize the import.   
<br><br>
![Account landing page](../../img/img02_2_009.png?raw=true)
<br><br>
11. You should see now all the 3 applications in your Workspace folder.   
<br><br>
![Account landing page](../../img/img02_2_010.png?raw=true)
<br><br>

## 3. Deploy and test: Slot Machine Simulation App
1. Expand the folder ```slotmachinesim``` and double-click on the file ```index.html``` so that the file opens up in the editor.
<br><br>
![Account landing page](../../img/img02_3_001.png?raw=true)
<br><br>
2. In the ```index.html``` file adapt the variable ```idCasino``` and  ```idSlotmachine``` to the values provided by your session instructor. Please don't rename the variable itself, but just change the values in " ... ".
<br><br>
![Account landing page](../../img/img02_3_002.png?raw=true)
<br><br>
3. Save the changed file by clicking on the ```Save```button.  
<br><br>
![Account landing page](../../img/img02_3_003.png?raw=true)
<br><br>
4. To deploy the application to your account you need to right-click on the ```slotmachinesim``` folder and select the commands ```Deploy``` > ```Deploy to SAP HANA Cloud Platform```.
<br><br>
![Account landing page](../../img/img02_3_004.png?raw=true)
<br><br>
5. Enter your credentials and click on ```Login```.   
<br><br>
![Account landing page](../../img/img02_3_005.png?raw=true)
<br><br>
6. Finally click on ```Deploy```.   
<br><br>
![Account landing page](../../img/img02_3_006.png?raw=true)
<br><br>
7. If all worked out well you should see the message below about the successful deployment of your app. Now you can click on the link ```Open the active version of the application``` .
<br><br>
![Account landing page](../../img/img02_3_007.png?raw=true)
<br><br>
8. When the page opens up, the Slot Machine Simulator application allows you to specify an URL to an image for which you like the sentiment detection to be applied. The image should contain a human face. Example URLs that you can copy and paste into the input box of the application:
<br>
   [example 1](http://65.media.tumblr.com/tumblr_lbrz5ep3Ze1qbih58.jpg)
<br>
   [example 2](http://richardbattista.org/wp-content/uploads/richardbattista-org/2013/03/smile-300x256.jpg)
<br>
   [example 3](http://www.abc.net.au/reslib/201502/r1385324_19654588.jpg)
<br><br>
![Account landing page](../../img/img02_3_008.png?raw=true)
<br><br>
9. The images of the Slot Machine Simulation application get sent to a centrally deployed Casino Sentiment Service. You can now play a bit with the Slot Machine Simulation application and send some images to the service. You will get back the detected emotion on the face provided.
<br><br>
![Account landing page](../../img/img02_3_009.png?raw=true)
<br><br>

## 4. Deploy and test: Casino Overview App
1. To deploy the Casino Overview app to your account, you need to right-click on the ```casinooverview``` folder and select the commands ```Deploy``` > ```Deploy to SAP HANA Cloud Platform```.   
<br><br>
![Account landing page](../../img/img02_4_001.png?raw=true)
<br><br>
2. Now click on ```Deploy```.    
<br><br>
![Account landing page](../../img/img02_4_002.png?raw=true)
<br><br>
3. If all worked out well you should see the message below about the successful deployment of your app. Now you can click on the link ```Open the active version of the application```.
<br><br>
![Account landing page](../../img/img02_3_007.png?raw=true)
<br><br>
9. Now you see the summary of all sentiments for all the Casinos that have been created so far   
<br><br>
![Account landing page](../../img/img02_4_003.png?raw=true)
<br><br>

## 5. Deploy and test: Slot Machine Overview App
1. Expand the folder ```slotmachineoverview``` and the subfolder ```i18n```. Then double-click on the file ```i18n.properties``` so that the file opens up in the editor. In the ```i18n.properties``` file adapt the variable ```casinoid``` and to the value provided by your session instructor. Please don't rename the variable itself.   
<br><br>
![Account landing page](../../img/img02_5_001.png?raw=true)
<br><br>
2. To deploy the app to your account you need to right-click on the ```slotmachineoverview``` folder and select the commands ```Deploy``` > ```Deploy to SAP HANA Cloud Platform```. Now click on ```Deploy```.   
<br><br>
![Account landing page](../../img/img02_5_002.png?raw=true)
<br><br>
3. If all worked out well you should see the message below about the successful deployment of your app. Now you can click on the link ```Open the active version of the application```.  
<br><br>
![Account landing page](../../img/img02_5_003.png?raw=true)
<br><br>
9. Now you see a summary for the sentiment of all slot machines for the specified casino.   
<br><br>
![Account landing page](../../img/img02_5_004.png?raw=true)
<br><br>

##Summary

In this exercise you have created a destination to the centrally deployed Casino Sentiment Service, and then imported and deployed the three sample applications which make use of this service into your HCP account. All three applications should now be running and you have an understanding of the sample scenario used in this tutorial. In the next steps, you will develop the Casino Sentiment Service locally, deploy it into your HCP account and then use the deployed sample applications for testing against your own Casino Sentiment Service. Continue with [exercise03](../exercise03).
