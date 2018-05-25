package com.panahia.ticketservice;

import com.panahia.ticketservice.domain.SeatHold;
import com.panahia.ticketservice.service.DefaultTicketService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;


/**
 * Created by Ali Panahi on 5/21/18.
 */
public class DefaultTicketServiceTest {
    DefaultTicketService defaultTicketService;
    String customerEmail = "ali@apanahi.com";
    Config config = Config.getInstance();
    int venueCapacity = config.getVenueColumnCount() * config.getVenueRowCount();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void initialNumSeatsAvailableWithConfigTest() throws Exception {
        defaultTicketService = new DefaultTicketService();
        assertEquals(venueCapacity, defaultTicketService.numSeatsAvailable());
    }

    @Test
    public void initialNumSeatsAvailableWithoutConfigTest() throws Exception {
        int rowCount = 10;
        int columnCount = 20;
        defaultTicketService = new DefaultTicketService(rowCount, columnCount);

        assertEquals(rowCount * columnCount, defaultTicketService.numSeatsAvailable());
    }

    @Test
    public void initialNumSeatsAvailableOddColumnCountWithoutConfigTest() throws Exception {
        int rowCount = 10;
        int columnCount = 9;
        defaultTicketService = new DefaultTicketService(rowCount, columnCount);

        assertEquals(rowCount * columnCount, defaultTicketService.numSeatsAvailable());
    }

    @Test
    public void initialNumSeatsAvailableOddColumnCountWithoutConfig2Test() throws Exception {
        int rowCount = 10;
        int columnCount = 9;
        int numSeats = 5;
        defaultTicketService = new DefaultTicketService(rowCount, columnCount);
        SeatHold seatHold = defaultTicketService.findAndHoldSeats(numSeats, customerEmail);
        assertEquals(rowCount * columnCount - numSeats, defaultTicketService.numSeatsAvailable());
    }

    @Test
    public void initialNumSeatsAvailableOddColumnCountWithoutConfig3Test() throws Exception {
        int rowCount = 10;
        int columnCount = 9;
        int numSeats = 0;
        defaultTicketService = new DefaultTicketService(rowCount, columnCount);
        SeatHold seatHold = defaultTicketService.findAndHoldSeats(rowCount * columnCount - numSeats, customerEmail);
        assertEquals(numSeats, defaultTicketService.numSeatsAvailable());
    }

    @Test
    public void findAndHoldSeatsZeroSeatsTest() throws Exception {
        int numSeats = 0;
        defaultTicketService = new DefaultTicketService();

        SeatHold seatHold = defaultTicketService.findAndHoldSeats(numSeats, customerEmail);
        assertEquals(venueCapacity - numSeats, defaultTicketService.numSeatsAvailable() );
    }

    @Test
    public void findAndHoldSeatsFiveSeatsTest() throws Exception {
        int numSeats = 5;
        defaultTicketService = new DefaultTicketService();

        SeatHold seatHold = defaultTicketService.findAndHoldSeats(numSeats, customerEmail);
        assertEquals(venueCapacity - numSeats, defaultTicketService.numSeatsAvailable());
    }

    @Test
    public void findAndHoldSeatsSeatsVenueCapacityTest() throws Exception {
        defaultTicketService = new DefaultTicketService();
        int numSeats = venueCapacity;

        SeatHold seatHold = defaultTicketService.findAndHoldSeats(numSeats, customerEmail);
        assertEquals(venueCapacity - numSeats , defaultTicketService.numSeatsAvailable());
    }

    @Test
    public void findAndHoldSeatsExpiredHoldAgainSeats() throws Exception {
        int numSeats = venueCapacity;
        defaultTicketService = new DefaultTicketService();
        SeatHold seatHold = defaultTicketService.findAndHoldSeats(numSeats, customerEmail);

        sleep(( config.getHoldSeatLifeTime() + 1 ) * 1000 );

        SeatHold seatHoldAgain = defaultTicketService.findAndHoldSeats(numSeats, customerEmail);
        assertEquals(venueCapacity - numSeats , defaultTicketService.numSeatsAvailable());
    }

    @Test
    public void findAndHoldSeatsWithMoreThanCapacityTest() throws Exception {
        int numSeats = venueCapacity + 1;
        defaultTicketService = new DefaultTicketService();

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Number of seats requested is more than number of available seats");

        SeatHold seatHold = defaultTicketService.findAndHoldSeats(numSeats, customerEmail);
        assertEquals(venueCapacity , defaultTicketService.numSeatsAvailable());
    }

    @Test
    public void findAndHoldSeatsNotValidEmailAddressTest() throws Exception {
        int numSeats = 5;
        defaultTicketService = new DefaultTicketService();

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Not a valid email address");
        SeatHold seatHold = defaultTicketService.findAndHoldSeats(numSeats, "invalidemail.com");
    }

    @Test
    public void reserveSeatsSeatAlreadyReservedTest() throws Exception {
        int numSeats = 5;
        defaultTicketService = new DefaultTicketService();
        SeatHold seatHold = defaultTicketService.findAndHoldSeats(numSeats, customerEmail);
        defaultTicketService.reserveSeats(seatHold.getSeatHoldId(), customerEmail);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("This seat already reserved");
        defaultTicketService.reserveSeats(seatHold.getSeatHoldId(), customerEmail);
    }

    @Test
    public void reserveSeatsConfirmationCodeLengthTest() throws Exception {
        int numSeats = 5;
        defaultTicketService = new DefaultTicketService();
        SeatHold seatHold = defaultTicketService.findAndHoldSeats(numSeats, customerEmail);
        String confirmationCode = defaultTicketService.reserveSeats(seatHold.getSeatHoldId(), customerEmail);
        assertEquals(confirmationCode.length(), config.getConfirmationCodeLength());
    }

    @Test
    public void reserveSeatsNoHoldIdTest() throws Exception {
        defaultTicketService = new DefaultTicketService();

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("No seat held for this id");
        defaultTicketService.reserveSeats(0, customerEmail);
    }

    @Test
    public void reserveSeatsEmailMismatchTest() throws Exception {
        int numSeats = 5;
        defaultTicketService = new DefaultTicketService();
        SeatHold seatHold = defaultTicketService.findAndHoldSeats(numSeats, customerEmail);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The email address to reserve ticket doesn't match the email address used to hold the seats");
        defaultTicketService.reserveSeats(seatHold.getSeatHoldId(), "mismatch@email.com");
    }

    @Test
    public void reserveSeatsExpiredSeats() throws Exception {
        int numSeats = 5;
        defaultTicketService = new DefaultTicketService();
        SeatHold seatHold = defaultTicketService.findAndHoldSeats(numSeats, customerEmail);

        sleep(( config.getHoldSeatLifeTime() + 1 ) * 1000 );

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Your hold for the seats have expired");
        defaultTicketService.reserveSeats(seatHold.getSeatHoldId(), customerEmail);
    }
}