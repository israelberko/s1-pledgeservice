package org.ssm.demo.pledgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.ssm.demo.pledgeservice.statemachine.PledgeStateMachineConfig;

@SpringBootApplication
@Import(PledgeStateMachineConfig.class)
public class PledgeApplication 
{
	public static void main( String[] args )
    {
    	SpringApplication.run(PledgeApplication.class, args);
    }
}
