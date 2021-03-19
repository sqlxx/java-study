package ind.sq.study.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GreeterServer {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final int PORT = 50051;

    private Server server;

    public void start() throws IOException {
        this.server = ServerBuilder.forPort(PORT).addService(new GreeterImpl()).build().start();
        logger.info("Server started on port {}", PORT);
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
            logger.info("Server stopped");
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }

    }

    public static void main(String[] args) throws Exception {
        logger.info("Starting...");
        var server = new GreeterServer();
        server.start(); 
        logger.info("Server started");
        server.blockUntilShutdown();
    }
}
