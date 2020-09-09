package org.ssm.demo.pledgeservice.entity;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.ssm.demo.pledgeservice.shared.BaseEntity;

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
@EntityListeners(AuditingEntityListener.class)
public class PledgeOutbox implements BaseEntity{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	UUID event_id;
	String event_type;
	String payload;
	@CreatedDate @Temporal(TemporalType.TIMESTAMP) Date created_at;
	@LastModifiedDate @Temporal(TemporalType.TIMESTAMP) Date updated_at;
	
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
		
		outbox.setEvent_type(pledge.getStatus());
		
		outbox.setPayload(pledge.toMap().toString());
		
		return outbox;
	}
}


