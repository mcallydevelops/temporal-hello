package com.mcallydevelops;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SampleController {

    private final WorkflowOptions options;
    private final WorkflowClient client;

    public SampleController(WorkflowOptions options, WorkflowClient workflowClient) {
        this.options = options;
        this.client = workflowClient;
    }

    @GetMapping
    public String get() {

        // WorkflowStubs enable calls to methods as if the Workflow object is local, but actually perform an RPC.
        HelloWorldWorkflow workflow = client.newWorkflowStub(HelloWorldWorkflow.class, options);
        // Synchronously execute the Workflow and wait for the response.

        String greeting = workflow.getGreeting("World");
        return greeting;
    }
}
