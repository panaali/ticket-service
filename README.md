
# Ticket Service Coding Challenge
Ticket Service was a small project that I did in a few days as a coding challenge for an interview that I had.

## Contents

- [Problem Background](#problem-background)
- [Problem](#problem)
- [Solution](#solution)
    - [Requriements](#requriements)
    - [How to build](#how-to-build)
    - [How to run tests](#how-to-run-tests)
    - [Solution Description](#solution-description)
- [License](#license)
---

## Problem Background

Implement a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.
For example, see the seating arrangement below.

        ----------[[  STAGE  ]]----------
        ---------------------------------
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss


## Problem

Your homework assignment is to design and write a Ticket Service that provides the following functions:
- Find the number of seats available within the venue
Note: available seats are seats that are neither held nor reserved.
- Find and hold the best available seats on behalf of a customer
Note: each ticket hold should expire within a set number of seconds.
- Reserve and commit a specific group of held seats for a customer
### Requirements
- The ticket service implementation should be written in Java
- The solution and tests should build and execute entirely via the command line using either Maven or Gradle as the build tool
- A README file should be included in your submission that documents your assumptions and includes instructions for building the solution and executing the tests
- Implementation mechanisms such as disk-based storage, a REST API, and a front-end GUI are not required

Your solution will be reviewed by engineers that you will be working with if you join the Walmart Technology team. We are interested in seeing how you design, code, and test software.

You will need to implement the following interface. The design of the SeatHold object is entirely up to you.

    public interface TicketService {

    /**
    * The number of seats in the venue that are neither held nor reserved
    *
    * @return the number of tickets available in the venue
    */

     int numSeatsAvailable();

    /**
    * Find and hold the best available seats for a customer
    *
    * @param numSeats the number of seats to find and hold
    * @param customerEmail unique identifier for the customer
    * @return a SeatHold object identifying the specific seats and related
    information
    */

     SeatHold findAndHoldSeats(int numSeats, String customerEmail); /**

    * Commit seats held for a specific customer
    *
    * @param seatHoldId the seat hold identifier
    * @param customerEmail the email address of the customer to which the
    seat hold is assigned

    * @return a reservation confirmation code
    */ String reserveSeats(int seatHoldId, String customerEmail);

    }

## Solution

### Requriements
- Java SE Development Kit 8
- Apache Maven 3.5.0 or later

### How to build
    $ mvn package

### How to run tests
    $ mvn test

### Solution Description
The program coded in Java and uses an array for seats and a HashMap for storing the seatHolds with their Id. The following assumptions were made:
- The service manages one venue
- For selecting best seats it was assumed that, the seats near the stage have higher priority and after that the seats in the middle of the column have higher priority.
- Each SeatHold has a seatHoldId which is an integer that is incremented in every new SeatHold object.
- The confirmation code is a string of random characters.
- For preventing inconsistencies when using multithreading, `findAndHoldSeats` and `reserveSeats` methods considered `synchronized`.

Configuartion about venue and ticket expiration should be entered in the `./src/main/resources/config.properties` file.

## License
This work released under **Apache License 2.0**.
