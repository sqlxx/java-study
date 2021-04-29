package ind.sq.study.jdk;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompletableFutureTest {

    private static final Logger logger = LoggerFactory.getLogger(CompletableFutureTest.class);

    public void normalFuture() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(10);

        Future<Integer> future = es.submit(() -> {
            Thread.sleep(10000);
            return 10;
        });

        var result = future.cancel(false);
        logger.info("Cancel result: {}", result);
        // int result = future.get();
        // logger.info("Result is {}", result);

        es.shutdown();
    }

    public void completableFuture() throws InterruptedException, ExecutionException {

        var future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                logger.info("In other thread");
                Thread.sleep(3000);
                future.complete(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
            }
        }).start();
        logger.info("Do something else");

        logger.info("{}", future.get());
    }

    public void completableFuture2() throws InterruptedException, ExecutionException {

        var future = CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("In other thread");
                Thread.sleep(3000);
                return Thread.currentThread().getName();
            } catch (InterruptedException e) {
                // For testing purpose
                throw new RuntimeException(e);
            }

        });

        logger.info("Do something else");

        logger.info("{}", future.get());
    }

    public void completableFutureWithException() throws InterruptedException, ExecutionException {
        var future = CompletableFuture.supplyAsync(() -> {
            return 100/10;
        });

        future.whenComplete((v, u) -> {
            logger.info("In when complete {}, {}", v, u);
        });
        logger.info("{}", future.join());
    }
}