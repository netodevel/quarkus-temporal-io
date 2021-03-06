package com.accenture.temporalio.quarkus.runtime;

import com.accenture.temporalio.quarkus.runtime.annotation.Workflow;
import com.accenture.temporalio.quarkus.runtime.metadata.TemporalBuildItem;
import io.quarkus.arc.Arc;
import io.quarkus.arc.Unremovable;
import io.quarkus.runtime.Startup;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@Unremovable
public class TemporalProducer {

    private static final Logger LOGGER = Logger.getLogger(TemporalProducer.class);

    @Produces
    @ApplicationScoped
    @Unremovable
    WorkflowServiceStubs workflowService() {
        return WorkflowServiceStubs.newInstance();
    }

    @Produces
    @ApplicationScoped
    @Unremovable
    WorkflowClient workflowClient(WorkflowServiceStubs service) {
        return WorkflowClient.newInstance(service);
    }

    @Produces
    @ApplicationScoped
    @Startup
    @Unremovable
    public WorkerFactory temporalWorker(WorkflowClient workflowClient, TemporalBuildItem workflowBuildItems) {
        var factory =  WorkerFactory.newInstance(workflowClient);
        var worker = factory.newWorker("quarkus-temporal-worker");
        var classLoader = Thread.currentThread().getContextClassLoader();

        System.out.println("recording workflows");
        System.out.println("workflows: " + workflowBuildItems.getListOfWorkflows().toString());
        for (String clazzName : workflowBuildItems.getListOfWorkflows()) {
            try {
                Class<?> clazz = classLoader.loadClass(clazzName);
                Workflow workflowAnnotation = clazz.getAnnotation(Workflow.class);

                System.out.println("queue_name: " + workflowAnnotation.queue());

                worker.registerWorkflowImplementationTypes(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        System.out.println("recording activities...");
        System.out.println("activities: " + workflowBuildItems.getListOfActivities().toString());

        for (String clazzName : workflowBuildItems.getListOfActivities()) {
            try {
                Class<?> clazz = classLoader.loadClass(clazzName);
                System.out.println("name: " + clazz.getName());
                for (Class<?> interfaces : clazz.getInterfaces()) {
                    System.out.println("interface: " + interfaces.getName());
                }

                //how to register to arc container
                Object activityInjectedToRegister = Arc.container().select(clazz).get();
                System.out.println(activityInjectedToRegister.toString());
                worker.registerActivitiesImplementations(activityInjectedToRegister);

            } catch (Exception e) {
                System.out.println("error = " + e.getMessage());
                System.out.println("cause = " + e.getCause().toString());
            }
        }

        factory.start();
        LOGGER.info("worker started");
        return factory;
    }
}
