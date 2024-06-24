package com.project.FlightReservation.domain.models.airline;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirlineView extends Airline
{
	private List<Seat> seatList;
}
