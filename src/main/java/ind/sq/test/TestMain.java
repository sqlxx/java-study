package ind.sq.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMain {
    private static Logger logger = LoggerFactory.getLogger(TestMain.class);


    public static void main(String[] args) throws Exception {
        
        logger.info("********CircuitBreaker test start********");
        var cbt = new CircuitBreakerTest();
        cbt.recoverFromException();
        logger.info("--------CircuitBreaker test finished--------"); 

        logger.info("********RateLimiter test start********");
        var rl = new RateLimiterTest();
        rl.rateLimiter();
        logger.info("--------RateLimiter test finished--------"); 

        logger.info("********Bulkhead test start********");
        var bh = new BulkheadTest();
        bh.bulkhead();
        logger.info("--------Bulkhead test finished--------");
        
        logger.info("********Retry test start********");
        var retry = new RetryTest();
        retry.retry();
        logger.info("--------Retry test finished--------");

        logger.info("********TimeLimiter test start********");
        var timeLimiter = new TimeLimiterTest();

        try {
            timeLimiter.normCase();
        } catch (Exception ex) {
            logger.error("Exception in time limiter normal case, not expected.", ex);
        }

        logger.info("Timed out case: ");
        try {
            timeLimiter.timedout();
        } catch (Exception ex) {
            logger.info("Expected exception in timeLimiter timed out case", ex);
        }

        logger.info("Chained with circuit breaker case: ");

        timeLimiter.chainedWithCircuitBreaker();

        logger.info("--------TimeLimiter test finished--------");


        // logger.info("**********Test Future**************");
        // var futureTest = new CompletableFutureTest();
        // futureTest.normalFuture();
        // futureTest.completableFuture();
        // futureTest.completableFuture2();
        // futureTest.completableFutureWithException();
    }
}