package com.project.FlightReservation.domain.repository;

import static com.project.FlightReservation.domain.dao.Tables.SEATS;
import static com.project.FlightReservation.domain.dao.tables.Airlines.AIRLINES;
import static org.jooq.impl.DSL.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.FlightReservation.domain.dao.enums.SeatType;
import com.project.FlightReservation.domain.dao.tables.records.SeatsRecord;
import com.project.FlightReservation.domain.models.airline.Airline;
import com.project.FlightReservation.domain.models.airline.AirlineView;
import com.project.FlightReservation.domain.models.airline.Seat;

@Repository
public class AirlineRepository
{
	@Autowired
	DSLContext dsl;

	public long saveAirlines(Airline model)
	{
		if(model.getAirlineId() == 0)
		{
			return DSL.using(dsl.configuration()).insertInto(AIRLINES).set(AIRLINES.AIRLINE_CODE, model.getAirlineCode()).set(AIRLINES.NAME, model.getName()).execute();
		}
		else
		{
			Condition condition = AIRLINES.AIRLINE_ID.equal(model.getAirlineId());
			UpdateSetMoreStep<?> updateQuery = (UpdateSetMoreStep<?>) DSL.using(dsl.configuration()).update(AIRLINES);
			if(Optional.ofNullable(model.getAirlineCode()).isPresent())
			{
				updateQuery.set(AIRLINES.AIRLINE_CODE, model.getAirlineCode());
			}
			if(Optional.ofNullable(model.getName()).isPresent())
			{
				updateQuery.set(AIRLINES.NAME, model.getName());
			}
			return updateQuery.where(condition).execute();
		}
	}

	public int[] addSeats(long airlineId, List<Seat> seatList)
	{
		if(seatList.isEmpty())
		{
			return new int[0];
		}
		List<SeatsRecord> seatsRecords = new ArrayList<>();
		seatList.forEach(ap -> {
			SeatsRecord seat = new SeatsRecord();
			seat.setAirlineRefId(airlineId);
			seat.setSeatNo(ap.getSeatNo());
			seat.setSeatType(ap.getSeatType());
			seatsRecords.add(seat);
		});

		return DSL.using(dsl.configuration()).batchInsert(seatsRecords).execute();
	}

	public AirlineView getAirlineView(String airlineCode)
	{

		Result<Record> result = DSL.using(dsl.configuration()).select().from(AIRLINES).leftJoin(SEATS).on(AIRLINES.AIRLINE_ID.eq(SEATS.AIRLINE_REF_ID)).where(AIRLINES.AIRLINE_CODE.eq(airlineCode)).fetch();

		// Map the result to AirlineView
		if(result.isEmpty())
		{
			return null;
		}
		// Map airline data
		Airline airline = result.get(0).into(Airline.class);
		List<Seat> seats = result.into(SEATS).stream().filter(record -> Optional.ofNullable(record.get("seat_id")).isPresent()).map(record -> {
			Seat seat = new Seat();
			seat.setSeatId(record.get("seat_id", Long.class));
			seat.setAirlineRefId(record.get("airline_ref_id", Long.class));
			seat.setSeatType(record.get("seat_type", SeatType.class));  // Adjust type if necessary
			seat.setSeatNo(record.get("seat_no", String.class));
			return seat;
		}).collect(Collectors.toList());

		// Create AirlineView
		AirlineView airlineView = new AirlineView();
		airlineView.setAirlineId(airline.getAirlineId());
		airlineView.setName(airline.getName());
		airlineView.setAirlineCode(airline.getAirlineCode());
		airlineView.setSeatList(seats);

		return airlineView;
	}

}
