package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class AirlineView extends Airline
{
	private List<Seat> seatList;
}
