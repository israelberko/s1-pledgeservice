package org.ssm.demo.pledgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PledgeApplication 
{
	public static void main( String[] args )
    {
    	SpringApplication.run(PledgeApplication.class, args);
    }
}
