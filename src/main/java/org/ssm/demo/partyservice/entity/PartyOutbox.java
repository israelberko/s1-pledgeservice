package org.ssm.demo.partyservice.entity;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class PartyOutbox implements BaseEntity{
	Long id;
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
		outbox.setPayload(party.toString());
		return outbox;
	}
}


