package ind.sq.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMain {
    private static Logger logger = LoggerFactory.getLogger(TestMain.class);

    public static void main(String[] args) {
        
        logger.info("--------CircuitBreaker test start--------");
        var cbt = new CircuitBreakerTest();
        cbt.recoverFromException();
        logger.info("**********CircuitBreaker test finished********"); 

        logger.info("--------RateLimiter test start--------");
        var rl = new RateLimiterTest();
        rl.rateLimiter();
        logger.info("**********RateLimiter test finished********"); 
        logger.info("--------Bulkhead test start--------");
        var bh = new BulkheadTest();
        bh.bulkhead();

        logger.info("**********Bulkhead test finished********");
    }
}