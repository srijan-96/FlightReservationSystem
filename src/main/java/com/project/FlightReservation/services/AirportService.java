package com.project.FlightReservation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.FlightReservation.constants.ResponseStatusCode;
import com.project.FlightReservation.domain.models.airline.Airport;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.domain.repository.AirportRepository;

@Service
public class AirportService
{
	@Autowired
	AirportRepository airportRepository;

	public Response saveAirport(Airport airport)
	{
		airportRepository.saveAirport(airport);
		return new Response<>(ResponseStatusCode.SUCCESS, "Airport added/updated successfully", null);
	}

	public Airport getAirportDetails(long airportId)
	{
		return airportRepository.getAirport(airportId);
	}
}

