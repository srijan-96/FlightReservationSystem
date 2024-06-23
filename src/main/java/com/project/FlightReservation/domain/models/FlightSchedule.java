package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import com.project.FlightReservation.domain.dao.enums.FlightStatus;

@Setter
@Getter
@ToString
public class FlightSchedule
{
	private long flightScheduleId;
	private long airlineId;
	private long toAirport;
	private long fromAirport;
	private LocalDateTime departureDatetime;
	private LocalDateTime arrivalDatetime;
	private FlightStatus status;
	private String basePrice;
	private long seatPricingId;
}
