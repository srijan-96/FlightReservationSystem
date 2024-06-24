package com.project.FlightReservation.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

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
import com.project.FlightReservation.domain.models.airline.Airline;
import com.project.FlightReservation.domain.models.airline.AirlineView;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.domain.models.airline.Seat;
import com.project.FlightReservation.services.AirlineService;

@Controller
@Slf4j
@RequestMapping("/airlines")
public class AirlineController
{
	@Autowired
	AirlineService airlineService;

	@CrossOrigin
	@RequestMapping(value = "/details", method = RequestMethod.POST)
	public ResponseEntity<Response> addAirline(
		@Valid
		@RequestBody
		Airline airline)
	{
		Response response;
		try
		{
			log.info("Received request to add airline details : {}", airline);
			response = airlineService.saveAirlines(airline);
		}
		catch(Exception e)
		{
			log.error("Add airlines operation failed with reason - {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);

		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/details", method = RequestMethod.PUT)
	public ResponseEntity<Response> updateAirline(
		@Valid
		@RequestBody
		Airline airline)
	{
		Response response;
		try
		{

			if(airline.getAirlineId() == 0)
				throw new Exception("Airline id is missing");
			log.info("Received request to update airline details : {}", airline);
			response = airlineService.saveAirlines(airline);
		}
		catch(Exception e)
		{
			log.error("Update airlines operation failed with reason - {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/{airlineId}/seats", method = RequestMethod.POST)
	public ResponseEntity<Response> addSeats(@PathVariable("airlineId")
	long airlineId,
		@Valid
		@RequestBody
		List<Seat> seatList)
	{
		Response response;
		try
		{
			log.info("Received request to update airline seat details : {}", seatList);
			response = airlineService.addSeats(airlineId, seatList);
		}
		catch(Exception e)
		{
			log.error("Failed to add seats to airline - {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/view/{airlineCode}", method = RequestMethod.GET)
	public ResponseEntity<Response> getAirline(@PathVariable("airlineCode")
	String airlineCode)
	{
		Response response;
		try
		{
			log.info("Received request to fetch airline details for : {}", airlineCode);
			AirlineView airlineView = airlineService.getAirlineView(airlineCode);
			if(!Optional.ofNullable(airlineView).isPresent())
				response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success - Record not found", airlineView);
			else
				response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success", airlineView);
		}
		catch(Exception e)
		{
			log.error("Failed to fetch airline details - {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
