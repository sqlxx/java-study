package ind.sq.test;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;

public class BulkheadTest {

    private static final Logger logger = LoggerFactory.getLogger(BulkheadTest.class);

    private BulkheadRegistry registry;

    public BulkheadTest() {
        BulkheadConfig config = BulkheadConfig.custom().maxConcurrentCalls(3)
                .maxWaitTimeDuration(Duration.ofMillis(100)).build();
        this.registry = BulkheadRegistry.of(config);

    }

    public void bulkhead() {
        Bulkhead bulkhead = registry.bulkhead("testBulkhead");
        Bulkhead anotherBulkhead = registry.bulkhead("anotherTestBulkhead");

        CheckedFunction0<String> decoratedSupplier = Bulkhead.decorateCheckedSupplier(bulkhead, () -> "Hello");
        CheckedFunction1<String, String> decoratedFunction = Bulkhead.decorateCheckedFunction(anotherBulkhead,
                (input) -> input + " World!");

        Try<String> result = Try.of(decoratedSupplier).mapTry(decoratedFunction);

        anotherBulkhead.tryAcquirePermission();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //ignored
        }
        logger.info( "{}/{}", anotherBulkhead.getMetrics().getAvailableConcurrentCalls(), anotherBulkhead.getMetrics().getMaxAllowedConcurrentCalls());
        logger.info("{}", result.isSuccess());
        
        for (int i = 0; i < 100; i ++) {
            var wt = new WorkThread(bulkhead, i + "");
            wt.start();
        }
    }

    class WorkThread extends Thread {

        private Bulkhead bulkhead;
        private String workerId;
        
        WorkThread(Bulkhead bulkhead, String workerId) {
            this.bulkhead = bulkhead;
            this.workerId = workerId;
        }
        @Override
        public void run() {
           CheckedFunction0<String> decoratedSupplier = Bulkhead.decorateCheckedSupplier(bulkhead, () -> {
               Thread.sleep(100);
               return "This is thread " + workerId;
           }); 

           Try<String> result = Try.of(decoratedSupplier);
           logger.info( "{}: result:{}, {}/{}",workerId, result.isSuccess(),  bulkhead.getMetrics().getAvailableConcurrentCalls(), bulkhead.getMetrics().getMaxAllowedConcurrentCalls());
            
        }
    }

}