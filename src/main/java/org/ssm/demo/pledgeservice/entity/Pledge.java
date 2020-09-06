package org.ssm.demo.pledgeservice.entity;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.core.type.TypeReference;

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
	Integer actual_pledged_amount = 0;
	Integer requested_pledged_amount = 0;
	Timestamp created_at;
	
	public static Pledge of(Map<?,?> data) {
		return new Pledge().buildFrom(data, Pledge.class);
	}
	
	public void addToActualAmount(Integer amount) {
		this.actual_pledged_amount += amount;
	}
	
	public void resetActualAmount() {
		this.actual_pledged_amount = 0;
	}
}
