package com.project.FlightReservation.domain.models.booking;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PassengerBookingDetails
{
	private String name;
	private String email;
	private long seatId;
	private String seatNo;

}
