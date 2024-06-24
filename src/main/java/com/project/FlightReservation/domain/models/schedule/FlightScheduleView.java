package com.project.FlightReservation.domain.models.schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.FlightReservation.domain.models.airline.AirlineView;
import com.project.FlightReservation.domain.models.airline.Airport;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightScheduleView extends FlightSchedule
{
	private AirlineView airline;
	private Airport source;
	private Airport destination;
	private SeatPricing seatPricing;
}
