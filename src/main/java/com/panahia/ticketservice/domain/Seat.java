package com.panahia.ticketservice.domain;

/**
 * Created by Ali Panahi on 5/10/18.
 */
public class Seat {
    protected int column;

    protected int row;

    protected String customerEmail;

    protected SeatHold seatHolder;

    public Seat(int row, int column, Status status) {
        this.column = column;
        this.row = row;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public SeatHold getSeatHolder() {
        return seatHolder;
    }

    public void setSeatHolder(SeatHold seatHolder) {
        this.seatHolder = seatHolder;
    }

    public boolean isAvailable() {
        if(seatHolder == null)
            return true;
        else
            return seatHolder.isAvailable();
    }
}
