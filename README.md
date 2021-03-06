ApScheduler 
=========
Jdk 1.6

Introduction
------------
This is an open source scheduling system, I will be very happy if it is useful for others. This system is for scheduling operation task program automatically, actually aim for easy and simple usage but with capabilities to schedule heavy work at once and related to each other.

The purpose of system use is to run unix scripting operations, batches and so on. This system is designed using the Java programming language. I only tried Windows 10 but, because of the development environment in this OS. If this system is stable I will try to adapt it to the Unix system. I hope this will continue.

Concept
-------
![alt text](https://raw.githubusercontent.com/aseven7/ap-scheduler/master/docs/img/concept.png "Concept flow")

Features
--------
Support scheduling functions and features :
1. ap.xml used as preference schedule of task.
2. ap.xml in-time editable and auto reloading schedule on live time.
3. Support chaining process, in terms call next job process after current job finish.

TODO
----
1. Provide Unit Test
2. User Interface for possible schedule
3. New parameter for job *Parameter* such like, parameter passing and communication between one job and another job task.
4. New parameter for job *Condition* such like, condition watching file change.

Compatibility
-------------
This function is provide WITHOUT Warranty, use your own risk. Basically, this function still on progress made for personally backup itself. I writing using eclipse IDE (Oxygen) Windows 10. Never test in Unix system. But I also target this function to work in UNIX (after I started Unit Test).

Usage
-----
Test with Install project on eclipse, configuration config/ap.properties :
1. Locating *config/ap.xml.example* to *E:/ap.xml*
2. Set properties *jobpath* to *E:/ap.xml*
3. Running ApMain class
4. Try change ap.xml time with your actual time for see the job working:
```xml
	<id>JB00A1</id>
	<groupid>JB00A</groupid>
	....
	<time>13:57</time>
	<day></day>
```
5. Change *command* with batch script target :
```xml
	<command>cmd /c start E:\\script\\JB00A1.bat</command>
	<presentCommand>job:JB00A2</presentCommand>
```
6. Setup HttpClient for access schedule and logs in user friendly format (default : http://localhost:8080).

Actually, No Difference between *command* and *presentCommand*. but *presentCommand* can use for call another job task after *command* finish executed. To call another job simply use syntax :
*job:JOBID*