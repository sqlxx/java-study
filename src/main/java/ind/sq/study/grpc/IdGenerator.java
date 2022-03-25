package ind.sq.study.grpc;

import com.maycur.grpc.idgeneration.IdGeneration;
import com.maycur.grpc.idgeneration.IdGenerationServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.AbstractStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;

/**
 * Created by sqlxx on 2019-09-18.
 * Copyright to Maycur Tech.
 */
public class IdGenerator {
    private static final Logger logger = LoggerFactory.getLogger(IdGenerator.class);
    private IdGenerationServiceGrpc.IdGenerationServiceBlockingStub stub;

    public IdGenerator(IdGenerationServiceGrpc.IdGenerationServiceBlockingStub stub) {
        this.stub = stub;
    }

    public long genId() {
        var id = stub.getUniqueId(IdGeneration.IdRequest.getDefaultInstance());
        return id.getId();
    }
    public static void main(String[] args)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SLF4JBridgeHandler.removeHandlersForRootLogger();  // (since SLF4J 1.6.5)

        // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
        // the initialization phase of your application
        SLF4JBridgeHandler.install();
        var root = java.util.logging.Logger.getLogger("io.grpc");
        root.setLevel(Level.FINER);
        for (Handler handler : root.getHandlers()) {
            handler.setLevel(Level.FINER);
        }

//        var channelBuilder = ManagedChannelBuilder.forAddress("localhost", 9900).usePlaintext().defaultLoadBalancingPolicy();
//        var stub = IdGenerationServiceGrpc.newBlockingStub(channelBuilder.build());
//        var generator = new IdGenerator(stub);
//        logger.info("got ID: {}", generator.genId());

        GrpcClientUtil util = new GrpcClientUtil();
        var stub = util.getStubByClass(IdGenerationServiceGrpc.IdGenerationServiceBlockingStub.class);

//        for (int i = 0; i < 10; i ++) {
//            var result = stub.getUniqueId(IdGeneration.IdRequest.getDefaultInstance());
//            logger.info("got ID: {}", result.getId());
//        }
        var request = IdGeneration.BatchUniqueIdRequest.newBuilder().setCount(600).build();
        try {
            var response = stub.getBatchUniqueIds(request);
            List<Long> ids = response.getIdsList();
            for (Long id : ids) {
                logger.info("" + id);
            }
            logger.info("Total id count is {}", ids.size());
        } catch (StatusRuntimeException ex) {
            logger.error("Status Code: {}, error message: {}, (code)error message: {}", ex.getStatus().getCode(),
                    ex.getStatus().getDescription(), ex.getMessage());
            ;

        }

//        Class<? extends AbstractStub> stubClass = IdGenerationServiceGrpc.IdGenerationServiceStub.class;
//        logger.info("{}", stubClass.getTypeName());
//        logger.info("{}", stubClass.getCanonicalName());
//        logger.info("{}", stubClass.getSuperclass());
//        logger.info("{}", stubClass.getGenericSuperclass());
//        logger.info("{}", stubClass.getNestHost());
//        var hostClass = stubClass.getNestHost();
//        var method = hostClass.getMethod("newStub", Channel.class);
//        method.invoke(null, channelBuilder.build());



    }
}
