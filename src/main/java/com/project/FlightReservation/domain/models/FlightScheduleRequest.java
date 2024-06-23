package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.project.FlightReservation.constants.FlightScheduleFrequencyType;
@Setter
@Getter
@ToString
public class FlightScheduleRequest extends FlightSchedule
{
	@NotNull(message = "Flight schedule type cannot be empty")
	private FlightScheduleFrequencyType flightScheduleFrequencyType;
	private OffsetDateTime endDate;
	private List<String> daysOFWeek;
}
