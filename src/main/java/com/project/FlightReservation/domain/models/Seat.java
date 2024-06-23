package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.project.FlightReservation.domain.dao.enums.SeatType;

@Setter
@Getter
@ToString
public class Seat
{
	private long seatId;
	private long airlineRefId;
	private SeatType seatType;
	private String seatNo;
}
