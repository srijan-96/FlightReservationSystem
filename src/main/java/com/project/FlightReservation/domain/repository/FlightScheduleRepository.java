package com.project.FlightReservation.domain.repository;

import static com.project.FlightReservation.domain.dao.Tables.AIRLINES;
import static com.project.FlightReservation.domain.dao.Tables.AIRPORT;
import static com.project.FlightReservation.domain.dao.Tables.FLIGHT_SCHEDULE;
import static com.project.FlightReservation.domain.dao.Tables.SEAT_PRICING;
import static org.jooq.impl.DSL.day;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.year;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.FlightReservation.domain.dao.tables.records.FlightScheduleRecord;
import com.project.FlightReservation.domain.models.AirlineView;
import com.project.FlightReservation.domain.models.Airport;
import com.project.FlightReservation.domain.models.FlightScheduleRequest;
import com.project.FlightReservation.domain.models.FlightScheduleView;
import com.project.FlightReservation.domain.models.SeatPricing;

@Repository
public class FlightScheduleRepository
{
	@Autowired
	DSLContext dsl;

	public int[] createSchedule(List<FlightScheduleRequest> flightScheduleRequests)
	{
		if(flightScheduleRequests.isEmpty())
		{
			return new int[0];
		}
		List<FlightScheduleRecord> flightScheduleRecords = new ArrayList<>();
		flightScheduleRequests.forEach(ap -> {
			FlightScheduleRecord flightScheduleRecord = new FlightScheduleRecord();
			flightScheduleRecord.setAirlineId(ap.getAirlineId());
			flightScheduleRecord.setStatus(ap.getStatus());
			flightScheduleRecord.setBasePrice(ap.getBasePrice());
			flightScheduleRecord.setArrivalDatetime(ap.getArrivalDatetime());
			flightScheduleRecord.setDepartureDatetime(ap.getDepartureDatetime());
			flightScheduleRecord.setToAirport(ap.getToAirport());
			flightScheduleRecord.setFromAirport(ap.getFromAirport());
			flightScheduleRecords.add(flightScheduleRecord);
		});

		return DSL.using(dsl.configuration()).batchInsert(flightScheduleRecords).execute();
	}

	public List<FlightScheduleView> fetchFlightScheduleView(
		Optional<String> sourceAirportCode,
		Optional<String> destinationAirportCode,
		Optional<OffsetDateTime> departureDate,
		Optional<String> airlineName,
		Optional<String> minPrice,
		Optional<String> maxPrice,
		int offset,
		int limit)
	{

		List<Condition> condition = new ArrayList<>();

		sourceAirportCode.ifPresent(s -> condition.add(AIRPORT.as("source").AIRPORT_CODE.eq(s)));

		destinationAirportCode.ifPresent(s -> condition.add(AIRPORT.as("destination").AIRPORT_CODE.eq(s)));

		if(departureDate.isPresent())
		{
			LocalDate date = departureDate.get().toLocalDate();
			condition.add( year(FLIGHT_SCHEDULE.DEPARTURE_DATETIME).eq(date.getYear())
				.and(month(FLIGHT_SCHEDULE.DEPARTURE_DATETIME).eq(date.getMonthValue()))
				.and(day(FLIGHT_SCHEDULE.DEPARTURE_DATETIME).eq(date.getDayOfMonth()))
			);
		}

		airlineName.ifPresent(s -> condition.add(AIRLINES.NAME.equalIgnoreCase(s)));

		if(minPrice.isPresent() && maxPrice.isPresent())
		{
			condition.add(FLIGHT_SCHEDULE.BASE_PRICE.between(minPrice.get(), maxPrice.get()));
		}
		else if(minPrice.isPresent())
		{
			condition.add(FLIGHT_SCHEDULE.BASE_PRICE.ge(minPrice.get()));
		}
		else
			maxPrice.ifPresent(s -> condition.add(FLIGHT_SCHEDULE.BASE_PRICE.le(s)));

		Result<Record> result = dsl.select(
				FLIGHT_SCHEDULE.fields())
			.select(AIRLINES.fields())
			.select(AIRPORT.as("source").fields())
			.select(AIRPORT.as("destination").fields())
			.select(SEAT_PRICING.fields())
			.from(FLIGHT_SCHEDULE)
			.join(AIRLINES).on(FLIGHT_SCHEDULE.AIRLINE_ID.eq(AIRLINES.AIRLINE_ID))
			.join(AIRPORT.as("source")).on(FLIGHT_SCHEDULE.FROM_AIRPORT.eq(AIRPORT.as("source").AIRPORT_ID))
			.join(AIRPORT.as("destination")).on(FLIGHT_SCHEDULE.TO_AIRPORT.eq(AIRPORT.as("destination").AIRPORT_ID))
			.leftJoin(SEAT_PRICING).on(FLIGHT_SCHEDULE.FLIGHT_SCHEDULE_ID.eq(SEAT_PRICING.FLIGHT_SCHEDULE_ID))
			.where(condition)
			.limit(limit)
			.offset(offset)
			.fetch();

		return result.stream().map(this::mapRecordToFlightScheduleView).collect(Collectors.toList());
	}

	private FlightScheduleView mapRecordToFlightScheduleView(Record record)
	{
		FlightScheduleView flightScheduleView = new FlightScheduleView();
		flightScheduleView.setFlightScheduleId(record.get(FLIGHT_SCHEDULE.FLIGHT_SCHEDULE_ID));
		flightScheduleView.setAirlineId(record.get(FLIGHT_SCHEDULE.AIRLINE_ID));
		flightScheduleView.setToAirport(record.get(FLIGHT_SCHEDULE.TO_AIRPORT));
		flightScheduleView.setFromAirport(record.get(FLIGHT_SCHEDULE.FROM_AIRPORT));
		flightScheduleView.setDepartureDatetime(record.get(FLIGHT_SCHEDULE.DEPARTURE_DATETIME));
		flightScheduleView.setArrivalDatetime(record.get(FLIGHT_SCHEDULE.ARRIVAL_DATETIME));
		flightScheduleView.setBasePrice(record.get(FLIGHT_SCHEDULE.BASE_PRICE));
		flightScheduleView.setStatus(record.get(FLIGHT_SCHEDULE.STATUS));

		AirlineView airlineView = new AirlineView();
		airlineView.setAirlineId(record.get(AIRLINES.AIRLINE_ID));
		airlineView.setName(record.get(AIRLINES.NAME));
		airlineView.setAirlineCode(record.get(AIRLINES.AIRLINE_CODE));
		flightScheduleView.setAirline(airlineView);

		Airport sourceAirport = record.into(AIRPORT.as("source")).into(Airport.class);
		flightScheduleView.setSource(sourceAirport);

		Airport destinationAirport = record.into(AIRPORT.as("destination")).into(Airport.class);
		flightScheduleView.setDestination(destinationAirport);

		if(record.get(SEAT_PRICING.SEAT_PRICING_ID) != null)
		{
			SeatPricing seatPricing = new SeatPricing();
			seatPricing.setSeatPricingId(record.get(SEAT_PRICING.SEAT_PRICING_ID));
			seatPricing.setFlightScheduleId(record.get(SEAT_PRICING.FLIGHT_SCHEDULE_ID));
			seatPricing.setBusinessPrice(record.get(SEAT_PRICING.BUSINESS_PRICE));
			seatPricing.setEconomyPrice(record.get(SEAT_PRICING.ECONOMY_PRICE));
			seatPricing.setFirstClassPrice(record.get(SEAT_PRICING.FIRST_CLASS_PRICE));
			flightScheduleView.setSeatPricing(seatPricing);
		}

		return flightScheduleView;

	}

}
