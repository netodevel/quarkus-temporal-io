package com.accenture.temporalio.quarkus.deployment;

import com.accenture.temporalio.quarkus.runtime.TemporalProducer;
import com.accenture.temporalio.quarkus.runtime.metadata.TemporalBuildItems;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;

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
        TemporalBuildItems temporalBuildItems = new TemporalBuildItems();

        SyntheticBeanBuildItem runtimeConfigBuildItem = SyntheticBeanBuildItem.configure(TemporalBuildItems.class)
                .scope(Singleton.class)
                .supplier(() -> temporalBuildItems)
                .done();

        syntheticBeanBuildItemBuildProducer.produce(runtimeConfigBuildItem);
        return new WorkflowBuildItem(temporalBuildItems);
    }
}
