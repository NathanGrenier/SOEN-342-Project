# Operation Contracts for Use Case: Take Offering

## Contract CO1: viewInstructorAllOfferings

## Operation:
`viewInstructorAvailableOfferings(): int`

## Cross Reference:
System Sequence Diagram: Instructor Taking an Offering

## Preconditions:
- The `currentUser` is logged in and authenticated.
- The `currentUser` is of type `Instructor`
- There is at least one `Offering` in `offeringRecords`.
- The `Instructor` has at least one valid `City` in their available locations.
- The `Instructor` has at least one valid `Specialization` associated with them.

## Postconditions:
- A filtered list of `Offering` instances is created:
1. The `Offering` is unassigned (i.e., `offering.getInstructor() == null`).
2. The `Offering` location matches one of the `Instructor`'s available cities.
3. The `Offering` lesson matches at least one of the `Instructor`'s specializations.
- The filtered list of `Offering` instances is displayed to the `Instructor`.
- The method returns the number of displayed offerings.
- If the `currentUser` is not an `Instructor`, an `IllegalStateException` is thrown.

## Contract CO2: acceptInstructorLesson

## Operation:
`acceptInstructorLesson(offeringId: int)`

## Cross Reference:
System Sequence Diagram: Instructor Taking an Offering

## Preconditions:
- The `currentUser` is logged in and authenticated.
- The `currentUser` is of type `Instructor`
- The `Offering` with the specified `offeringId` exists.
- The `Offering` is not already assigned to an `Instructor`.
- The current user is an instance of `Instructor`.
- The `Offering` is available within the cities and specializations of the `Instructor`.

## Postconditions:
- The `offering.instructor` attribute is set to `this.instructorID` (the ID of the current `Instructor`).
- The `Offering` is removed from the list of offerings available to instructors.
- A confirmation message is displayed to the `Instructor`.
