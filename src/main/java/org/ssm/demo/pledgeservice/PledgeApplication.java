package org.ssm.demo.pledgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableAsync;
import org.ssm.demo.pledgeservice.binders.NewConsumerPledgeBinder;
import org.ssm.demo.pledgeservice.binders.PledgeConsumerBinder;

@SpringBootApplication
@EnableBinding(value = {NewConsumerPledgeBinder.class, PledgeConsumerBinder.class})
@EnableAsync
public class PledgeApplication 
{
	public static void main( String[] args )
    {
    	SpringApplication.run(PledgeApplication.class, args);
    }
}
