package com.panahia.walmartlabs.venue.service;

import java.util.List;
import java.util.Random;

/**
 * Created by aliakbarpanahi on 5/9/18.
 */
public class SeatHold {
    private int seatHoldId;
    private int numSeats;
    private String email;

    private List<Integer> seats;

    public SeatHold(String email, List<Integer> seats){
        this.numSeats = seats.size();
        this.email = email;
        this.seats = seats;
    }

}
