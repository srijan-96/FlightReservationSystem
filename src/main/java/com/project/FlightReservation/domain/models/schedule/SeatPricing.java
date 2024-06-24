package com.project.FlightReservation.domain.models.schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
