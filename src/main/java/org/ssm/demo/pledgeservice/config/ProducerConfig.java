package org.ssm.demo.pledgeservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {
	@Bean public NewTopic donorInbox() {
		return new NewTopic("donor.inbox", 2, (short)1);
	}
}
