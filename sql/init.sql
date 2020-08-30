CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE SCHEMA party;
CREATE TABLE party.parties (
  id uuid DEFAULT uuid_generate_v4 (),
  state varchar(100) ,
  rsvp_count int default 0,
  account_balance int ,
  max_attendees int default 0,
  created_at timestamp default current_timestamp,
   CONSTRAINT parties_pkey PRIMARY KEY (id)
)
;
CREATE TABLE party.parties_outbox (
  id serial,
  event_id uuid,
  event_type varchar(100) ,
  payload varchar(5000) ,
  created_at timestamp default current_timestamp,
  CONSTRAINT parties_outbox_pkey PRIMARY KEY (id)
)
;