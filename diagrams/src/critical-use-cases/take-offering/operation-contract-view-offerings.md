# Contract: viewInstructorAvailableOfferings

## Operation:
`viewInstructorAvailableOfferings(): int`

## Cross Reference:
System Sequence Diagram: Instructor Taking an Offering

## Preconditions:

-- The `currentUser` is logged in and authenticated.
-- The `currentUser` is of type `Instructor`
-- There is at least one `Offering` in `offeringRecords`.
-- The `Instructor` has at least one valid `City` in their available locations.
-- The `Instructor` has at least one valid `Specialization` associated with them.


## Postconditions:
-- A filtered list of `Offering` instances is created:  
   - The `Offering` is unassigned (i.e., `offering.getInstructor() == null`).  
   - The `Offering` location matches one of the `Instructor`'s available cities.  
   - The `Offering` lesson matches at least one of the `Instructor`'s specializations.  
-- The filtered list of `Offering` instances is displayed to the `Instructor`.  
-- The method returns the number of displayed offerings.  
-- If the `currentUser` is not an `Instructor`, an `IllegalStateException` is thrown.  
