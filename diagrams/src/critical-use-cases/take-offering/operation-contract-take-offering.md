# Contract CO2: acceptInstructorLesson

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
