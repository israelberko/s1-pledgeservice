CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE SCHEMA party;
CREATE TABLE party.parties (
  id uuid DEFAULT uuid_generate_v4 (),
  state varchar(100) ,
  rsvp_count int ,
  account_balance int ,
  max_attendees int ,
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
INSERT INTO party.parties (state,rsvp_count,account_balance) VALUES 
('proposed',NULL,NULL)
;