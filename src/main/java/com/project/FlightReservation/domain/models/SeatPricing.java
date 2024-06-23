package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.databind.JsonNode;

@Setter
@Getter
@ToString
public class SeatPricing
{
	private long seatPricingId;
	private long flightScheduleId;
	private String businessPrice;
	private String economyPrice;
	private String firstClassPrice;
}
