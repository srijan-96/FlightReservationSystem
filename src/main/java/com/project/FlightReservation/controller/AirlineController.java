package com.project.FlightReservation.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

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
import com.project.FlightReservation.domain.models.Airline;
import com.project.FlightReservation.domain.models.AirlineView;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.domain.models.Seat;
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
		@RequestBody
		Airline airline)
	{
		Response response = null;
		try
		{
			response = airlineService.saveAirlines(airline);
		}
		catch(Exception e)
		{
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
			log.error("", e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/{airlineId}/seats", method = RequestMethod.POST)
	public ResponseEntity<Response> addSeats(@PathVariable("airlineId")
	long airlineId,
		@RequestBody
		List<Seat> seatList)
	{
		Response response = null;
		try
		{
			response = airlineService.addSeats(airlineId, seatList);
		}
		catch(Exception e)
		{
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
			log.error("", e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/view/{airlineCode}", method = RequestMethod.GET)
	public ResponseEntity<Response> getAirline(@PathVariable("airlineCode")
	String airlineCode)
	{
		Response response = null;
		try
		{
			AirlineView airlineView = airlineService.getAirlineView(airlineCode);
			if(!Optional.ofNullable(airlineView).isPresent())
				response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success - Record not found", airlineView);
			else
				response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success", airlineView);


		}
		catch(Exception e)
		{
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
			log.error("", e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
