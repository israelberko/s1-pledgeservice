package org.ssm.demo.pledgeservice.statemachine;

public enum PledgeStates {
    /* Sub states */
    PLEDGE_REQUESTED, PLEDGE_REQUESTED_PENDING, PLEDGE_MATCHED, PLEDGE_CANCELLED, PLEDGE_CANCEL_REQUESTED, PLEDGE_HISTORY,
    /* Parent states */
    IN_PROGRESS, SUSPENDED, COMPLETED
}
