CREATE SCHEMA flight_reservation;

CREATE TYPE flight_reservation.flight_status AS ENUM ('SCHEDULED' , 'BOARDING' , 'SECURITY', 'DELAYED', 'REACHED', 'IN_TRANSIT', 'CANCELLED');
CREATE TYPE flight_reservation.seat_type AS ENUM ('BUSINESS' , 'FIRST_CLASS' , 'ECONOMY');

CREATE TABLE flight_reservation.airlines
(
  airline_id bigserial NOT NULL,
  name text,
  airline_code text,
  CONSTRAINT airlines_pk PRIMARY KEY (airline_id),
  CONSTRAINT airlines_unique UNIQUE (airline_code),
);


 CREATE TABLE flight_reservation.seat_pricing
 (
    seat_pricing_id bigserial NOT NULL,
    prices jsonb DEFAULT '{}'::jsonb,
    CONSTRAINT seat_pricing_pk PRIMARY KEY (seat_pricing_id)

 );

CREATE TABLE flight_reservation.airport
(
  airport_id bigserial NOT NULL,
  name text,
  airport_code text,
  latitude numeric(20,18),
  longitude numeric(20,18),
  CONSTRAINT airport_pk PRIMARY KEY (airport_id)
);

CREATE TABLE flight_reservation.flight_schedule
(
  flight_schedule_id bigserial NOT NULL,
  airline_id bigserial NOT NULL,
  to_airport bigserial NOT NULL,
  from_airport bigserial NOT NULL,
  departure_datetime timestamp with time zone NOT NULL DEFAULT now(),
  arrival_datetime timestamp with time zone NOT NULL DEFAULT now(),
  base_price text NOT NULL,
  status flight_status NOT NULL,
  seat_pricing bigserial NOT NULL,
  CONSTRAINT flight_schedule_pk PRIMARY KEY (flight_schedule_id),
  CONSTRAINT to_airport_fk FOREIGN KEY (to_airport)
        REFERENCES flight_reservation.airport (airport_id)
        MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT from_airport_fk FOREIGN KEY (from_airport)
          REFERENCES flight_reservation.airport (airport_id)
          MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT flight_schedule_airline_fk FOREIGN KEY (airline_id)
            REFERENCES flight_reservation.airlines (airline_id)
            MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT flight_schedule_price_fk FOREIGN KEY (seat_pricing)
                REFERENCES flight_reservation.seat_pricing (seat_pricing_id)
                        MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
  );



CREATE TABLE flight_reservation.seats
(
  seat_id bigserial NOT NULL,
  airline_id bigserial NOT NULL,
  seat_type flight_reservation.seat_type NOT NULL,
  seat_no text NOT NULL,
  CONSTRAINT seat_pk PRIMARY KEY (seat_id),
  CONSTRAINT seats_airline_fk FOREIGN KEY (airline_id)
              REFERENCES flight_reservation.airlines (airline_id)
              MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
 );










