# Contract: adminCreateOffering

## Operation:
`adminCreateOffering(String lesson, int locationId, List<String> timeSlots, int capacity, String startDate, String endDate)`

## Cross Reference:
System Sequence Diagram: Offering Creation

## Preconditions:
- The `currentUser` is logged in and authenticated.
- The `currentUser` is of type `Admin`
- The `locationId` corresponds to an existing `Location` in the `LocationRecords`.  
- The `startDate` and `endDate` are in the format `yyyy-MM-dd`.  
- The `endDate` is not before the `startDate`.  
- The `timeSlots` list contains valid entries in the format `DAY,HH:mm,HH:mm`, where `DAY` is a valid day of the week.  
- The `capacity` is a positive integer.   
- There is no other `Offering` at the same `locationId` with overlapping `timeSlots` during the specified date range.  

## Postconditions: 
- A new `Schedule` object is created with the given start and end dates, and the `timeSlots`.  
- The `Schedule` is saved to the database, and its `ID` is retrieved and assigned to the `Schedule` object.  
- A new `Offering` object is created with the provided details (`lesson`, `location`, `schedule`, `capacity`).  
- The `Offering` is added to the `offeringRecords`.  
- A confirmation message is displayed indicating successful creation and addition to the database.  
- If any validation fails or an error occurs, an appropriate error message is displayed, and the `Offering` is not created.  
