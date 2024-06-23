package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Airport
{
	private long airportId;
	private String name;
	private String airportCode;
	private Double latitude;
	private Double longitude;

}
