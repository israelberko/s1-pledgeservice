package org.ssm.demo.pledgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PledgeApplication 
{
	public static void main( String[] args )
    {
    	SpringApplication.run(PledgeApplication.class, args);
    }
}
