# Kid

# HITAM5PSI
https://github.com/meiliverani/kiddo_app

Java Based Android Kid Scheduler Application - UAS Sistem Rekayasa 5PSI - Kelompok Hitam
Made using Android Studio

# APPLICATION LOGO
![alt text](https://5psihitamsr.000webhostapp.com/logo.png)

1. Licen 1831076
2. Meiliverani Erline 1831126
3. Melvy Devalia 1831158
4. Jodi Saputra Dermawan Saragih 1831119

# DATABASE ANALYSIS
This application is using 1 database named "db_kiddo" with 6 tables within.

Each table has 1 primary key which is ID or username. 
The diagram of the db's schema is drawn as below:

![alt text](https://5psihitamsr.000webhostapp.com/db-schema.png)

and exported .sql file as below:

![alt text](https://5psihitamsr.000webhostapp.com/db_kiddo.sql)


# FEATURES ANALYSIS
This application uses some features as below:
1. Async Task, to handle processes from and to database // used on MainActivity.java, SubActivity.java, Gift.java, and other Java Activity class
2. Broadcast receivers, to listen notification receiver // used on AlertReceiver.java
3. AlarmManager, to set Alarm service // used on AddSubActivity.java in startAlarm() function
4. Shared Preference, to save logged in user session and information // used almost on all Java Activity Class that needs logged in user's information
5. ViewModel, to show list in RecyclerView // used on MainActivity.java, SubActivity.java, Gift.java

# LOGIN PAGE
![alt text](https://5psihitamsr.000webhostapp.com/login.png)

# LOGOUT MODAL WITH ACKNOWLEDGEMENT
![alt text](https://5psihitamsr.000webhostapp.com/logout.png)

# PROCESS 1 - SET ALARM WITH DAY REPETITION
Parent can set alarm by adding new sub activity and choosing the day repition by checking checkboxs
![alt text](https://5psihitamsr.000webhostapp.com/set-alarm.png)
![alt text](https://5psihitamsr.000webhostapp.com/alarm.png)

# PROCESS 2 - REDEEM STARS FROM FINISHED ACTIVITY
Application will filter out all activity that should be already passed (the time < now) so kid can redeem the stars
![alt text](https://5psihitamsr.000webhostapp.com/redeem-stars.png)

# PROCESS 3 - REDEEM GIFT FROM COLLECTED ACTIVITY
Application will filter out all not redeemed yet gift or all gift that able to be redeemed (required stars < kid's stars) so kid can redeem the stars
![alt text](https://5psihitamsr.000webhostapp.com/redeem-gift.png)

# HOSTING SITE
https://5psihitamsr.000webhostapp.com/

# METHOD
PHP is implemented in this application using GET and POST method to integrate between Java and MySQL


