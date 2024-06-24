package com.project.FlightReservation.domain.models.airline;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@ToString
public class Airline
{
	private long airlineId;
	@NotEmpty(message = "Airline name cannot be empty")
	private String name;
	@NotEmpty(message = "Airline code cannot be empty")
	private String airlineCode;
}
