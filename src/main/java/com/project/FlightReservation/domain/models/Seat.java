package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.project.FlightReservation.domain.dao.enums.SeatType;

@Setter
@Getter
@ToString
public class Seat
{
	private long seatId;
	private long airlineRefId;
	@NotNull(message = "Seat type cannot be empty")
	private SeatType seatType;
	@NotEmpty(message = "Seat no cannot be empty")
	private String seatNo;
}
