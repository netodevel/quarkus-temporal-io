package com.accenture.temporalio.quarkus.deployment;

import com.accenture.temporalio.quarkus.runtime.GenericSupplier;
import com.accenture.temporalio.quarkus.runtime.TemporalProducer;
import com.accenture.temporalio.quarkus.runtime.annotation.Activity;
import com.accenture.temporalio.quarkus.runtime.annotation.Workflow;
import com.accenture.temporalio.quarkus.runtime.metadata.TemporalBuildItem;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.DotName;

import javax.inject.Singleton;

class QuarkusTemporalioProcessor {

    private static final String FEATURE = "quarkus-temporalio";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void build(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        additionalBeans.produce(AdditionalBeanBuildItem.builder().addBeanClass(TemporalProducer.class).build());
    }

    @BuildStep
    WorkflowBuildItem workflowBuildItens(BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer,
                                         CombinedIndexBuildItem combinedIndex) {
        TemporalBuildItem temporalBuildItem = new TemporalBuildItem();

        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(Activity.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            temporalBuildItem.putActivity(activityClassName);
        }

        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(Workflow.class.getName()))) {
            String workflowClassName = ai.target().asClass().name().toString();
            temporalBuildItem.putWorkflow(workflowClassName);
        }

        SyntheticBeanBuildItem runtimeConfigBuildItem = SyntheticBeanBuildItem.configure(TemporalBuildItem.class)
                .scope(Singleton.class)
                .supplier(new GenericSupplier<>(temporalBuildItem))
                .done();

        syntheticBeanBuildItemBuildProducer.produce(runtimeConfigBuildItem);
        return new WorkflowBuildItem(temporalBuildItem);
    }
}
