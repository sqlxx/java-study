package ind.sq.study.grpc;

import com.maycur.common.grpc.HealthChecker;
import grpc.health.v1.HealthGrpc;
import grpc.health.v1.Healthcheck;
import ind.sq.study.grpc.GreeterGrpc.GreeterBlockingStub;
import ind.sq.study.grpc.Helloworld.HelloReply;
import ind.sq.study.grpc.Helloworld.HelloRequest;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.MetadataUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class Client extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private GreeterBlockingStub greeterStub;

    public Client(GreeterBlockingStub stub) {
        this.greeterStub = stub;
    }

    @Override
    public void run() {
//        String name = Thread.currentThread().getName();
//        HelloReply reply = greeterStub.sayHello(HelloRequest.newBuilder().setFirstName(name).build());
        HelloReply reply = greeterStub.sayHello(HelloRequest.newBuilder().build());

        logger.info("<<< {}/{}/{}", reply.getMessage(), reply.getAge(), reply.getMemo().equals(""));

    }
    
    public static void main(String[] args) {

        logger.info("Starting client");
        var channelBuilder = ManagedChannelBuilder.forAddress("localhost", 50051).enableRetry().usePlaintext();
        var client = new Client(GreeterGrpc.newBlockingStub(channelBuilder.build()));
        for (int i = 0; i < 200000; i ++) {
//            try {
//                sleep(1000l);
//            } catch (InterruptedException e) {
//
//            }
            logger.info(">>>Staring Run {} times", i);
            client.run();
        }

//        var blockingStub = HealthGrpc.newBlockingStub(channelBuilder.build());
//            // GreeterGrpc.newBlockingStub(channelBuilder.build());
//        var response = blockingStub.check(Healthcheck.HealthCheckRequest.newBuilder().build());
//        System.out.println(response.getStatus());
//        var es = Executors.newFixedThreadPool(100);
//        for (int i = 0; i < 100; i++) {
//            es.submit(new Client(blockingStub));
//        }
//
//        es.shutdown();
        
    }
}
