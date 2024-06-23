package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Airline
{
	private long airlineId;
	private String name;
	private String airlineCode;
}
