Custom Lockscreen Application. 

Add the project to Android Studio. 

First modify the credentials in app/src/main/python/login.csv

Then run the session.py there to generate the StringSession.json 


Lastly, build the APK: 

In Android Studio: Build - Generate Signed Bundle/APK 
Create a new key or load in a key 
Click on release
Signature Version: V2(Full APK Signature)

The APK can be found under app/release/ 
