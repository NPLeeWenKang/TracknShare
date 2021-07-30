# 1. About TracknShare 

### Google Maps Navigation
TracknShare is a running app that tracks the users running activity. Users can also share their runs via the inbuilt social media feature.
Google Maps is integrated to allow users to track their running routes after every run.
**NOTE**:User's current location and end location will be tracked only during the duration of the run.

### Statistics
Following each run, runners will be able to view their run statistics.
Run statistics consists of :
  - Steps taken
  - Run pace
  - Calories burned
  - Run duration
  There is a dashboard in the profile tab where the above-mentioned statistics are computed by the means of **total** and **average**
  
 ### Social Media
 Similar to other social media functionalities,we have features which allows users to post their runs in the app. Such posts will be consolidated as the general feed
 for runners using this common platform.
 Furthermore, runners can befriend others and view their run profile to learn more about them.
 Hence,the feed is categorised to FRIENDS and GENERAL where the posts are filtered accordingly.
 Our app allows runners to share posts beyond this platform to external apps i.e Instagram, WhatsApp, Gmail etc.
 
# 2. Contributors
1. **Name**: Mahshukurrahman |
   **Student ID**: S10204884H
2. **Name**: Alan Antony James |
   **Student ID**: S10207327J
  
# 3. Roles & Contributions
 
 ## Front-End Developer
 **Developer** : Mahshukurrahman
 
 **Contributions**:
  - Drafted the [TracknShare App Icon](https://github.com/NPLeeWenKang/TracknShare/blob/master/app/src/main/ic_launcher-playstore.png) with [Adobe Illustrator](https://www.adobe.com/sg/products/illustrator.html)
  - Implemented some of the Model Entity classes like Posts.java,User.java and Run.java
  - Drafted storyboards for the App UI
  - Implemented the viewholders and recycler views for Social Media posts feed, Statistics Dashboard
  - ViewPager2 and Tab Layouts for Posts and Runner Profile 
  - [MPAndroidCharts Library for Bargraphs](https://github.com/PhilJay/MPAndroidChart)
  - Implemented [Sharesheet API](https://developer.android.com/training/sharing/send)
  
 ## Google-Maps and Fitness Data
 **Developer**: Alan Antony James

 **Contributions** :
  - Implemented the Google Maps using Google Maps SDK for Android.
  - Figured out how to calculate data such as Calories, Steps, Total time during running (Timer) and Distance travelled as well as the speed / pace of each run and implemented it
  - The calculation of steps was calculated using the [Step Sensor](https://developer.android.com/reference/android/hardware/Sensor#TYPE_STEP_COUNTER) in Android
  - Implemented the [Foreground Service](https://developer.android.com/guide/components/foreground-services) to make sure that the tracking of location and data is going on in the background even when the app is destroyed.
  
  # 4. Appendices
  
  ![TracknShare App Icon](/app/src/main/ic_launcher-playstore.png)
  
 **TracknShare App Icon**
 
![Statistics Dashboard.mp4](https://user-images.githubusercontent.com/73008987/127649810-6622caf4-93a5-4a89-ba2c-a84a99d8fed5.mp4)


 **Bargraphs** , **Statistics Dashboard**
 
 ![profile](https://user-images.githubusercontent.com/73008987/127648035-076f56c1-2f50-4c8b-8ec5-9e2da99795c4.png)
 
 **ViewPager2 - Profile**
 
 ![sharesheet](https://user-images.githubusercontent.com/73008987/127647675-9eea376c-a22b-4f4d-9c2a-b0ef18c00af3.png)

 **Sharesheet API**

 **[Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk/overview)**

 **[Flaticons vector graphics**](https://www.flaticon.com/)
