package com.project.FlightReservation.exceptions;

public class PaymentFailedException extends RuntimeException
{
    public PaymentFailedException(String msg)
    {
        super(msg);
    }
}

