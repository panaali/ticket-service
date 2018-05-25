package com.panahia.ticketservice.service;

import com.panahia.ticketservice.Config;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;


import com.panahia.ticketservice.domain.Seat;
import com.panahia.ticketservice.domain.SeatHold;
import com.panahia.ticketservice.domain.Status;
import com.panahia.ticketservice.domain.Venue;

import java.util.*;

/**
 * This class implements the methods of the interface TicketService.
 * Created by Ali Panahi on 5/9/18.
 */
public class DefaultTicketService implements TicketService{

    /**
     * The venue that this ticket service manages
     */
    private Venue venue;

    /**
     * A Hashmap for holding all SeatHold objects in a central place
     */
    private Map<Integer, SeatHold> seatHolds = new HashMap<>();

    public DefaultTicketService() {
        Config config = Config.getInstance();
        int rowCount = config.getVenueRowCount();
        int columnCount = config.getVenueColumnCount();
        venue = new Venue(rowCount, columnCount);
    }

    public DefaultTicketService(int rowCount, int columnCount) {
        venue = new Venue(rowCount, columnCount);
    }

    /**
     * The number of seats in the venue that are neither held nor reserved
     *
     * @return the number of tickets available in the venue
     */
    public int numSeatsAvailable() {
        return venue.getAvailableSeats();
    }

    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats      the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related
     * information
     */
    synchronized public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {

        if(!emailIsValid(customerEmail))
            throw new IllegalArgumentException("Not a valid email address");

        if(numSeats > numSeatsAvailable())
            throw new IllegalArgumentException("Number of seats requested is more than number of available seats");

        Set<Seat> seatsToHold = findBestAvailableSeats(numSeats);
        SeatHold seatHold = holdSeats(customerEmail, seatsToHold);
        return seatHold;
    }

    private boolean emailIsValid(String customerEmail) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(customerEmail);
    }

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the
     *                      seat hold is assigned
     * @return a reservation confirmation code
     */
    synchronized public String reserveSeats(int seatHoldId, String customerEmail) {
        Config config = Config.getInstance();
        SeatHold seatHold = seatHolds.get(seatHoldId);

        String confirmationCode;

        // if there's no matching SeatHold
        if (seatHold == null)
            throw new IllegalArgumentException("No seat held for this id");

        // if the SeatHold has not been reserved yet
        if(seatHold.isReserved())
            throw new IllegalArgumentException("This seat already reserved");

        // if the email usd to commit the SeatHold is different from the email used to hold seats
        if (!seatHold.getCustomerEmail().equals(customerEmail))
            throw new IllegalArgumentException("The email address to reserve ticket doesn't match the email address used to hold the seats");

        //if the seatHold has not been expired
        if (seatHold.isExpired())
            throw new IllegalArgumentException("Your hold for the seats have expired");

        seatHold.setStatus(Status.RESERVED);


        // Generating a random confirmation code
        confirmationCode = RandomStringUtils.random(config.getConfirmationCodeLength(), true, true).toUpperCase();

        seatHold.setConfirmationCode(confirmationCode);

        return confirmationCode;
    }

    /**
     * Find best available seats.
     * Best available seats is a very subjective term. Based on our common sense rows near to the stage are better and
     * columns in the middle have higher priority. In case of even column number, the two middle seats have the same
     * priority.
     *
     * @param numSeats
     * @return Set of best available seats
     */
    protected Set<Seat> findBestAvailableSeats(int numSeats) {
        Set<Seat> seatsToHold = new HashSet<Seat>();
        int venueRowsCount = venue.getSeats().length;
        int venueColumnsCount = venue.getSeats()[0].length;
        int columnMiddleFloor = (int) Math.floor((double) venueColumnsCount / 2);
        int columnMiddleCeil = (int) Math.ceil((double) venueColumnsCount / 2);

        outerLoop:
        for (int i = 0; i < venueRowsCount; i++) {
            for (int j = 0; j < columnMiddleCeil; j++) {
                int bestRowIndex = i;
                int bestColumnIndex1 = columnMiddleFloor + j;
                int bestColumnIndex2 = columnMiddleCeil - j - 1;

                Seat candidateSeat1 = venue.getSeats()[bestRowIndex][bestColumnIndex1];
                Seat candidateSeat2 = venue.getSeats()[bestRowIndex][bestColumnIndex2];

                if(seatsToHold.size() < numSeats && candidateSeat1.isAvailable()) {
                    seatsToHold.add(candidateSeat1);
                }
                if(seatsToHold.size() < numSeats && candidateSeat2.isAvailable()) {
                    seatsToHold.add(candidateSeat2);
                }
                if(seatsToHold.size() == numSeats)
                    break outerLoop;
            }
        }
        return seatsToHold;
    }

    /**
     * Hold seats
     *
     * @param customerEmail the email address of the customer to which the
    seat hold is assigned
     * @param seatsToHold Set of seats to be hold
     * @return a seatHold object
     */
    protected SeatHold holdSeats(String customerEmail, Set<Seat> seatsToHold){
        SeatHold seatHold = new SeatHold(customerEmail, seatsToHold);
        seatHolds.put(seatHold.getSeatHoldId(), seatHold);
        seatHold.setStatus(Status.HOLD);
        seatsToHold.forEach(seat -> {
            seat.setSeatHolder(seatHold);
        });

        return seatHold;
    }

}
