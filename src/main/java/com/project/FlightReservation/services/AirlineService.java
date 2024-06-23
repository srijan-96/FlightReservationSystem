package com.project.FlightReservation.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.FlightReservation.constants.ResponseStatusCode;
import com.project.FlightReservation.domain.models.Airline;
import com.project.FlightReservation.domain.models.AirlineView;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.domain.models.Seat;
import com.project.FlightReservation.domain.repository.AirlineRepository;

@Service
public class AirlineService
{
	@Autowired
	AirlineRepository airlineRepository;

	public Response saveAirlines(Airline airline)
	{
		airlineRepository.saveAirlines(airline);
		return new Response<>(ResponseStatusCode.SUCCESS, "Airline added successfully", null);

	}

	public Response addSeats(long airlineId, List<Seat> seats)
	{
		airlineRepository.addSeats(airlineId, seats);
		return new Response<>(ResponseStatusCode.SUCCESS, "Seats added to airline", null);
	}

	public AirlineView getAirlineView(String airlineCode)
	{
		return airlineRepository.getAirlineView(airlineCode);
	}

}
