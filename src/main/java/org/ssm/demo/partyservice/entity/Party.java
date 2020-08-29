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
public class Party implements Entity{
	UUID id;
	String first_name;
	String last_name;
	String email;
	Timestamp created_at;
	
	public static Party of(Map<?,?> data) {
		return new Party().buildFrom(data, Party.class);
	}
}
