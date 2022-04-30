package com.accenture.temporalio.quarkus.deployment;

import com.accenture.temporalio.quarkus.runtime.GenericSupplier;
import com.accenture.temporalio.quarkus.runtime.TemporalProducer;
import com.accenture.temporalio.quarkus.runtime.annotation.SelfRegisterActivity;
import com.accenture.temporalio.quarkus.runtime.annotation.SelfRegisterWorkflow;
import com.accenture.temporalio.quarkus.runtime.metadata.TemporalBuildItem;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageAllowIncompleteClasspathBuildItem;
import io.quarkus.deployment.builditem.nativeimage.RuntimeInitializedClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.RuntimeInitializedPackageBuildItem;
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

        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(SelfRegisterActivity.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            temporalBuildItem.putActivity(activityClassName);
        }

        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(SelfRegisterWorkflow.class.getName()))) {
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

//    @BuildStep
//    public void configureIncompleteClassPath(BuildProducer<NativeImageAllowIncompleteClasspathBuildItem> incompleteClasspathBuildItem) {
//        incompleteClasspathBuildItem.produce(new NativeImageAllowIncompleteClasspathBuildItem(FEATURE));
//
//    }

    @BuildStep
    public void runTimeInitializationForNettyShaded(BuildProducer<RuntimeInitializedClassBuildItem> runtimeInitialized,
                                                    BuildProducer<RuntimeInitializedPackageBuildItem> runtimePackages) {
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.OpenSsl"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.OpenSslContext"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.ReferenceCountedOpenSslEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JdkNpnApplicationProtocolNegotiator"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyAlpnSslEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyAlpnSslEngine$ClientEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyAlpnSslEngine$ServerEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyNpnSslEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.internal.tcnative.SSL"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.internal.tcnative.CertificateVerifier"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.internal.tcnative.SSLPrivateKeyMethod"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.internal.tcnative.AsyncSSLPrivateKeyMethod"));

        runtimePackages.produce(new RuntimeInitializedPackageBuildItem("io.grpc.netty.shaded.io.grpc.netty"));
        runtimePackages.produce(new RuntimeInitializedPackageBuildItem("io.grpc.netty.shaded.io.netty.channel.epoll"));
        runtimePackages.produce(new RuntimeInitializedPackageBuildItem("io.grpc.netty.shaded.io.netty.channel.unix"));

        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.internal.RetriableStream"));
    }
}
