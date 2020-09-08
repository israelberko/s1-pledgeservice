package org.ssm.demo.pledgeservice.applicationevents;

import org.ssm.demo.pledgeservice.entity.Pledge;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoadStateMachineEvent {
	Pledge pledge;
}
