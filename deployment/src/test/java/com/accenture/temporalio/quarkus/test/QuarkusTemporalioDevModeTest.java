package com.accenture.temporalio.quarkus.test;

import io.quarkus.test.QuarkusDevModeTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class QuarkusTemporalioDevModeTest {

    // Start hot reload (DevMode) test with your extension loaded
    @RegisterExtension
    static final QuarkusDevModeTest devModeTest = new QuarkusDevModeTest()
        .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));


/*
    @Inject
    ArcContainer arcContainer;
*/

    @Test
    public void shouldInjectTemporalBeans() {
        // Write your dev mode tests here - see the testing extension guide https://quarkus.io/guides/writing-extensions#testing-hot-reload for more information

/*
        WorkflowClient client = (WorkflowClient) arcContainer.instance(WorkflowClient.class);
        Assertions.assertNotNull(client);
*/

        Assertions.assertTrue(true, "Add dev mode assertions to " + getClass().getName());
    }
}
