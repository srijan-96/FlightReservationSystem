package com.project.FlightReservation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.FlightReservation.constants.ResponseStatusCode;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.domain.models.SeatPricing;
import com.project.FlightReservation.domain.repository.PricingRepository;

@Service
public class PricingService
{

	@Autowired
	PricingRepository pricingRepository;

	public Response addPricing(SeatPricing seatPricing, long flightScheduleId)
	{
		pricingRepository.addPricing(seatPricing, flightScheduleId);
		return new Response<>(ResponseStatusCode.SUCCESS, "Price details added successfully", null);
	}

	public Response updatePricing(SeatPricing seatPricing, long flightScheduleId)
	{
		pricingRepository.updatePricing(seatPricing, flightScheduleId);
		return new Response<>(ResponseStatusCode.SUCCESS, "Price details updated successfully", null);
	}
}
