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
import com.project.FlightReservation.domain.models.schedule.FlightSchedule;
import com.project.FlightReservation.domain.models.schedule.FlightScheduleRequest;
import com.project.FlightReservation.domain.models.schedule.FlightScheduleView;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.domain.models.airline.Seat;
import com.project.FlightReservation.domain.models.schedule.SeatPricing;
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
	public ResponseEntity<Response> addSchedule(
		@RequestBody
		@Valid
		FlightScheduleRequest flightScheduleRequest)
	{
		Response response;
		try
		{
			log.info("Received request to create schedule with details : {}", flightScheduleRequest);
			response = flightScheduleService.createSchedule(flightScheduleRequest);
		}
		catch(Exception e)
		{
			log.error("Exception in creating schedule - {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/schedule", method = RequestMethod.PUT)
	public ResponseEntity<Response> updateSchedule(
		@RequestBody
		FlightSchedule flightSchedule)
	{
		Response response;
		try
		{
			if(flightSchedule.getFlightScheduleId() == 0)
				throw new Exception("Flight schedule id is missing");
			log.info("Received request to update schedule with details : {}", flightSchedule);
			response = flightScheduleService.updateSchedule(flightSchedule);
		}
		catch(Exception e)
		{
			log.error("Exception in updating schedule - {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/{flightScheduleId}/pricing", method = RequestMethod.POST)
	public ResponseEntity<Response> addPricing(@PathVariable("flightScheduleId")
	long flightScheduleId,
		@RequestBody
		@Valid
		SeatPricing seatPricing)
	{
		Response response;
		try
		{
			log.info("Received request to add pricing config for schedule : {}", seatPricing.getFlightScheduleId());
			response = pricingService.addPricing(seatPricing, flightScheduleId);
		}
		catch(Exception e)
		{
			log.error("Exception in adding price details for schedule : {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/{flightScheduleId}/pricing", method = RequestMethod.PUT)
	public ResponseEntity<Response> updatePricing(@PathVariable("flightScheduleId")
	long flightScheduleId,
		@RequestBody
		SeatPricing seatPricing)
	{
		Response response;
		try
		{
			log.info("Received request to update pricing config for schedule : {}", seatPricing.getFlightScheduleId());
			response = pricingService.updatePricing(seatPricing, flightScheduleId);
		}
		catch(Exception e)
		{
			log.error("Exception in updating price details for schedule : {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<Response> searchFlights(
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
			log.info("Received request to search and filter flight schedules");
			List<FlightScheduleView> flights = flightScheduleService.getFlightSchedules(sourceCode, destCode, depDate, airline, minP, maxP, offset, limit);
			if(flights.isEmpty())
					response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success - Record not found", flights);
				else
					response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success", flights);
		}
		catch(Exception e)
		{
			log.error("Exception in searching flight schedule  - {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/{flightScheduleId}/availableSeats", method = RequestMethod.GET)
	public ResponseEntity<Response> getAvailableSeats(
		@PathVariable("flightScheduleId")
		long flightScheduleId)
	{
		Response response;
		try
		{
			log.info("Received request to fetch all available seats for flight schedule - {}", flightScheduleId);
			List<Seat> seats = flightScheduleService.getAvailableSeats(flightScheduleId);
			if(seats.isEmpty())
				response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success - No available seats", seats);
			else
				response = new Response<>(ResponseStatusCode.SUCCESS, "Fetch Success", seats);
		}
		catch(Exception e)
		{
			log.error("Exception in fetching available seats - {}", e.getMessage());
			response = new Response<>(ResponseStatusCode.ERROR, e.getMessage(), null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}

