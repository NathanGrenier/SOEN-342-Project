# TODO
- [x] UML Use Case Diagram
- [x] UML Domain Model
- [x] UML Class diagram
- [x] Relational data model
- [x] Package Diagram
- [x] 5 min video demonstrating entire system functionality.
## Critical Use Cases
Given a critical use case, we need to create these artifacts for **BOTH** the success and failure scenarios: 
- A System Sequence Diagram (SSD)
- An Interaction Diagram
- Operation Contracts

> If not too complex, then both success and failure scenarios can be captured by the same SSD (and consequently by the same interaction diagram during design).
### Offerings:
#### Create Offering
Create a Offering and make it visible to instructors.

An organization (through an administrator) would create a lesson. This is not made public yet as it does not (upon its construction) have an instructor.
#### Take Offering
When an instructor chooses to teach an offering, it must now be made visible to the public.
### Booking:
#### Create Booking
When a client signs up for a booking time slot.

A registered client can login into the system and sign up for an offering.
## Use Case 1: PROCESS OFFERINGS
### Requirements  
**Actors:** Administrator, Instructor

- Organization (through the Administrator) makes offerings available. 
- Instructors select lessons. 
- Public can view offerings.
### Artifacts
- [x] **System sequence diagram(s**) to capture **success** and **failure** scenarios. 
- [x] Identification of system operations and **operation contracts**. 
- [x] UML interaction diagrams.

## Use Case 2: PROCESS BOOKINGS
### Requirements
**Actors:** Client
### Artifacts 
- [x] System sequence diagram(s) to capture success and failure scenarios. 
- [x] Identification of system operations and operation contracts. 
- [x] UML interaction diagrams.

## FORMAL SPECIFICATIONS
- [x] OCL expressions

1. "Offerings are unique.  In other words, multiple offerings on the same day and time slot must be offered at a different location."
2. "Any client who is underage must necessarily be accompanied by an adult who acts as their guardian."
3. "The city associated with an offering must be one the city’s that the instructor has indicated in their availabilities."
4. "A client does not have multiple bookings on the same day and time slot."
   - (for simplicity we consider only identical day and time slots, even though in reality a booking on Monday 3pm – 4pm and another also on Monday 3:30pm – 4:30pm should not be acceptable.) 

# Miscellaneous
**Cancellations:** It is left up to you to implement such policy(-ies) as you see fit. For example, it would make sense to cancel a lesson if it has no instructor, but how you would cancel an offering is left to your judgment. For example, you may decide to cancel an offering only if there are no clients, or unconditionally (i.e. even in the presence of clients).

# Division of Labor
## Together
- Domain Model
- ERD
- Class Diagram

## Nathan
- Package Diagram
- **Use Case:** Create Booking
- OCL Specifications: 2, 4
### Code

## Nathanial
- Use Cases Diagram
- **Use Case:** Make Offering Available
- **Use Case:** Create Offering
- OCL Specifications: 1, 3
### Code
