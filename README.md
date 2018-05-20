# HRM
Heart Rate Monitor PSM/Mobile Project

# What is the project is all about?
This project is designed to allow android user to monitor their heartbeat. 
Users can sign up their details and log in to this application. 
Also this application allow user to keep track their heartbeat rate history .

# Module :
1.	Registration Module.
User needs to register first before use the application. The registration require username, password, contact no, address and emergency contact number.

2.	Login Module.
User can login by entering a registered username and password. Doctor can access by using authorized id and password.

3.	Check Heartbeat Module.
User can check their heartbeat rate and will display the heartbeat rate.

4.	View History Module.
User can view their heartbeat history on this application. Doctor can view the user’s detail.

5.	Manage User Information
User can view and edit their user profile information in this module.

6.	 Location Module
If the user heartbeat is abnormal, the application will automatically ask the user 
whether they want to send their current location to their emergency contact.

# What libraries/external API's is used:
The external API's used was Google Maps API for getting longitude latitudes, Firebase API for connection to database, and Messaging API for sending messages to other people through SMS.


# How to setup the development environment:
Inside App Gradle:
      dependencies 
      {
          compile fileTree(dir: 'libs', include: ['*.jar'])
          androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', 
          {
              exclude group: 'com.android.support', module: 'support-annotations'
          })
          compile 'com.squareup.picasso:picasso:2.71828'
          compile 'com.getkeepsafe.taptargetview:taptargetview:1.11.0'
          compile 'com.android.support.constraint:constraint-layout:1.0.2'
          compile 'com.android.support:cardview-v7:23.3.+'
          compile 'com.firebaseui:firebase-ui-database:1.2.0'
          compile 'com.google.firebase:firebase-core:11.4.2'
          compile 'com.google.firebase:firebase-database:11.4.2'
          compile 'com.android.support:appcompat-v7:26.+'
          compile 'com.android.support.constraint:constraint-layout:1.0.2'
          compile 'com.android.support:design:26.+'
          compile 'com.google.firebase:firebase-storage:11.4.2'
          compile 'com.google.android.gms:play-services:11.4.2'
          testCompile 'junit:junit:4.12'
      }
