package com.accenture.temporalio.quarkus.runtime;

import com.accenture.temporalio.quarkus.runtime.metadata.TemporalBuildItems;
import io.quarkus.arc.DefaultBean;
import io.quarkus.runtime.Startup;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class TemporalProducer {

    private static final Logger LOGGER = Logger.getLogger(TemporalProducer.class);

    @Produces
    @DefaultBean
    WorkflowServiceStubs workflowService() {
        return WorkflowServiceStubs.newInstance();
    }

    @Produces
    @DefaultBean
    WorkflowClient workflowClient(WorkflowServiceStubs service) {
        return WorkflowClient.newInstance(service);
    }

    @Produces
    @DefaultBean
    WorkerFactory workerFactory(WorkflowClient client) {
        return WorkerFactory.newInstance(client);
    }

    @Produces
    @Startup
    public Worker temporalWorker(WorkerFactory workerFactory, TemporalBuildItems worklowBuildItens) {
        // Worker that listens on a task queue and hosts both workflow and activity implementations.
        var worker = workerFactory.newWorker("quarkus-temporal-worker");

        System.out.println(worklowBuildItens);

        // Workflows are stateful. So you need a type to create instances.
/*
        worker.registerWorkflowImplementationTypes(TripBookingWorkflowImpl.class);
*/
/*
        System.out.println(trip.reserveCar("xpto"));
*/
/*
        worker.registerActivitiesImplementations(new TripBookingActivitiesImpl());
*/
        // Start all workers created by this factory.

        workerFactory.start();
        LOGGER.info("worker started");
        return worker;
    }
}
