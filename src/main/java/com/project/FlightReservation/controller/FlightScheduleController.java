package com.project.FlightReservation.controller;

import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.project.FlightReservation.constants.ResponseStatusCode;
import com.project.FlightReservation.domain.models.FlightScheduleRequest;
import com.project.FlightReservation.domain.models.FlightScheduleView;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.domain.models.SeatPricing;
import com.project.FlightReservation.domain.repository.FlightScheduleRepository;
import com.project.FlightReservation.services.FlightScheduleService;
import com.project.FlightReservation.services.PricingService;

@Controller
@Slf4j
@RequestMapping("/flight")
public class FlightScheduleController
{
	@Autowired
	PricingService pricingService;
	@Autowired
	FlightScheduleService flightScheduleService;


	@Autowired
	FlightScheduleRepository flightScheduleRepository;


	@CrossOrigin
	@RequestMapping(value = "/schedule", method = RequestMethod.POST)
	public ResponseEntity<Response> addAirline(
		@RequestBody
		@Valid
		FlightScheduleRequest flightScheduleRequest)
	{
		Response response;
		try
		{
			response = flightScheduleService.createSchedule(flightScheduleRequest);
		}
		catch(Exception e)
		{
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
			log.error("", e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/{flightScheduleId}/pricing", method = RequestMethod.POST)
	public ResponseEntity<Response> addPricing(@PathVariable("flightScheduleId")
	long flightScheduleId,
		@RequestBody
		SeatPricing seatPricing)
	{
		Response response;
		try
		{
			response = pricingService.addPricing(seatPricing, flightScheduleId);
		}
		catch(Exception e)
		{
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
			log.error("", e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/{flightScheduleId}/pricing", method = RequestMethod.PUT)
	public ResponseEntity<Response> updatePricing(@PathVariable("airlineCode")
	long flightScheduleId,
		@RequestBody
		SeatPricing seatPricing)
	{
		Response response;
		try
		{
			response = pricingService.updatePricing(seatPricing, flightScheduleId);
		}
		catch(Exception e)
		{
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
			log.error("", e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<Response> updatePricing(
		@RequestParam(value = "sourceAirportCode", required = false)
		String sourceAirportCode,
		@RequestParam(value = "destinationAirportCode", required = false)
		String destinationAirportCode,
		@RequestParam(value = "departureDate", required = false)
		String departureDate,
		@RequestParam(value = "airlineName", required = false)
		String airlineName,
		@RequestParam(value = "minPrice", required = false)
		String minPrice,
		@RequestParam(value = "maxPrice", required = false)
		String maxPrice,
		@RequestParam(value = "offset", defaultValue = "0")
		int offset,
		@RequestParam(value = "limit", defaultValue = "10")
		int limit)
	{
		Response response;
		Optional<String> sourceCode = Optional.ofNullable(sourceAirportCode);
		Optional<String> destCode = Optional.ofNullable(destinationAirportCode);
		Optional<OffsetDateTime> depDate = departureDate != null ? Optional.of(OffsetDateTime.parse(departureDate, DateTimeFormatter.ISO_DATE_TIME)) : Optional.empty();
		Optional<String> airline = Optional.ofNullable(airlineName);
		Optional<String> minP = Optional.ofNullable(minPrice);
		Optional<String> maxP = Optional.ofNullable(maxPrice);

		try
		{
			List<FlightScheduleView> flights = flightScheduleService.getFlightSchedules(sourceCode, destCode, depDate, airline, minP, maxP, offset, limit);
			if(flights.isEmpty())
					response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success - Record not found", flights);
				else
					response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success", flights);
		}
		catch(Exception e)
		{
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
			log.error("", e);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}

