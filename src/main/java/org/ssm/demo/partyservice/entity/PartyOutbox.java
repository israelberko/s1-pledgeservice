package org.ssm.demo.partyservice.entity;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
public class PartyOutbox implements BaseEntity{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	UUID event_id;
	String event_type;
	String payload;
	Timestamp created_at;
	
	public static PartyOutbox of(Map<?,?> data) {
		return new PartyOutbox().buildFrom(data, PartyOutbox.class);
	}
	
	public static PartyOutbox from(Party party) {
		PartyOutbox outbox = new PartyOutbox();
		outbox.setEvent_id(party.getId());
		outbox.setEvent_type(party.getState());
		outbox.setPayload(party.toMap().toString());
		return outbox;
	}
}


