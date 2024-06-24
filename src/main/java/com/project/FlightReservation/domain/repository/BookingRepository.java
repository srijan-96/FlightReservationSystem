package com.project.FlightReservation.domain.repository;

import static com.project.FlightReservation.domain.dao.Tables.AIRLINES;
import static com.project.FlightReservation.domain.dao.Tables.AIRPORT;
import static com.project.FlightReservation.domain.dao.Tables.BOOKINGS;
import static com.project.FlightReservation.domain.dao.Tables.FLIGHT_SCHEDULE;
import static com.project.FlightReservation.domain.dao.Tables.SEATS;
import static org.jooq.impl.DSL.select;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import com.project.FlightReservation.domain.dao.enums.BookingStatus;
import com.project.FlightReservation.domain.dao.tables.records.BookingsRecord;
import com.project.FlightReservation.domain.models.booking.BookingView;
import com.project.FlightReservation.domain.models.booking.Bookings;
import com.project.FlightReservation.domain.models.booking.PassengerBookingDetails;
import com.project.FlightReservation.exceptions.SeatNotAvailableException;

@Repository
public class BookingRepository
{
	@Autowired
	DSLContext dsl;

	public void bookFlights(Bookings booking)
	{
		String bookingId = booking.getBookingId();
		Long flightId = booking.getFlightId();
		try
		{
			dsl.transaction(configuration -> {
				DSLContext ctx = DSL.using(configuration);

				for(PassengerBookingDetails passengerBookingDetails : booking.getPassengerBookingDetailsList())
				{
					// Check if the seat is already booked
					boolean seatExists = ctx.fetchExists(ctx.selectFrom(BOOKINGS).where(BOOKINGS.FLIGHT_ID.eq(flightId)).and(BOOKINGS.SEAT_ID.eq(passengerBookingDetails.getSeatId())));

					if(seatExists)
					{
						throw new SeatNotAvailableException("Seat " + passengerBookingDetails.getSeatId() + " is already booked");
					}

					// Mock payment processing
					boolean paymentSuccessful = processPayment();

					if(paymentSuccessful)
					{
						// Create booking record
						BookingsRecord bookingRecord = ctx.newRecord(BOOKINGS);
						bookingRecord.setBookingId(bookingId);
						bookingRecord.setFlightId(flightId);
						bookingRecord.setPassengerName(passengerBookingDetails.getName());
						bookingRecord.setPassengerEmail(passengerBookingDetails.getEmail());
						bookingRecord.setSeatId(passengerBookingDetails.getSeatId());
						bookingRecord.setStatus(BookingStatus.CONFIRMED);  // Assuming successful payment

						int insertedRows = ctx.insertInto(BOOKINGS).set(bookingRecord).execute();
						if(insertedRows == 0)
						{
							throw new SeatNotAvailableException("The seat " + passengerBookingDetails.getSeatId() + " is no longer available. Please try booking a different seat.");
						}
					}
				}
			});
		}
		catch(DuplicateKeyException e)
		{
			throw new SeatNotAvailableException("One or more seats are no longer available. Please try booking different seats.");
		}
	}

	private boolean processPayment()
	{
		return true; // Mock response
	}

	public boolean isDuplicateBooking(String bookingId)
	{
		return DSL.using(dsl.configuration()).fetchExists(select().from(BOOKINGS).where(BOOKINGS.BOOKING_ID.eq(bookingId)));
	}

	public BookingView getBookingViewById(String bookingId)
	{
		Table<?> fromAirport = AIRPORT.as("from_airport");
		Table<?> toAirport = AIRPORT.as("to_airport");

		Result<Record> records = dsl.select().
			from(BOOKINGS).
			join(SEATS).
			on(BOOKINGS.SEAT_ID.eq(SEATS.SEAT_ID)).
			join(FLIGHT_SCHEDULE).on(BOOKINGS.FLIGHT_ID.eq(FLIGHT_SCHEDULE.FLIGHT_SCHEDULE_ID)).
			join(AIRLINES).on(FLIGHT_SCHEDULE.AIRLINE_ID.eq(AIRLINES.AIRLINE_ID)).
			join(fromAirport).on(FLIGHT_SCHEDULE.FROM_AIRPORT.eq(fromAirport.field(AIRPORT.AIRPORT_ID))).
			join(toAirport).on(FLIGHT_SCHEDULE.TO_AIRPORT.eq(toAirport.field(AIRPORT.AIRPORT_ID))).
			where(BOOKINGS.BOOKING_ID.eq(bookingId)).
			fetch();

		if(!records.isEmpty())
		{
			Record record = records.get(0);
			BookingView bookingView = new BookingView();
			bookingView.setId(record.get(BOOKINGS.ID));
			bookingView.setFlightId(record.get(BOOKINGS.FLIGHT_ID));
			bookingView.setBookingId(record.get(BOOKINGS.BOOKING_ID));
			bookingView.setStatus(BookingStatus.valueOf(record.get(BOOKINGS.STATUS).name()));

			bookingView.setAirlineName(record.get(AIRLINES.NAME));
			bookingView.setAirlineCode(record.get(AIRLINES.AIRLINE_CODE));

			bookingView.setSourceAirportCode(record.get(fromAirport.field(AIRPORT.AIRPORT_CODE),String.class));
			bookingView.setDestinationAirportCode(record.get(toAirport.field(AIRPORT.AIRPORT_CODE),String.class));

			List<PassengerBookingDetails> passengerDetailsList = records.stream()
				.map(ap -> {
					PassengerBookingDetails details = new PassengerBookingDetails();
					details.setName(ap.get(BOOKINGS.PASSENGER_NAME));
					details.setEmail(ap.get(BOOKINGS.PASSENGER_EMAIL));
					details.setSeatId(ap.get(BOOKINGS.SEAT_ID));
					details.setSeatNo(ap.get(SEATS.SEAT_NO));
					return details;
				})
				.collect(Collectors.toList());

			bookingView.setPassengerBookingDetailsList(passengerDetailsList);

			return bookingView;
		}
		return null;
	}
}

