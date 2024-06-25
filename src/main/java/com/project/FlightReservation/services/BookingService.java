package com.project.FlightReservation.services;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.FlightReservation.constants.ResponseStatusCode;
import com.project.FlightReservation.domain.models.booking.BookingView;
import com.project.FlightReservation.domain.models.booking.Bookings;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.domain.models.booking.PassengerBookingDetails;
import com.project.FlightReservation.domain.repository.BookingRepository;
import com.project.FlightReservation.exceptions.InvalidDataException;

@Service
@Slf4j
public class BookingService
{
	@Autowired
	BookingRepository bookingRepository;

	@Autowired
	EmailService emailService;

	@Value("${send.email}")
	private boolean sendEmail;

	public Response createBooking(Bookings bookings)
	{
		if(bookingRepository.isDuplicateBooking(bookings.getBookingId()))
			throw new InvalidDataException("Booking reference id already present. Please check your current bookings.");
		bookingRepository.bookFlights(bookings);
		try
		{
			if(sendEmail)
				emailService.sendEmail(bookings.getPassengerBookingDetailsList().get(0).getEmail(), "E-Ticket for booking", generateETicketContent(bookings));
		}
		catch(Exception ex)
		{
			log.error("Booking is confirmed. Exception in sending email - {}", ex.getMessage());
		}
		return new Response<>(ResponseStatusCode.SUCCESS, "Booking details added successfully", null);
	}

	private String generateETicketContent(Bookings bookings)
	{
		StringBuilder emailBody = new StringBuilder();
		emailBody.append("<h1>Booking Confirmation</h1>").append("<p>Dear ").append(bookings.getPassengerBookingDetailsList().get(0).getName()).append(",</p>").append("<p>Your booking is confirmed. Here are your booking details:</p>").append("<p>Flight ID: " + bookings.getFlightId() + "</p>").append("<p>Booking ID: " + bookings.getBookingId() + "</p>");
		emailBody.append("<h1>Passenger Details</h1>");
		for(PassengerBookingDetails pa : bookings.getPassengerBookingDetailsList())
		{
			emailBody.append("<p>Passenger Name: " + pa.getName() + "</p>");
			emailBody.append("<p>Passenger Email: " + pa.getEmail() + "</p>");
			emailBody.append("<p>Passenger Seat: " + pa.getSeatId() + "</p>");

		}
		return emailBody.toString();
	}

	public BookingView getBookingDetails(String bookingId)
	{
		return bookingRepository.getBookingViewById(bookingId);
	}
}
