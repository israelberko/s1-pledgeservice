package org.ssm.demo.pledgeservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.ssm.demo.pledgeservice.shared.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@ToString
@Entity
@AllArgsConstructor
@Builder
public class PledgeOutbox implements BaseEntity{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) UUID id;
	UUID event_id;
	String event_type;
	String payload;
	@CreatedDate @Temporal(TemporalType.TIMESTAMP) Date created_at;
	@LastModifiedDate @Temporal(TemporalType.TIMESTAMP) Date updated_at;
	
	public static PledgeOutbox of(Map<?,?> data) {
		return new PledgeOutbox().buildFrom(data, PledgeOutbox.class);
	}

	@JsonIgnore
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

		outbox.setCreated_at(pledge.getCreated_at());
		outbox.setUpdated_at(pledge.getUpdated_at());

		return outbox;
	}
}


