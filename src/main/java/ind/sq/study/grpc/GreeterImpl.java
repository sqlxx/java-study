package ind.sq.study.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ind.sq.study.grpc.GreeterGrpc.GreeterImplBase;
import ind.sq.study.grpc.Helloworld.HelloReply;
import ind.sq.study.grpc.Helloworld.HelloRequest;
import io.grpc.stub.StreamObserver;

public class GreeterImpl extends GreeterImplBase {

    private static final Logger logger = LoggerFactory.getLogger(GreeterImpl.class);

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        var name = request.getFirstName();
        var lastName = request.getLastName();
        logger.info("Request to say hello to {}, {}", name, lastName);

        var reply = HelloReply.newBuilder().setMessage("Hello " + name).build();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //ignored
        }

        responseObserver.onNext(reply);
        logger.info("Response replied");
        responseObserver.onCompleted();
        logger.info("Request processed");
    }

}