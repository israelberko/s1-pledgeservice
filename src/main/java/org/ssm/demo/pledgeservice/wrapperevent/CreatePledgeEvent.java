package org.ssm.demo.pledgeservice.wrapperevent;

import lombok.Data;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CreatePledgeEvent<T> {
	T outbox;
}
