# Maréu
This is a Lamzon Android app, developed for internal uses, that allows to manage meetings and allocation of meeting rooms.

## General info
This project is led by the company Lamzon.

The [Framework paper](https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/GEN+android+P4/Note+de+cadrage+-+Mare%CC%81u.pdf "Framework paper") for this Android app requires the following implementations :

>* A list of meetings, including the hour, the room, the subject and the list of guests.
>* Functionality for adding new meetings.
>* Functionality for deleting meetings.
>* Filters by date and by room.
>* A responsive design, for phones and tables, in all orientations.
>* Android app supporting API21

## Tests
4 Unit tests :
- [x] add a meeting
- [x] delete a meeting
- [x] filter meetings by date
- [x] filter meetings by room

6 Instrumented tests :
- [x] recycler view not empty
- [x] add a meeting
- [x] check that an alert is displayed if we try to add a meeting with missing inputs
- [x] delete a meeting
- [x] filter the meetings list by date selection
- [x] filter the meetings list by room selection

## Screenshots of the application
<img src="/images_readme/main.png " width="250"> <img src="/images_readme/filters_menu.png " width="250"> <img src="/images_readme/rooms.png " width="250">
<img src="/images_readme/add.png " width="250">

## Why JAVA and not KOTLIN ?
Kotlin has many advantages, the code can be set up faster and in an easier way, partly because of its NULL values management.
However, JAVA has a community that is way more developed. For an application that has to serve the benefits of the Lamzone company internally, we can use that community to implement in an easier way (vs Kotlin) the functionnalities we want here. 
Additionally, JAVA could be easier to sustain vs Kotlin as we have way more employees knowing this code, as the ratio of the community using JAVA vs Kotlin.
Therefore, the choice has been made to take JAVA over Kotlin, but that will not avoid implementing new functionnalities under Kotlin if required one day, as Kotlin can work in classes in the same project than a .java classes.

## Status
Project is:  _finished_




