package org.ssm.demo.pledgeservice.applicationevents;

import org.ssm.demo.pledgeservice.entity.PledgeOutbox;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PledgeRoutedEvent {
	PledgeOutbox pledgeOutbox;
}