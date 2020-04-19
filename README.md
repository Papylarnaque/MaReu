# Maréu
This is a Lamzon Android app, developed for internal uses, that allows to manage reunions and allocation of meeting rooms.

## General info
This project is led by the company Lamzon.

The [Framework paper](https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/GEN+android+P4/Note+de+cadrage+-+Mare%CC%81u.pdf "Framework paper") for this Android app requires the following implementations :

>* A list of reunions, including the hour, the room, the subject and the list of guests.
>* Functionality for adding new reunions.
>* Functionality for deleting reunions.
>* Filters by date and by room.
>* A responsive design, for phones and tables, in all orientations.
>* Android app supporting API21

## Tests
2 Unit tests :
- [x] add a reunion
- [x] delete a reunion

6 Instrumented tests :
- [x] recycler view not empty
- [x] add a reunion
- [x] check that an alert is displayed if we try to add a reunion with missing inputs
- [x] delete a reunion
- [x] filter the reunions list by date selection
- [x] filter the reunions list by room selection

## Screenshots of the application
<img src="/device-2020-04-19-143925.png " width="250"> <img src="/device-2020-04-19-144519.png " width="250"> <img src="/device-2020-04-19-144527.png " width="250">
<img src="/device-2020-04-19-144500.png " width="250">

## Why JAVA and not KOTLIN ?
Kotlin has many advantages, the code can be set up faster and in an easier way, partly because of its NULL values management.
However, JAVA has a community that is way more developed. For an application that has to serve the benefits of the Lamzone company internally, we can use that community to implement in an easier way (vs Kotlin) the functionnalities we want here. 
Additionally, JAVA could be easier to sustain vs Kotlin as we have way more employees knowing this code, as the ratio of the community using JAVA vs Kotlin.
Therefore, the choice has been made to take JAVA over Kotlin, but that will not avoid implementing new functionnalities under Kotlin if required one day, as Kotlin can work in classes in the same project than a .java classes.

## Status
Project is:  _finished_




