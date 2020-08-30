package org.ssm.demo.partyservice.entity;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import javax.persistence.GenerationType;

import javax.persistence.GeneratedValue;

import javax.persistence.Id;

import javax.persistence.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
public class Party implements BaseEntity{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) UUID id;
	String state;
	Integer rsvp_count;
	Integer max_attendees;
	String account_balance;
	Timestamp created_at;
	
	public static Party of(Map<?,?> data) {
		return new Party().buildFrom(data, Party.class);
	}
}
