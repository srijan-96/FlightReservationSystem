package com.project.FlightReservation.exceptions;

public class InvalidPayloadException extends RuntimeException
{
	public InvalidPayloadException(String msg)
	{
		super(msg);
	}
}