package com.project.FlightReservation.domain.models.schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class SeatPricing
{
	private long seatPricingId;
	@NotNull(message = "Flight schedule id cannot be empty")
	private long flightScheduleId;
	@NotEmpty(message = "Business seat price cannot be empty")
	private String businessPrice;
	@NotEmpty(message = "Economy seat price cannot be empty")
	private String economyPrice;
	@NotEmpty(message = "First class seat price cannot be empty")
	private String firstClassPrice;
}
