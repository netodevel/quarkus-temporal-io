package com.accenture.temporalio.quarkus.test;

import io.quarkus.test.QuarkusDevModeTest;
import io.temporal.client.WorkflowClient;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.enterprise.inject.spi.CDI;

public class QuarkusTemporalioDevModeTest {

    // Start hot reload (DevMode) test with your extension loaded
    @RegisterExtension
    static final QuarkusDevModeTest devModeTest = new QuarkusDevModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Test
    @Disabled
    public void shouldInjectTemporalBeans() {
        WorkflowClient workflowClient = CDI.current().select(WorkflowClient.class).get();
        Assertions.assertNotNull(workflowClient);
    }
}
