package org.ssm.demo.pledgeservice.applicationevents;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommandMessage<T> {
	T command;
}
