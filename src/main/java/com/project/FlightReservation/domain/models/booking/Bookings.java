package com.project.FlightReservation.domain.models.booking;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import com.project.FlightReservation.domain.dao.enums.BookingStatus;

@Setter
@Getter
@ToString
public class Bookings
{
	private long id;
	private long flightId;
	private String bookingId;
	private BookingStatus status;
	List<PassengerBookingDetails> passengerBookingDetailsList;
}
