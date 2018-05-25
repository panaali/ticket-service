package com.panahia.ticketservice.domain;

import com.panahia.ticketservice.Config;

import java.util.stream.IntStream;


/**
 * Created by Ali Panahi on 5/21/18.
 */
public class Venue {

    protected Seat[][] seats;

    public Venue(int rowCount, int columnCount){
        Config config = Config.getInstance();
        seats = new Seat[rowCount][columnCount];
        initializeSeats();
    }

    public void initializeSeats(){
        IntStream.range(0, seats.length)
                .forEach( row -> IntStream.range(0, seats[0].length)
                        .forEach( column -> {
                            seats[row][column] = new Seat(row, column, Status.AVAILABLE);
                        }));
    }

    public int getRowCount(){
        return seats.length;
    }

    public int getColCount(){
        return seats[0].length;
    }

    public int getAvailableSeats(){
        int availableSeats = 0;
        for (Seat seatRow[] : seats) {
            for (Seat seat : seatRow) {
                if(seat.isAvailable())
                    availableSeats++;
            }
        }
        return availableSeats;
    }

    public Seat[][] getSeats(){
        return seats;
    }

    public void setSeats(Seat[][] seats){
        this.seats = seats;
    }

    public String printSeatsScore(){
        return seats.toString();
    }
}
