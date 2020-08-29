package org.ssm.demo.partyservice.entity;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Customer implements Entity{
	Long id;
	String first_name;
	String last_name;
	String email;
	
	public static Customer of(Map<?,?> data) {
		return new Customer().withAfterField(data, Customer.class);
	}
}
