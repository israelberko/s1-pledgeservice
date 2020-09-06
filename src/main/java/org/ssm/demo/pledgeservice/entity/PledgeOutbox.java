package org.ssm.demo.pledgeservice.entity;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
@AllArgsConstructor
@Builder
public class PledgeOutbox implements BaseEntity{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	UUID event_id;
	String event_type;
	String payload;
	Timestamp created_at;
	
	public static PledgeOutbox of(Map<?,?> data) {
		return new PledgeOutbox().buildFrom(data, PledgeOutbox.class);
	}
	
	public Map<?,?> getPayloadAsMap() {
		return Splitter.on(",")
				.trimResults(CharMatcher.anyOf("{} "))
				.withKeyValueSeparator("=")
				.split(payload);
	}
	
	public static PledgeOutbox from(Pledge pledge) {
		PledgeOutbox outbox = new PledgeOutbox();
		outbox.setEvent_id(pledge.getId());
		outbox.setEvent_type(pledge.getState());
		outbox.setPayload(pledge.toMap().toString());
		return outbox;
	}
}


