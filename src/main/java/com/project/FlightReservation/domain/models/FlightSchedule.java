package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import javax.validation.constraints.NotNull;

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
	@NotNull(message = "Flight departure time cannot be empty")
	private OffsetDateTime departureDatetime;
	@NotNull(message = "Flight arrival time cannot be empty")
	private OffsetDateTime arrivalDatetime;
	private FlightStatus status;
	@NotNull(message = "Flight base price cannot be empty")
	private String basePrice;
}
