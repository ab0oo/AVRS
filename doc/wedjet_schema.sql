--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: users; Type: TABLE; Schema: public; Owner: johng; Tablespace: 
--

CREATE TABLE users (
    user_id SERIAL NOT NULL UNIQUE,
    username varchar(12) not null UNIQUE,
    password varchar(20) not null,
    create_time timestamp with time zone not null default now(),
    timezone varchar not null default 'GMT'
);

--
-- Name: monitored_stations; Type: TABLE; Schema: public; Owner: johng; Tablespace: 
--

CREATE TABLE monitored_stations (
    station_id SERIAL NOT NULL UNIQUE,
    user_id integer NOT NULL REFERENCES users(user_id),
    callsign varchar(12) NOT NULL
);

CREATE INDEX monitored_stations_callsign_idx ON monitored_stations(callsign);

--
-- Name: notification_addresses; Type: TABLE; Schema: public; Owner: johng; Tablespace: 
--

CREATE TABLE notification_addresses (
    na_id SERIAL NOT NULL UNIQUE,
    user_id integer NOT NULL REFERENCES users(user_id),
    address character varying(30) NOT NULL,
    primary_address boolean DEFAULT false,
    description varchar,
    short_form boolean DEFAULT true
);

--
-- Name: rules; Type: TABLE; Schema: public; Owner: johng; Tablespace: 
--

CREATE TABLE rules (
    rule_id SERIAL NOT NULL UNIQUE,
    user_id integer NOT NULL REFERENCES users(user_id),
    station_id integer NOT NULL REFERENCES monitored_stations(station_id),
    ruletype VARCHAR(10) NOT NULL,
    cycle_time integer NOT NULL default 5,
    next_enabled timestamp with time zone default now()-interval '1 day'
);

--
-- Name: notifications; Type: TABLE; Schema: public; Owner: johng; Tablespace: 
--

CREATE TABLE notifications (
    n_id SERIAL NOT NULL UNIQUE,
    user_id integer NOT NULL REFERENCES users(user_id),
    start_time integer default 0,
    end_time integer default 1440,
    valid_days integer default 127,
    rule_id integer NOT NULL REFERENCES rules(rule_id),
    na_id integer NOT NULL REFERENCES notification_addresses(na_id)
);

--
-- Name: zones; Type: TABLE; Schema: public; Owner: johng; Tablespace: 
--

CREATE TABLE zones (
    zone_id SERIAL NOT NULL UNIQUE,
    user_id integer NOT NULL REFERENCES users(user_id),
    point_radius integer
);
SELECT AddGeometryColumn('','zones','point_geom',4326,'POINT',2);
SELECT AddGeometryColumn('','zones','poly_geom',4326,'POLYGON',2);

