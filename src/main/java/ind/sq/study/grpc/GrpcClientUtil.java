package ind.sq.study.grpc;

import com.google.protobuf.ServiceException;
import io.grpc.*;
import io.grpc.stub.AbstractStub;
import io.grpc.stub.MetadataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sqlxx on 2019-09-18.
 * Copyright to Maycur Tech.
 */
public class GrpcClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(GrpcClientUtil.class);

    private static final String GRPC_PACKAGE_PREFIX = "com.maycur.grpc";
    private static final int GRPC_PACKAGE_PREFIX_LENGTH = GRPC_PACKAGE_PREFIX.length();
    private static final String SERVICE_SUFFIX = "service";

    private enum StubType {
        BLOCKING("newBlockingStub"), ASYNC("newStub"), FUTURE("newFutureStub");

        StubType(String methodName) {
            this.methodName = methodName;
        }

        private String methodName;

        public String getMethodName() {
            return methodName;
        }

    }

    private final Map<String, ManagedChannel> channelMap = new ConcurrentHashMap<>();

    public <T extends AbstractStub> T getStubByClass(Class<T> stubClass) {

        var channel = getChannel(inferServiceName(stubClass));

        var stubType = inferStubType(stubClass.getSimpleName());

        var hostClass = stubClass.getNestHost();

        try {
            Method method = hostClass.getMethod(stubType.getMethodName(), Channel.class);
            var result = method.invoke(null, channel);
            return (T) result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            logger.error("Can't create stub class {} because {}", stubClass, ex.getMessage(), ex);
            throw new RuntimeException("Can't create stub class " + stubClass.getCanonicalName());
        }
    }


    public ManagedChannel getChannel(String serviceName) {
        if (!channelMap.containsKey(serviceName)) {
            createChannel(serviceName);
        }

        return channelMap.get(serviceName);
    }

    private StubType inferStubType(String className) {
        if (className.endsWith("BlockingStub")) {
            return StubType.BLOCKING;
        } else if (className.endsWith("FutureStub")) {
            return StubType.FUTURE;
        } else if (className.endsWith("Stub")) {
            return StubType.ASYNC;
        } else {
            throw new InvalidParameterException("Not a valid stub class.");
        }
    }

    private <T extends AbstractStub> String inferServiceName(Class<T> stubClass) {
        var packageName = stubClass.getPackageName();
        logger.debug("Package name is {}", packageName);
        String serviceName;
        if (packageName.indexOf(GRPC_PACKAGE_PREFIX) == 0 ) {
            var servicePrefix = packageName.substring(GRPC_PACKAGE_PREFIX_LENGTH + 1, calculateEndIndex(packageName));
            serviceName = servicePrefix + SERVICE_SUFFIX;
            logger.debug("ServiceName is {}", serviceName);

            return serviceName;
        } else {
            throw new InvalidParameterException("Not a valid package name, should start with com.maycur.grpc");
        }
    }

    private int calculateEndIndex(String packageName) {
        var endIndex = packageName.indexOf(".", GRPC_PACKAGE_PREFIX_LENGTH + 1);
        endIndex = endIndex == -1 ? packageName.length(): endIndex;

        logger.debug("End index is {}", endIndex);
        return endIndex;
    }


    private synchronized void createChannel(String serviceName) {
        if (channelMap.containsKey(serviceName)) {
            return;
        }

        System.setProperty("io.grpc.internal.DnsNameResolverProvider.enable_grpclb", "true");

//        var channelBuilder = ManagedChannelBuilder.forTarget("dns:///" + serviceName.toLowerCase());
        var channelBuilder = ManagedChannelBuilder.forAddress("localhost", 9900).usePlaintext();
        channelBuilder.maxRetryAttempts(3).enableRetry();
        var channel = channelBuilder.build();

        channelMap.put(serviceName, channel);
    }


}
