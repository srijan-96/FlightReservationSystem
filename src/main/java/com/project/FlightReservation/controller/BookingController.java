package com.project.FlightReservation.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project.FlightReservation.constants.ResponseStatusCode;
import com.project.FlightReservation.domain.models.booking.BookingView;
import com.project.FlightReservation.domain.models.booking.Bookings;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.services.BookingService;

@Controller
@Slf4j
@RequestMapping("/booking")
public class BookingController
{
	@Autowired
	BookingService bookingService;

	@CrossOrigin
	@RequestMapping(value = "/details", method = RequestMethod.POST)
	public ResponseEntity<Response> createBooking(
		@RequestBody
		@Valid
		Bookings bookings)
	{
		Response response;
		try
		{
			log.info("Received request to book flight : {}", bookings.getBookingId());
			response = bookingService.createBooking(bookings);
		}
		catch(MessagingException e)
		{
			log.error("Exception in sending email. Booking is confirmed {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.SUCCESS, "Booking details added successfully", null);
		}
		catch(Exception e)
		{
			log.error("Booking could not be completed with reason - {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/{bookingId}", method = RequestMethod.GET)
	public ResponseEntity<Response> getBooking(
		@PathVariable("bookingId")
		String bookingId)
	{
		Response response;
		try
		{
			log.info("Received request to fetch booking details for : {}", bookingId);
			BookingView bookingView = bookingService.getBookingDetails(bookingId);
			if(!Optional.ofNullable(bookingView).isPresent())
				response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success - Record not found", bookingView);
			else
				response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success", bookingView);
		}
		catch(Exception e)
		{
			log.error("Exception in fetching booking details : {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
