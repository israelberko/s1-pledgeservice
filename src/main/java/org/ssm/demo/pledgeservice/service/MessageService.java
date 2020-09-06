package org.ssm.demo.pledgeservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.ssm.demo.pledgeservice.applicationevents.CommandMessage;
import org.ssm.demo.pledgeservice.entity.PledgeOutbox;

@Service
public class MessageService {
	
	Logger LOG = LoggerFactory.getLogger(MessageService.class);
	
	@EventListener(classes = CommandMessage.class)
	public void sendCommand(CommandMessage<PledgeOutbox> message) {
		this.sendToDestination(message);
	}
	
	@SendTo("donor.inbox")
	public PledgeOutbox sendToDestination(CommandMessage<PledgeOutbox> message) {
		LOG.info("About to send...{}", message.getCommand());
		return message.getCommand();
	}

}
