package com.project.FlightReservation.exceptions;

public class SeatNotAvailableException extends RuntimeException
{
	public SeatNotAvailableException(String msg)
	{
		super(msg);
	}
}

