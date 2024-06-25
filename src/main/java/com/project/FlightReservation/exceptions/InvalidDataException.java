package com.project.FlightReservation.exceptions;

public class InvalidDataException extends RuntimeException
{
	public InvalidDataException(String msg)
	{
		super(msg);
	}
}