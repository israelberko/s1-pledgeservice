package org.ssm.demo.pledgeservice.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssm.demo.pledgeservice.entity.Pledge;
import org.ssm.demo.pledgeservice.service.PledgeService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PledgeController {

    private static Logger LOG = LoggerFactory.getLogger(PledgeController.class);

    @Autowired
    private PledgeService service;
//    private PledgeRepository repository;

    @GetMapping(value = "/version")
    public Map<String, String> getVersionInfo() {
        Map<String, String> versionInfo = new HashMap<>();
        versionInfo.put("buildNumber", "0.0.1");

        return versionInfo;
    }

    @GetMapping(value = "/pledge")
    public String createRandomPledge(){

        LOG.info("Got new request for new pledge");
        Pledge pledge = new Pledge();
        pledge.setState("PLEDGE_REQUESTED");
        pledge.setRequested_pledged_amount(130);
        pledge.setActual_pledged_amount(0);

        service.savePledge(pledge);
        return "OK";
    }
}
