package com.project.FlightReservation.services;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.FlightReservation.constants.FlightScheduleFrequencyType;
import com.project.FlightReservation.constants.ResponseStatusCode;
import com.project.FlightReservation.domain.dao.enums.FlightStatus;
import com.project.FlightReservation.domain.models.schedule.FlightSchedule;
import com.project.FlightReservation.domain.models.schedule.FlightScheduleRequest;
import com.project.FlightReservation.domain.models.schedule.FlightScheduleView;
import com.project.FlightReservation.domain.models.Response;
import com.project.FlightReservation.domain.models.airline.Seat;
import com.project.FlightReservation.domain.repository.FlightScheduleRepository;
import com.project.FlightReservation.exceptions.InvalidDataException;

@Service
public class FlightScheduleService
{

	@Autowired
	FlightScheduleRepository flightScheduleRepository;

	public Response createSchedule(FlightScheduleRequest flightScheduleRequest)
	{

		List<FlightScheduleRequest> flightScheduleList = new ArrayList<>();
		OffsetDateTime currentDate = flightScheduleRequest.getDepartureDatetime();
		OffsetDateTime endDate = flightScheduleRequest.getEndDate();
		if(currentDate.isBefore(OffsetDateTime.now()))
			throw new InvalidDataException("Departure datetime is in the past - Not allowed");

		if(flightScheduleRequest.getDepartureDatetime().isAfter(flightScheduleRequest.getArrivalDatetime()))
			throw new InvalidDataException("Arrival datetime is before Departure time - Not allowed");

		if(flightScheduleRequest.getFlightScheduleFrequencyType() == FlightScheduleFrequencyType.NA)
		{
			flightScheduleRequest.setStatus(FlightStatus.SCHEDULED);
			flightScheduleList.add(flightScheduleRequest);
		}
		else
		{
			if(currentDate.isAfter(endDate))
				throw new InvalidDataException("End date cannot be before current date");
			while(!currentDate.isAfter(endDate))
			{
				if(flightScheduleRequest.getFlightScheduleFrequencyType() == FlightScheduleFrequencyType.DAILY)
				{
					addDailySchedule(flightScheduleList, flightScheduleRequest, currentDate);
					currentDate = currentDate.plusDays(1);
				}
				else if(flightScheduleRequest.getFlightScheduleFrequencyType() == FlightScheduleFrequencyType.WEEKLY)
				{
					if(flightScheduleRequest.getDaysOFWeek().contains(currentDate.getDayOfWeek().name()))
					{
						addDailySchedule(flightScheduleList, flightScheduleRequest, currentDate);
					}
					currentDate = currentDate.plusDays(1);
				}
			}

		}
		flightScheduleRepository.createSchedule(flightScheduleList);
		return new Response<>(ResponseStatusCode.SUCCESS, "Flight schedules added successfully", null);
	}

	private void addDailySchedule(List<FlightScheduleRequest> schedules, FlightScheduleRequest request, OffsetDateTime departureDateTime)
	{
		FlightScheduleRequest schedule = new FlightScheduleRequest();
		schedule.setStatus(FlightStatus.SCHEDULED);
		schedule.setAirlineId(request.getAirlineId());
		schedule.setToAirport(request.getToAirport());
		schedule.setFromAirport(request.getFromAirport());
		schedule.setDepartureDatetime(departureDateTime);
		schedule.setArrivalDatetime(departureDateTime.plusSeconds(request.getArrivalDatetime().toEpochSecond() - request.getDepartureDatetime().toEpochSecond()));
		schedule.setBasePrice(request.getBasePrice());
		schedules.add(schedule);
	}

	public List<FlightScheduleView> getFlightSchedules(Optional<String> sourceAirportCode, Optional<String> destinationAirportCode, Optional<OffsetDateTime> departureDate, Optional<String> airlineName, Optional<String> minPrice, Optional<String> maxPrice, int offset, int limit)
	{
		return flightScheduleRepository.fetchFlightScheduleView(sourceAirportCode, destinationAirportCode, departureDate, airlineName, minPrice, maxPrice, offset, limit);
	}

	public List<Seat> getAvailableSeats(long flightScheduleId)
	{
		return flightScheduleRepository.getAvailableSeats(flightScheduleId);
	}

	public Response updateSchedule(FlightSchedule flightSchedule)
	{
		flightScheduleRepository.updateSchedule(flightSchedule, flightSchedule.getFlightScheduleId());
		return new Response<>(ResponseStatusCode.SUCCESS, "Flight schedule updated successfully", null);
	}

}

