package com.project.FlightReservation.domain.repository;

import static com.project.FlightReservation.domain.dao.Tables.AIRPORT;
import static com.project.FlightReservation.domain.dao.Tables.SEAT_PRICING;

import java.math.BigDecimal;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.FlightReservation.domain.models.schedule.SeatPricing;

@Repository
public class PricingRepository
{
	@Autowired
	DSLContext dsl;

	public long addPricing(SeatPricing seatPricing, long flightScheduleId)
	{
		return DSL.using(dsl.configuration()).insertInto(SEAT_PRICING).
			set(SEAT_PRICING.BUSINESS_PRICE, seatPricing.getBusinessPrice()).
			set(SEAT_PRICING.ECONOMY_PRICE, seatPricing.getEconomyPrice()).
			set(SEAT_PRICING.FIRST_CLASS_PRICE, seatPricing.getFirstClassPrice()).
			set(SEAT_PRICING.FLIGHT_SCHEDULE_ID, flightScheduleId).execute();
	}

	public long updatePricing(SeatPricing seatPricing, long flightScheduleId)
	{
		Condition condition = SEAT_PRICING.FLIGHT_SCHEDULE_ID.eq(flightScheduleId);
		UpdateSetMoreStep<?> updateQuery = (UpdateSetMoreStep<?>) DSL.using(dsl.configuration()).update(SEAT_PRICING);
		if(Optional.ofNullable(seatPricing.getBusinessPrice()).isPresent())
		{
			updateQuery.set(SEAT_PRICING.BUSINESS_PRICE, seatPricing.getBusinessPrice());
		}
		if(Optional.ofNullable(seatPricing.getEconomyPrice()).isPresent())
		{
			updateQuery.set(SEAT_PRICING.ECONOMY_PRICE, seatPricing.getEconomyPrice());
		}
		if(Optional.ofNullable(seatPricing.getFirstClassPrice()).isPresent())
		{
			updateQuery.set(SEAT_PRICING.FIRST_CLASS_PRICE, seatPricing.getFirstClassPrice());
		}
		return updateQuery.where(condition).execute();
	}
}
