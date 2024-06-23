package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.databind.JsonNode;

@Setter
@Getter
@ToString
public class FlightScheduleView extends FlightSchedule
{
	private AirlineView airline;
	private Airport source;
	private Airport destination;
	private JsonNode seatPricing;
}
