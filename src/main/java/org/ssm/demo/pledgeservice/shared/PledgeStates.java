package org.ssm.demo.pledgeservice.shared;

public enum PledgeStates {
	/* Sub states */
	PLEDGE_REQUESTED, PLEDGE_MATCHED, PLEDGE_CANCELLED, PLEDGE_CANCEL_REQUESTED, PLEDGE_HISTORY,
	/* Parent states */
	IN_PROGRESS, SUSPENDED, COMPLETED
}
