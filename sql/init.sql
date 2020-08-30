CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
GRANT CREATE,USAGE ON SCHEMA party TO postgres; 
alter USER postgres set search_path to party, postgres;

CREATE SCHEMA party;
CREATE TABLE party.party (
  id uuid DEFAULT uuid_generate_v4 (),
  state varchar(100) ,
  rsvp_count int default 0,
  account_balance int ,
  max_attendees int default 0,
  created_at timestamp default current_timestamp,
   CONSTRAINT party_pkey PRIMARY KEY (id)
)
;
CREATE TABLE party.party_outbox (
  id serial,
  event_id uuid,
  event_type varchar(100) ,
  payload varchar(5000) ,
  created_at timestamp default current_timestamp,
  CONSTRAINT party_outbox_pkey PRIMARY KEY (id)
)
;
INSERT INTO party.party (state,rsvp_count,account_balance) VALUES 
('proposed',NULL,NULL)
;
