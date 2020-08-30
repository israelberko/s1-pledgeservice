package org.ssm.demo.partyservice.applicationevents;

import org.ssm.demo.partyservice.entity.PartyOutbox;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class SendOutboxEvent {
	PartyOutbox partyOutbox;
}
