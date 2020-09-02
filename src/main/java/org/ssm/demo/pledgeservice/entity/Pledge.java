package org.ssm.demo.pledgeservice.entity;

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
public class Pledge implements BaseEntity{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) UUID id;
	String state;
	Integer actual_pledged_amount;
	Integer requested_pledged_amount;
	Timestamp created_at;
	
	public static Pledge of(Map<?,?> data) {
		return new Pledge().buildFrom(data, Pledge.class);
	}
}