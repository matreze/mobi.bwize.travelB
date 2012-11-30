/* Group 2 
 * travelB - A user friendly travel planner
 * 
 * By:
 * Amabille Leal
 * Dominika Nowak
 * Alison Price
 * Michael Reid
 * 
 * Date: 30th November 2012
 * 
 * v 1.0
 * 
 */


Known Issues:
 
AddTrip.java
- Notifications are either on or off - no middle ground at present
- Radio groups for connections currently serve no purpose
 
CountDown.java
- Only works for departure flight, not the return flights
 
CurrencyConv.java
- Too many decimal points
 
Weather
 - no weather available for US (due to API requiring states as well as city)
 - Only in Metric - no farenheit (no preferences for this yet)
 
ViewDetails.java 
 - Changing the notifications here does not change any notifications
 - alarms are not cancelled when a trip is cancelled - would be cancelled if a trip is deleted and phone is then restarted
 
 DaschBoardActivity.java
 - only supports en wiki - may want to change if localised
 
 Description:
 
 The app was designed for use on flagship, small screen and high density devices. 
While the app displays all functionality on a tablet this device was not an immediate 
concern when building the layouts.  Every screen was designed to allow the user to 
carry out another action and or to return to the previous activity. 
All images used within the app were created and re-sized to accommodate the 
screen densities of the aforementioned phones and separate layout files were 
produced.  Additional imagery came from a royalty free image repository.
The splash screen contains the image of bee and the name of the application: 
TravelB ( recognizable logo of our application) as well as  cartoon sound that last 
around 3,5 seconds and lead to the main screen with the list of journeys.
The main features are all database driven, with a trip being stored there and 
subsequently accessed where required. The countries and cities lists are stored in a 
static database that is imported the first time the countries list is called. This 
significantly improves the user experience as there are no wait times while the list is 
loaded into the adaptors from memory.
The Activities are mostly fragment based to allow easier future layout changes 
should it be decided to introduce a tablet version. Fragments also lead to better 
modularisation of the code and functions.
After a trip has been added the user can select their trip from the triplist. This opens 
up the dashboard, which includes a countdown until the departure time, the current 
weather conditions at the destination, a convenient to-do list and links to a ten day 
weather forecast, a wikipedia page on the city and a currency converter.
Each item fetches displays or calculates information based on the trip id. 
For the weather, countdown and currency, this involves retrieving the destination info 
from the database. 
For the to-do list, the id allows the list to only show tasks related to the current trip.
All functions that access the internet are designed to cache the last values retrieved 
so as to avoid a situation where the user has no internet access (or is on holiday 
already) and not being able to use the functions.
The database of the journeys is based on SQLiteOpenHelper The class contains 
methods that can perform below indicated operations on the table in database:
- Upgrade SQL database;
- Open the database;
- Create new record;
- Delete selected record;
- Update record;
- Return the columns;
- Retrieve all trips and specific trip details based on trip ID;
- Retrieve specific column of the trip selected by ID.
The application  also has  notifications and alarm  features that  were designed to 
remind the user about important points of the trip.
The alarm is going to go off four hours before each flight departures. The user can't 
choose if they want an alarm or not, it's mandatory.
The alarm is also a notification, but it will make the phone flash lights, make sound 
and vibrate, while the other notifications will only make the phone vibrates.
For the notifications the user can decide if they will receive them or not.  The 
notifications will remind the user about important points of the trip like: packing bags, 
interesting items to put in the bag,  check-in,  accommodation, visa,  bag size,  etc.
These notifications will never be displayed during night time (after 22 and before 7 
hours).