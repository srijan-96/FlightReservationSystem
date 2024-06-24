package com.project.FlightReservation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.FlightReservation.constants.ResponseStatusCode;
import com.project.FlightReservation.domain.models.booking.BookingView;
import com.project.FlightReservation.domain.models.booking.Bookings;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.domain.repository.BookingRepository;
import com.project.FlightReservation.exceptions.InvalidPayloadException;

@Service
public class BookingService
{
	@Autowired
	BookingRepository bookingRepository;

	public Response createBooking(Bookings bookings)
	{

		if(bookingRepository.isDuplicateBooking(bookings.getBookingId()))
			throw new InvalidPayloadException("Booking reference id already present. Please check your current bookings.");

		bookingRepository.bookFlights(bookings);
		return new Response<>(ResponseStatusCode.SUCCESS, "Booking details added successfully", null);

	}

	public BookingView getBookingDetails(String bookingId)
	{
		return bookingRepository.getBookingViewById(bookingId);
	}
}
