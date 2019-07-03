package ind.sq.study.jdk;

import java.util.concurrent.ExecutionException;

public class TestMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        var futureTest = new CompletableFutureTest();
        futureTest.normalFuture();
        futureTest.completableFuture();
        futureTest.completableFuture2();
        futureTest.completableFutureWithException();
    }
}
