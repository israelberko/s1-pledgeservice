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
INSERT INTO party.parties (state,rsvp_count,account_balance) VALUES 
('proposed',NULL,NULL)
;

CREATE SCHEMA invitation;

CREATE TABLE invitation.friends (
	id serial NOT NULL,
	first_name varchar(1024) NULL,
	last_name varchar(1024) NULL,
	email varchar(1024) NULL,
	created_at timestamp default current_timestamp,
	CONSTRAINT invitees_pkey PRIMARY KEY (id)
);
;
CREATE TABLE invitation.invitations (
  id serial,
  party_id uuid,
  invitee_first_name varchar(100),
  invitee_last_name varchar(100) ,
  invitee_email varchar(100) ,
  invitee_rsvp boolean default false,
  created_at timestamp default current_timestamp,
  CONSTRAINT promotions_pkey PRIMARY KEY (id)
) 
;
CREATE TABLE invitation.invitations_outbox (
  id serial,
  event_type varchar(100) ,
  payload varchar(5000) ,
  created_at timestamp default current_timestamp,
  CONSTRAINT promotionsoutbx_pkey PRIMARY KEY (id)
) 
;

CREATE SCHEMA account;
CREATE TABLE account.accounts (
	id serial,
	party_id uuid,
	pledge int,
	created_at timestamp default current_timestamp,
	CONSTRAINT account_pkey PRIMARY key (id)
)
;

CREATE TABLE account.accounts_outbox (
  id serial,
  event uuid ,
  payload varchar(5000) ,
  created_at timestamp default current_timestamp,
  CONSTRAINT accountoutbx_pkey PRIMARY key (id)
)
;

INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Connie','Martin','c.martin@randatmail.com')
,('Daryl','Hamilton','d.hamilton@randatmail.com')
,('Audrey','Armstrong','a.armstrong@randatmail.com')
,('Lyndon','Perry','l.perry@randatmail.com')
,('Maria','Myers','m.myers@randatmail.com')
,('Adison','Hamilton','a.hamilton@randatmail.com')
,('Edwin','Warren','e.warren@randatmail.com')
,('Alan','Payne','a.payne@randatmail.com')
,('Melanie','Hill','m.hill@randatmail.com')
,('Emily','Holmes','e.holmes@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Roman','Carter','r.carter@randatmail.com')
,('Blake','Carroll','b.carroll@randatmail.com')
,('Oscar','Morrison','o.morrison@randatmail.com')
,('Albert','Taylor','a.taylor@randatmail.com')
,('Daisy','Cooper','d.cooper@randatmail.com')
,('Michael','Foster','m.foster@randatmail.com')
,('Wilson','Owens','w.owens@randatmail.com')
,('William','Harris','w.harris@randatmail.com')
,('Albert','Johnston','a.johnston@randatmail.com')
,('Madaline','Turner','m.turner@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Carina','Russell','c.russell@randatmail.com')
,('Lyndon','Harrison','l.harrison@randatmail.com')
,('Marcus','Bennett','m.bennett@randatmail.com')
,('Anna','Clark','a.clark@randatmail.com')
,('Sam','Riley','s.riley@randatmail.com')
,('Martin','Montgomery','m.montgomery@randatmail.com')
,('April','Payne','a.payne@randatmail.com')
,('Violet','Kelley','v.kelley@randatmail.com')
,('Ellia','Brown','e.brown@randatmail.com')
,('Nicole','Wright','n.wright@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Amber','Russell','a.russell@randatmail.com')
,('Maya','Richardson','m.richardson@randatmail.com')
,('Florrie','Owens','f.owens@randatmail.com')
,('Sofia','Cunningham','s.cunningham@randatmail.com')
,('Oliver','Stewart','o.stewart@randatmail.com')
,('Martin','Douglas','m.douglas@randatmail.com')
,('Adelaide','Carter','a.carter@randatmail.com')
,('Edgar','Cunningham','e.cunningham@randatmail.com')
,('Elise','Allen','e.allen@randatmail.com')
,('Chelsea','Davis','c.davis@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Alexia','Craig','a.craig@randatmail.com')
,('Deanna','Brooks','d.brooks@randatmail.com')
,('Samantha','Harrison','s.harrison@randatmail.com')
,('Catherine','Hall','c.hall@randatmail.com')
,('John','Edwards','j.edwards@randatmail.com')
,('Daryl','Davis','d.davis@randatmail.com')
,('Nicole','Ferguson','n.ferguson@randatmail.com')
,('Richard','Kelly','r.kelly@randatmail.com')
,('Maximilian','Williams','m.williams@randatmail.com')
,('Edward','Riley','e.riley@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Ned','Cameron','n.cameron@randatmail.com')
,('Justin','Ellis','j.ellis@randatmail.com')
,('Vivian','Nelson','v.nelson@randatmail.com')
,('David','Ellis','d.ellis@randatmail.com')
,('Adison','Ferguson','a.ferguson@randatmail.com')
,('Brooke','Thompson','b.thompson@randatmail.com')
,('Roman','Thompson','r.thompson@randatmail.com')
,('Alan','Moore','a.moore@randatmail.com')
,('Julian','Brooks','j.brooks@randatmail.com')
,('Cherry','Anderson','c.anderson@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Emily','Gray','e.gray@randatmail.com')
,('Sam','Harper','s.harper@randatmail.com')
,('Stella','Stewart','s.stewart@randatmail.com')
,('Jared','Craig','j.craig@randatmail.com')
,('Florrie','Phillips','f.phillips@randatmail.com')
,('Arianna','Higgins','a.higgins@randatmail.com')
,('Honey','Johnson','h.johnson@randatmail.com')
,('William','Stevens','w.stevens@randatmail.com')
,('Charlie','Thompson','c.thompson@randatmail.com')
,('Arianna','Alexander','a.alexander@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Eddy','Warren','e.warren@randatmail.com')
,('Spike','Russell','s.russell@randatmail.com')
,('Arianna','Rogers','a.rogers@randatmail.com')
,('Ryan','Murray','r.murray@randatmail.com')
,('Elian','Stewart','e.stewart@randatmail.com')
,('Paul','Moore','p.moore@randatmail.com')
,('Arthur','West','a.west@randatmail.com')
,('Lana','Payne','l.payne@randatmail.com')
,('Wilson','Lloyd','w.lloyd@randatmail.com')
,('Lyndon','Armstrong','l.armstrong@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Paige','Lloyd','p.lloyd@randatmail.com')
,('Aida','Stevens','a.stevens@randatmail.com')
,('Haris','Evans','h.evans@randatmail.com')
,('Amelia','Morrison','a.morrison@randatmail.com')
,('Maddie','Taylor','m.taylor@randatmail.com')
,('Michael','Murphy','m.murphy@randatmail.com')
,('Lenny','Hawkins','l.hawkins@randatmail.com')
,('Dale','Morgan','d.morgan@randatmail.com')
,('Charlie','Johnston','c.johnston@randatmail.com')
,('Brad','West','b.west@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Nicholas','Tucker','n.tucker@randatmail.com')
,('Catherine','Jones','c.jones@randatmail.com')
,('Carlos','Harris','c.harris@randatmail.com')
,('Kelvin','Henderson','k.henderson@randatmail.com')
,('Sarah','Turner','s.turner@randatmail.com')
,('Aiden','Roberts','a.roberts@randatmail.com')
,('Elise','Spencer','e.spencer@randatmail.com')
,('Arianna','Gray','a.gray@randatmail.com')
,('Maria','Gray','m.gray@randatmail.com')
,('Eric','Alexander','e.alexander@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Catherine','Elliott','c.elliott@randatmail.com')
,('Ashton','Farrell','a.farrell@randatmail.com')
,('Ned','Watson','n.watson@randatmail.com')
,('Dale','Clark','d.clark@randatmail.com')
,('Charlotte','Bennett','c.bennett@randatmail.com')
,('Albert','Mitchell','a.mitchell@randatmail.com')
,('Sofia','Edwards','s.edwards@randatmail.com')
,('Kristian','Carroll','k.carroll@randatmail.com')
,('Michael','Parker','m.parker@randatmail.com')
,('Arianna','Bennett','a.bennett@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Mary','Hawkins','m.hawkins@randatmail.com')
,('Roman','Edwards','r.edwards@randatmail.com')
,('Derek','Carroll','d.carroll@randatmail.com')
,('Chloe','Cole','c.cole@randatmail.com')
,('Amber','Myers','a.myers@randatmail.com')
,('Jasmine','Foster','j.foster@randatmail.com')
,('Caroline','Carroll','c.carroll@randatmail.com')
,('Ryan','Carroll','r.carroll@randatmail.com')
,('Maddie','Grant','m.grant@randatmail.com')
,('Jordan','Lloyd','j.lloyd@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Andrew','Thompson','a.thompson@randatmail.com')
,('Honey','Henderson','h.henderson@randatmail.com')
,('Annabella','Armstrong','a.armstrong@randatmail.com')
,('George','Carroll','g.carroll@randatmail.com')
,('Belinda','Roberts','b.roberts@randatmail.com')
,('Maya','Stewart','m.stewart@randatmail.com')
,('Melanie','Clark','m.clark@randatmail.com')
,('Agata','Hamilton','a.hamilton@randatmail.com')
,('Abraham','Morris','a.morris@randatmail.com')
,('Justin','Tucker','j.tucker@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Ted','Douglas','t.douglas@randatmail.com')
,('Nicole','Morgan','n.morgan@randatmail.com')
,('Eddy','Ross','e.ross@randatmail.com')
,('Nicholas','Harper','n.harper@randatmail.com')
,('Dexter','Barnes','d.barnes@randatmail.com')
,('Derek','Kelley','d.kelley@randatmail.com')
,('Kellan','Brooks','k.brooks@randatmail.com')
,('Mike','Richards','m.richards@randatmail.com')
,('Justin','Wright','j.wright@randatmail.com')
,('Edward','Parker','e.parker@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Charlotte','Casey','c.casey@randatmail.com')
,('Abigail','Stevens','a.stevens@randatmail.com')
,('Lana','Perry','l.perry@randatmail.com')
,('Adam','Harper','a.harper@randatmail.com')
,('Ellia','Henderson','e.henderson@randatmail.com')
,('April','Harris','a.harris@randatmail.com')
,('Joyce','Alexander','j.alexander@randatmail.com')
,('Luke','Murray','l.murray@randatmail.com')
,('Kate','Hawkins','k.hawkins@randatmail.com')
,('Rebecca','Higgins','r.higgins@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Walter','Johnston','w.johnston@randatmail.com')
,('Wilson','Reed','w.reed@randatmail.com')
,('Myra','Barrett','m.barrett@randatmail.com')
,('Tony','Andrews','t.andrews@randatmail.com')
,('George','Evans','g.evans@randatmail.com')
,('Justin','Scott','j.scott@randatmail.com')
,('Deanna','Ross','d.ross@randatmail.com')
,('Nicholas','Ross','n.ross@randatmail.com')
,('Ada','Bailey','a.bailey@randatmail.com')
,('Dale','Hill','d.hill@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Victoria','Foster','v.foster@randatmail.com')
,('Lilianna','Grant','l.grant@randatmail.com')
,('Dominik','Montgomery','d.montgomery@randatmail.com')
,('Miranda','Reed','m.reed@randatmail.com')
,('Luke','Harper','l.harper@randatmail.com')
,('Antony','Wells','a.wells@randatmail.com')
,('Alen','Scott','a.scott@randatmail.com')
,('Martin','Campbell','m.campbell@randatmail.com')
,('Eric','Robinson','e.robinson@randatmail.com')
,('Tess','Elliott','t.elliott@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Martin','Howard','m.howard@randatmail.com')
,('Belinda','Miller','b.miller@randatmail.com')
,('Lucia','Johnson','l.johnson@randatmail.com')
,('Ada','Stewart','a.stewart@randatmail.com')
,('Lydia','Warren','l.warren@randatmail.com')
,('Sam','Clark','s.clark@randatmail.com')
,('Lucas','Allen','l.allen@randatmail.com')
,('Blake','Mason','b.mason@randatmail.com')
,('Grace','Evans','g.evans@randatmail.com')
,('Sofia','Kelley','s.kelley@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Deanna','Kelly','d.kelly@randatmail.com')
,('Briony','Smith','b.smith@randatmail.com')
,('Alberta','Johnson','a.johnson@randatmail.com')
,('Isabella','Montgomery','i.montgomery@randatmail.com')
,('Isabella','Roberts','i.roberts@randatmail.com')
,('Maya','Mitchell','m.mitchell@randatmail.com')
,('Melissa','Elliott','m.elliott@randatmail.com')
,('Adam','Roberts','a.roberts@randatmail.com')
,('Deanna','Watson','d.watson@randatmail.com')
,('Sydney','Taylor','s.taylor@randatmail.com')
;
INSERT INTO invitation.friends (first_name,last_name,email) VALUES 
('Cherry','Crawford','c.crawford@randatmail.com')
,('Roman','Clark','r.clark@randatmail.com')
,('Grace','Carroll','g.carroll@randatmail.com')
,('Alfred','Chapman','a.chapman@randatmail.com')
,('Kelvin','Farrell','k.farrell@randatmail.com')
,('Lana','Gray','l.gray@randatmail.com')
,('Ellia','Bailey','e.bailey@randatmail.com')
,('Kelvin','Murray','k.murray@randatmail.com')
,('Julia','Parker','j.parker@randatmail.com')
,('John','Nelson','j.nelson@randatmail.com')
;