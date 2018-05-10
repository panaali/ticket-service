package com.panahia.walmartlabs.venue.service;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the methods of the interface TicketService.
 * Created by aliakbarpanahi on 5/9/18.
 */
public class DefaultTicketService implements TicketService{

    private List<List<Character>> venueSeats = new ArrayList<List<Character>>();

    public DefaultTicketService() {

    }

    /**
     * The number of seats in the venue that are neither held nor reserved
     *
     * @return the number of tickets available in the venue
     */
    public int numSeatsAvailable() {
        return 0;
    }

    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats      the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related
     * information
     */
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        List<Character> seats = findSeats(numSeats);
        holdSeats();
        return null;
    }

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the
     *                      seat hold is assigned
     * @return a reservation confirmation code
     */
    public String reserveSeats(int seatHoldId, String customerEmail) {
        return null;
    }

    private List<Character> findSeats(int numSeats) {
        List<Character> seats = new ArrayList<Character>();
        return seats;
    }

    private int holdSeats(){
        //        seatHoldId = lastSeatHoldId++;
//        List<Integer> seats =
//        seatHold = new SeatHold();
        return 0;
    }

    public void printVenue() {
        String stage = "----------[[  STAGE  ]]----------\n" +
                        "---------------------------------";
        String venueSeats = this.venueSeats.toString();
        String venue = stage + venueSeats;
        System.out.println(venue);
    }
}
