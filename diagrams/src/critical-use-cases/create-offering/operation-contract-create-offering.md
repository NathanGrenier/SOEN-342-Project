**Contract CO2: createOffering**
**Operation:** createOffering(lesson: Lesson, schedule: Schedule, location: Location)
**Cross Reference:** System Sequence Diagram: Offering Creation
**Preconditions:**
-All other available offerings must either be at a different time or a different location
**Postconditions:**
-A Schedule instance s was created
-An Offering instance o was created
-o.location was set to location
-o.lesson was set to lesson
