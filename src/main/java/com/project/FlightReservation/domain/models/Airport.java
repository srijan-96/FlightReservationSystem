package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class Airport
{
	private long airportId;
	@NotEmpty(message = "Airport name cannot be empty")
	private String name;
	@NotEmpty(message = "Airport code cannot be empty")
	private String airportCode;
	@NotNull(message = "Airport latitude cannot be empty")
	private Double latitude;
	@NotNull(message = "Airport longitude cannot be empty")
	private Double longitude;

}
