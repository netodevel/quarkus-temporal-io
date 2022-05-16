package com.accenture.temporalio.quarkus.deployment;

import com.accenture.temporalio.quarkus.runtime.GenericSupplier;
import com.accenture.temporalio.quarkus.runtime.TemporalProducer;
import com.accenture.temporalio.quarkus.runtime.annotation.SelfRegisterActivity;
import com.accenture.temporalio.quarkus.runtime.annotation.SelfRegisterWorkflow;
import com.accenture.temporalio.quarkus.runtime.metadata.TemporalBuildItem;
import io.grpc.NameResolverProvider;
import io.grpc.internal.DnsNameResolverProvider;
import io.grpc.internal.ReadableBuffers;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelProvider;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.*;
import io.temporal.activity.ActivityInterface;
import io.temporal.workflow.WorkflowInterface;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class QuarkusTemporalioProcessor {

    private static final String FEATURE = "quarkus-temporalio";

    static final DotName NAME_RESOLVER_PROVIDER = DotName.createSimple(NameResolverProvider.class.getName());

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void temporalReflections(BuildProducer<ReflectiveClassBuildItem> reflections, CombinedIndexBuildItem combinedIndex) {
        System.out.println("reflection ========");
        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(ActivityInterface.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            reflections.produce(new ReflectiveClassBuildItem(true, true, true, activityClassName));
        }

        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(WorkflowInterface.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            reflections.produce(new ReflectiveClassBuildItem(true, true, true, activityClassName));
        }
    }

    @BuildStep
    void configureProxies(BuildProducer<NativeImageProxyDefinitionBuildItem> proxies, CombinedIndexBuildItem combinedIndex) {
        System.out.println("configure proxiesss=====>");

        // proxies to async internal
        List<String> proxysToAsyncInternal = new ArrayList<>();
        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(ActivityInterface.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            proxysToAsyncInternal.add(activityClassName);
        }


        System.out.println(proxysToAsyncInternal.toString());

        proxysToAsyncInternal.add("io.temporal.internal.sync.AsyncInternal$AsyncMarker");
        proxies.produce(new NativeImageProxyDefinitionBuildItem(proxysToAsyncInternal));

        // proxies to StubMarker
        List<String> proxiesToStubMarker = new ArrayList<>();
        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(WorkflowInterface.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            proxiesToStubMarker.add(activityClassName);
        }
        System.out.println(proxiesToStubMarker.toString());

        proxiesToStubMarker.add("io.temporal.internal.sync.StubMarker");
        proxies.produce(new NativeImageProxyDefinitionBuildItem(proxiesToStubMarker));

        // temporal classes
        proxies.produce(new NativeImageProxyDefinitionBuildItem("io.temporal.serviceclient.WorkflowServiceStubs"));
        proxies.produce(new NativeImageProxyDefinitionBuildItem("io.temporal.client.WorkflowClient"));

        // app classes
        proxies.produce(new NativeImageProxyDefinitionBuildItem(proxiesToStubMarker.stream().filter(p -> !p.equalsIgnoreCase("io.temporal.internal.sync.StubMarker")).collect(Collectors.toList())));
        proxies.produce(new NativeImageProxyDefinitionBuildItem(proxysToAsyncInternal.stream().filter(p -> !p.equalsIgnoreCase("io.temporal.internal.sync.AsyncInternal$AsyncMarker")).collect(Collectors.toList())));
    }

    @BuildStep
    void registerReflecttionsNettyShaded(BuildProducer<ReflectiveClassBuildItem> reflections, CombinedIndexBuildItem combinedIndex) {
        System.out.println("reflection shaded ========");
        ReflectiveClassBuildItem buildItem = new ReflectiveClassBuildItem(true, true, true,
                "io.grpc.netty.shaded.io.netty.channel.socket.nio.NioSocketChannel",
                "io.grpc.netty.shaded.io.netty.util.internal.NativeLibraryUtil",
                "io.grpc.netty.shaded.io.netty.util.ReferenceCountUtil",
                "io.grpc.netty.shaded.io.netty.buffer.AbstractByteBufAllocator",
                "io.grpc.netty.shaded.io.netty.channel.epoll.Epoll",
                "io.grpc.netty.shaded.io.netty.channel.epoll.EpollChannelOption",
                "io.grpc.netty.shaded.io.netty.channel.epoll.EpollEventLoopGroup",
                "io.grpc.netty.shaded.io.netty.channel.epoll.EpollServerSocketChannel",
                "io.grpc.netty.shaded.io.netty.channel.epoll.EpollSocketChannel",
                "io.grpc.internal.PickFirstLoadBalancerProvider",
                "io.grpc.protobuf.services.internal.HealthCheckingRoundRobinLoadBalancerProvider");
        reflections.produce(buildItem);

        String prefixPackageDir = "io.grpc.netty.shaded.io.netty.util.internal.shaded.org.jctools.queues";
        reflections.produce(new ReflectiveClassBuildItem(true, true, true,
                prefixPackageDir + ".MpscArrayQueueProducerIndexField",
                prefixPackageDir + ".MpscArrayQueueProducerLimitField",
                prefixPackageDir + ".MpscArrayQueueConsumerIndexField",
                prefixPackageDir + ".BaseMpscLinkedArrayQueueProducerFields",
                prefixPackageDir + ".BaseMpscLinkedArrayQueueColdProducerFields",
                prefixPackageDir + ".BaseMpscLinkedArrayQueueConsumerFields",
                "io.grpc.internal.DnsNameResolverProvider"
        ));

        Collection<ClassInfo> nrs = combinedIndex.getIndex().getAllKnownSubclasses(NAME_RESOLVER_PROVIDER);
        for (ClassInfo nr : nrs) {
            reflections.produce(new ReflectiveClassBuildItem(true, true, false, nr.name().toString()));
        }

        reflections.produce(new ReflectiveClassBuildItem(true, true, true, DnsNameResolverProvider.class));
        reflections.produce(new ReflectiveClassBuildItem(true, true, false, "io.grpc.util.SecretRoundRobinLoadBalancerProvider$Provider"));
        reflections.produce(new ReflectiveClassBuildItem(true, true, false, NettyChannelProvider.class));
        reflections.produce(new ReflectiveClassBuildItem(true, true, true, ReadableBuffers.class));

    }

    @BuildStep
    void addNativeResourceForNettyShaded(BuildProducer<NativeImageResourceDirectoryBuildItem> resourceBuildItem) {
        resourceBuildItem.produce(new NativeImageResourceDirectoryBuildItem("META-INF"));
    }


    @BuildStep
    public void runTimeInitializationForNettyShaded(BuildProducer<RuntimeInitializedClassBuildItem> runtimeInitialized,
                                                    BuildProducer<RuntimeInitializedPackageBuildItem> runtimePackages) {
/*
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.OpenSsl"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.OpenSslContext"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.ReferenceCountedOpenSslEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JdkNpnApplicationProtocolNegotiator"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyAlpnSslEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyAlpnSslEngine$ClientEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyAlpnSslEngine$ServerEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyNpnSslEngine"));
*/
        runtimePackages.produce(new RuntimeInitializedPackageBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.internal.tcnative.SSL"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.internal.tcnative.CertificateVerifier"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.internal.tcnative.SSLPrivateKeyMethod"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.internal.tcnative.AsyncSSLPrivateKeyMethod"));

        runtimePackages.produce(new RuntimeInitializedPackageBuildItem("io.grpc.netty.shaded.io.grpc.netty"));
        runtimePackages.produce(new RuntimeInitializedPackageBuildItem("io.grpc.netty.shaded.io.netty.channel.epoll"));
        runtimePackages.produce(new RuntimeInitializedPackageBuildItem("io.grpc.netty.shaded.io.netty.channel.unix"));

        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.internal.RetriableStream"));
    }



    @BuildStep
    void build(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        System.out.println("build ========");
        additionalBeans.produce(AdditionalBeanBuildItem.builder().addBeanClass(TemporalProducer.class).build());
    }

    @BuildStep
    WorkflowBuildItem workflowBuildItens(BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer,
                                         CombinedIndexBuildItem combinedIndex) {
        System.out.println("build itens ========");
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
}
