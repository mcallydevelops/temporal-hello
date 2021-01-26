package com.mcallydevelops;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class Application {

    public static void main(String[] args) {
        Thread one = new Thread(() -> {
            // This gRPC stubs wrapper talks to the local docker instance of the Temporal service.
            WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
            WorkflowClient client = WorkflowClient.newInstance(service);
            // Create a Worker factory that can be used to create Workers that poll specific Task Queues.
            WorkerFactory factory = WorkerFactory.newInstance(client);
            Worker worker = factory.newWorker(Shared.HELLO_WORLD_TASK_QUEUE);
            // This Worker hosts both Workflow and Activity implementations.
            // Workflows are stateful, so you need to supply a type to create instances.
            worker.registerWorkflowImplementationTypes(HelloWorldWorkflowImpl.class);
            // Activities are stateless and thread safe, so a shared instance is used.
            worker.registerActivitiesImplementations(new FormatImpl());
            // Start polling the Task Queue.
            factory.start();
        });

        Thread two = new Thread(() -> {
            WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
            // WorkflowClient can be used to start, signal, query, cancel, and terminate Workflows.
            WorkflowClient client = WorkflowClient.newInstance(service);
            WorkflowOptions options = WorkflowOptions.newBuilder()
                    .setTaskQueue(Shared.HELLO_WORLD_TASK_QUEUE)
                    .build();
            // WorkflowStubs enable calls to methods as if the Workflow object is local, but actually perform an RPC.
            HelloWorldWorkflow workflow = client.newWorkflowStub(HelloWorldWorkflow.class, options);
            // Synchronously execute the Workflow and wait for the response.
            String greeting = workflow.getGreeting("World");
        });
        one.start();
        two.start();

    }
}
