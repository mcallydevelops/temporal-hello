package com.mcallydevelops;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class HelloWorldWorkflowImpl implements HelloWorldWorkflow {

    ActivityOptions options = ActivityOptions.newBuilder()
            .setScheduleToCloseTimeout(Duration.ofSeconds(2))
            .build();

    // ActivityStubs enable calls to Activities as if they are local methods, but actually perform an RPC.
    private final Format format = Workflow.newActivityStub(Format.class, options);

    @Override
    public String getGreeting(String name) {
        // This is the entry point to the Workflow.
        // If there were other Activity methods they would be orchestrated here or from within other Activities.
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 10; ++i) {
            stringBuilder.append(format.composeGreeting(String.valueOf(i)));
        }
        return stringBuilder.toString();
    }
}