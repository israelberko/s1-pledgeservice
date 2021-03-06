CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE SCHEMA pledge;
GRANT CREATE,USAGE ON SCHEMA pledge TO postgres; 
alter USER postgres set search_path to pledge, donor, postgres;


CREATE TABLE pledge.pledge (
  id uuid DEFAULT public.uuid_generate_v4 (),
  status varchar(100) ,
  actual_pledged_amount int default 0,
  requested_pledged_amount int default 0,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp,
   CONSTRAINT party_pkey PRIMARY KEY (id)
)
;
CREATE TABLE pledge.pledge_outbox (
  id serial,
  event_id uuid,
  event_type varchar(100) ,
  payload varchar(5000) ,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp,
  CONSTRAINT party_outbox_pkey PRIMARY KEY (id)
)
;