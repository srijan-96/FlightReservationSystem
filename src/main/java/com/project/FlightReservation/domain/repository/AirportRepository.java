package com.project.FlightReservation.domain.repository;

import static com.project.FlightReservation.domain.dao.Tables.AIRPORT;
import java.math.BigDecimal;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.FlightReservation.domain.models.airline.Airport;

@Repository
public class AirportRepository
{

	@Autowired
	DSLContext dsl;

	public long saveAirport(Airport airport)
	{
		if(airport.getAirportId() == 0)
		{
			return DSL.using(dsl.configuration()).insertInto(AIRPORT)
				.set(AIRPORT.AIRPORT_CODE, airport.getAirportCode())
			    .set(AIRPORT.NAME, airport.getName())
				.set(AIRPORT.LATITUDE, BigDecimal.valueOf(airport.getLatitude()))
				.set(AIRPORT.LONGITUDE,  BigDecimal.valueOf(airport.getLongitude())).
				execute();
		}
		else
		{
			Condition condition = AIRPORT.AIRPORT_ID.equal(airport.getAirportId());
			UpdateSetMoreStep<?> updateQuery = (UpdateSetMoreStep<?>) DSL.using(dsl.configuration()).update(AIRPORT);
			if(Optional.ofNullable(airport.getAirportCode()).isPresent())
			{
				updateQuery.set(AIRPORT.AIRPORT_CODE, airport.getAirportCode());
			}
			if(Optional.ofNullable(airport.getName()).isPresent())
			{
				updateQuery.set(AIRPORT.NAME, airport.getName());
			}
			if(Optional.ofNullable(airport.getLatitude()).isPresent())
			{
				updateQuery.set(AIRPORT.LATITUDE, BigDecimal.valueOf(airport.getLatitude()));
			}
			if(Optional.ofNullable(airport.getLongitude()).isPresent())
			{
				updateQuery.set(AIRPORT.LONGITUDE,  BigDecimal.valueOf(airport.getLongitude()));
			}
			return updateQuery.where(condition).execute();
		}
	}

	public Airport getAirport(long airportId)
	{
		return DSL.using(dsl.configuration()).selectFrom(AIRPORT).where(AIRPORT.AIRPORT_ID.eq(airportId)).fetchOneInto(Airport.class);
	}
}
