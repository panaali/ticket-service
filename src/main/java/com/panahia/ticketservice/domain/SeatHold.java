package com.panahia.ticketservice.domain;

import com.panahia.ticketservice.Config;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Ali Panahi on 5/9/18.
 */
public class SeatHold {

    private int seatHoldId;

    private int numSeats;

    private String customerEmail;

    private Set<Seat> holdSeats;

    private long expirationTime;

    /**
     * Unique Id for each seatHold object
     */
    private static AtomicInteger uid = new AtomicInteger();

    private Status status;

    private String confirmationCode;

    public SeatHold(String customerEmail, Set<Seat> holdSeats){
        Config config = Config.getInstance();
        this.numSeats = holdSeats.size();
        this.customerEmail = customerEmail;
        this.holdSeats = holdSeats;
        this.expirationTime = System.currentTimeMillis() + config.getHoldSeatLifeTime() * 1000;
        this.seatHoldId = uid.getAndIncrement();
    }

    public int getSeatHoldId() {
        return seatHoldId;
    }

    public void setSeatHoldId(int seatHoldId) {
        this.seatHoldId = seatHoldId;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Set<Seat> getHoldSeats() {
        return holdSeats;
    }

    public void setHoldSeats(Set<Seat> holdSeats) {
        this.holdSeats = holdSeats;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > getExpirationTime();
    }

    public boolean isReserved() {
        return status == Status.RESERVED;
    }

    public boolean isHold(){
        return status == Status.HOLD;
    }

    public boolean isAvailable(){
        return status == Status.AVAILABLE || (isHold() && isExpired());
    }


}
