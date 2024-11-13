**Contract CO2: acceptOffering**
**Operation:** acceptOffering(offering: Offering)
**Cross Reference:** System Sequence Diagram: Instructor Taking an Offering
**Preconditions:**
    -Offering exists
    -Offering isn't already assigned to an Instructor
**Postconditions:**
    -offering.instructor was set to this.instructorID