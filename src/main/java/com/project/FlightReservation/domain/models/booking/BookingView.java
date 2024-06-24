package com.project.FlightReservation.domain.models.booking;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BookingView extends Bookings
{
	private String airlineName;
	private String airlineCode;
	private String sourceAirportCode;
	private String destinationAirportCode;
}
