ClassesSimulator
================
Intended for generating class schedules with friends. May not work as intended. Future updates will increase user-friendliness, simulation speed, and robustness in general.

Example settings.csv and classes.csv included. Generates output.csv when run. Settings are no longer hard-coded, but may or may not run out of the box.

#Instructions

##settings.csv
###filters (line 0)
Each column contains the classes that filter is asking for. A filter can represent a student or a shared group of classes that multiple students take. A simple filter which simply requests MAT100 can be made quite easily:

    MAT100

Special characters are & and |. To request both MAT100 and GLG100 in the filter, the field is constructed like so:

    MAT100&GLG100

Alternatively, to request either MAT100 or GLG100 (but not both), change the '&' to a '|'.

    MAT100|GLG100

At the moment, it is not possible to have filters like "(MAT100&GLG100)|EEE200". "MAT100|GLG100&EEE200|BIO100" would be read as (MAT100 or GLG100) and (EEE200 or BIO100). "MAT100&GLG100|EEE200&BIO100" would be read as MAT100 and (GLG100 or EEE200) and BIO100.

Alternatively, a class ID can be used in place of the class type. To generate a filter that only accepts either the class with the ID 10024 or the class with the ID 20553, use the following field:

    :10024|:20553

Of course, class IDs and class types can be mixed, like so:

    :10024|:20553|EEE200

A field on this line cannot be empty, or else no data will be generated.
###compareTo (line 1)
Indicates the filters that will be compared. Filter numbering starts at 0. If your filter 0 is "MAT100", your filter 1 is "EEE200", and your filter 2 is "GLG100", a compareTo value of "0&1" associated with filter 2 will effectively give you "MAT100&EEE200&GLG100".

On it's own, this is not useful, but classes can easily be shared between students using this method. By adding filter 3 with the value "BIO100" and compareTo value "0&1", you can generate two students who share MAT100 and EEE200, but separately take BIO100 and GLG100.

Similarly, filter 3 could have the value "GLG100", thus generating two students who share MAT100 and EEE200, but may take different GLG100 courses.
###order (line 2)
This is the order of filter generation. Do not be confused by the appearance of the .csv file. The value within the field is the filter number, and the filters are generated from the beginning of the array to the end (left-to-right).
###earliestTime (line 3)
Allows and denies certain classes from being included in filters by the earliest time the class meets. 0 is the earliest possible time.
###latestTime (line 4)
Allows and denies certain classes from being included in filters by the latest time the class meets. 2359 is the latest possible time.
###allowedCampuses (line 5)
Allows and denies certain classes from being included in filters by the campus location. & is a special character. This helps generate schedules for schools with multiple campuses and internet classes.
###display (line 6)
Determines if a filter is displayed. Helps to hide filters that do not represent students, but only represent classes. 0 represents false, and 1 represents true.


##classes.csv
From left to right: ban field, class id, class type, class title, instructor name, campus, credit hours, days, times.

Ban field is marked with an 'x' to indicate the class will not be considered.

Possible values for days: assorted combinations of m, t, w, th, f (Monday, Tuesday, Wednesday, Thursday, Friday, respectively) and o (online, times are ignored). Times are defined as 100*hour (24-hour clock variation) + minute. 1520 = 3:20 PM. "mw,900,1015" means Monday and Wednesday from 9 to 10:15, while "mw,900,1015,900,1200" means Monday from 9 to 10:15 and Wednesday from 9 to 12.

To represent a MAT100 (Calculus for Engineers I) class worth 3 credit hours taught by James on the North Campus from 9:00 AM to 10:15 AM, simply add the following line:

    ,10082,MAT100,Calculus for Engineers I,James,North,3,mw,900,1015

Note that the first comma is necessary, as that is the ban field. To ban this entry, simply add an 'x' like so:

    x,10082,MAT100,Calculus for Engineers I,James,North,3,mw,900,1015

If the class goes from 9:00 AM to 10:15 AM on Mondays, but goes from 9:00 AM to 12:00 PM on Wednesdays, then add the new times like so:

    ,10082,MAT100,Calculus for Engineers I,James,North,3,mw,900,1015,900,1200

##output.csv
Generally a mess at the moment. Each displayed filter begins with two fields, one with time-specific data and the other empty for display purposes:

    [credit hours]c [# days on campus]:[hours on campus]:[week days on campus],

The rest should be fairly straight-forward. Do not keep output.csv open while generating schedules, because Java will throw a fit. An empty schedule indicates one of many problems, including:

* No classes listed in a filter.
* No applicable classes found for a filter. Maybe the classes available are misnamed?
* Invalid class schedule takes up all day.
* No possible schedules.
* Be extra careful with commas. Be sure that all commas are removed from parsed data.

Please check the console for any possible errors.

#License
GNU General Public License v2. Just don't rip off this code and shove it in your university's class registration server without telling me, please.