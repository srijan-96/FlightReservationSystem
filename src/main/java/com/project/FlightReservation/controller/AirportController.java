package com.project.FlightReservation.controller;

import lombok.extern.slf4j.Slf4j;

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
import com.project.FlightReservation.domain.models.airline.Airport;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.services.AirportService;

@Controller
@Slf4j
@RequestMapping("/airport")
public class AirportController
{

	@Autowired
	AirportService airportService;

	@CrossOrigin
	@RequestMapping(value = "/details", method = RequestMethod.POST)
	public ResponseEntity<Response> addAirport(
		@RequestBody
		@Valid
		Airport airport)
	{
		Response response;
		try
		{
			log.info("Received request to add airport details : {}", airport);
			response = airportService.saveAirport(airport);
		}
		catch(Exception e)
		{
			log.error("Add airport operation failed with reason - {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/details", method = RequestMethod.PUT)
	public ResponseEntity<Response> updateAirport(
		@RequestBody
		@Valid
		Airport airport)
	{
		Response response;
		try
		{
			if(airport.getAirportId() == 0)
				throw new Exception("Airport id is missing");
			log.info("Received request to update airport details : {}", airport);
			response = airportService.saveAirport(airport);
		}
		catch(Exception e)
		{
			log.error("Update airport operation failed with reason - {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);

		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/{airportId}", method = RequestMethod.GET)
	public ResponseEntity<Response> getAirport(
		@PathVariable("airportId")
		long airportId)
	{
		Response response;
		try
		{
			log.info("Received request to fetch airport details : {}", airportId);
			Airport airport = airportService.getAirportDetails(airportId);
			if(!Optional.ofNullable(airport).isPresent())
				response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success - Record not found", airport);
			else
				response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success", airport);
		}
		catch(Exception e)
		{
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
