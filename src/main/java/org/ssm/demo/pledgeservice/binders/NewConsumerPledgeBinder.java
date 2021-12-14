package org.ssm.demo.pledgeservice.binders;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface NewConsumerPledgeBinder {

    // channels
    String ORDER_IN = "pledgeRequested-in-0";
    String ORDER_OUT = "pledgeRequested-out-0";

    @Input(ORDER_IN)
    SubscribableChannel orderIn();

    @Output(ORDER_OUT)
    MessageChannel orderOut();
}