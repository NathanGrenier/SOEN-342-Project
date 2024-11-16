# Operation Contracts for Use Case: Create Booking

## Three Types of Postconditions
1. Instance Creation and Deletion
2. Attribute Modification
3. Associations Formed or Broken

## 1. `getCurrentUserType()`

**Contract:** getCurrentUserType

**Operation:** `getCurrentUserType()`

**Cross reference(s):** Use Case Create Booking

**Preconditions:**  [Obligations of the client]
1. A terminal session is started.

**Postconditions:**  [Obligations of the supplier]
1. `Terminal`'s `userType` variable was set to the current user's type. **[modification of variable]**

## 2. `selectOperation()`

**Contract:** selectOperation

**Operation:** `selectOperation(int operationId)`

**Cross reference(s):** Use Case Create Booking

**Preconditions:**  [Obligations of the client]
1. Terminal session is started.
2. System is in a state to accept user input (from Terminal).

**Postconditions:**  [Obligations of the supplier]
1. Variable `operation` is set to the user specified operation id. **[modification of variable]**

## 3. `login()`

**Contract:** login

**Operation:** `login(String username, String password)`

**Cross reference(s):** Use Case Create Booking

**Preconditions:**  [Obligations of the client]
1. Terminal session is started.
2. System is in a state to accept user input (from Terminal).
3. The user is not already logged in.
4. If the user is logging in with an `Admin` account, another admin (or the same one) must not already be logged in.

**Postconditions:**  [Obligations of the supplier]
1. A new user instance is instantiate **[Instance Creation]**
2. The app sets `currentUser` to the newly created user instance **[Attribute Modification]**

## 4. `createBooking()`

**Contract:** createBooking

**Operation:** `createBooking(int bookingId)`

**Cross reference(s):** Use Case Create Booking

**Preconditions:**  [Obligations of the client]
1. Terminal session is started.
2. System is in a state to accept user input (from Terminal).
3. The user is logged in as a `Client`

**Postconditions:**  [Obligations of the supplier]
1. A new `Booking` object named `booking` is instantiated. **[Instance Creation]**
2. An associated between `Offering` and `Client` is created (Conceptually stored in a `Booking`). **[Associations Formed]**