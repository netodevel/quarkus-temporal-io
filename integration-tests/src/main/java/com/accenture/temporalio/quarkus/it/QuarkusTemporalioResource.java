/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.accenture.temporalio.quarkus.it;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.UUID;

@Path("/hello")
@ApplicationScoped
public class QuarkusTemporalioResource {

    @Inject
    WorkflowClient workflowClient;

    @GET
    public String runWorkflowPureWay() {
        var workflow = workflowClient.newWorkflowStub(GreetingWorkflow.class, WorkflowOptions.newBuilder()
                .setWorkflowId(UUID.randomUUID().toString())
                .setTaskQueue("quarkus-temporal-worker") // queue-name should be equal a workflow
                .build());
        return workflow.getGreeting("World");
    }

}
