package ind.sq.test;

import java.time.Duration;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.SupplierUtils;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;

public class CircuitBreakerTest {
    private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerTest.class);

    private CircuitBreakerRegistry registry;

    public CircuitBreakerTest() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().failureRateThreshold(50)
        .waitDurationInOpenState(Duration.ofMillis(1000)).ringBufferSizeInHalfOpenState(2)
        .ringBufferSizeInClosedState(2).build();

        this.registry = CircuitBreakerRegistry.of(circuitBreakerConfig);

        registry.getEventPublisher().onEntryAdded(entryAddedEvent -> {
            logger.info("CircuitBreaker " + entryAddedEvent.getAddedEntry().getName() + " added.");
        }).onEntryRemoved(entryRemovedEvent -> {
            logger.info("CircuitBreaker " + entryRemovedEvent.getRemovedEntry().getName() + " removed.");
        });
    }

    public void testCircuit() {
        CircuitBreaker circuitBreaker = registry.circuitBreaker("testBreaker");
        circuitBreaker.onError(0, new RuntimeException());
        logger.info("CircuitBreaker status: " + circuitBreaker.getState());
        circuitBreaker.onSuccess(1);
        logger.info("CircuitBreaker status: " + circuitBreaker.getState());
        logger.info(circuitBreaker.getMetrics().getNumberOfSuccessfulCalls() + "");
        logger.info(circuitBreaker.getMetrics().getNumberOfFailedCalls() + "");
        logger.info("CircuitBreaker status: " + circuitBreaker.getState());

        CheckedFunction0<String> decoratedSupplier = CircuitBreaker.decorateCheckedSupplier(circuitBreaker,
                () -> "This can be any method with returns : 'Hello");
        Try<String> result = Try.of(decoratedSupplier).map(value -> value + " world'");

        logger.info("Invocation success? " + result.isSuccess());
        //logger.info("Result is : " + result.get());
        logger.info("CircuitBreaker status: " + circuitBreaker.getState());

        CheckedFunction1<String, String> decoratedFunction = CircuitBreaker.decorateCheckedFunction(circuitBreaker,
                (input) -> input + " world");

        Try<String> result1 = Try.of(decoratedSupplier).map(value -> value + "!");

        logger.info("Invocation failure? " + result.isFailure());
        // logger.info(result1.get());

    } 

    public void recoverFromException() {

        CircuitBreaker circuitBreaker = registry.circuitBreaker("testBreaker");

        Supplier<String> supplier = () -> {
            throw new RuntimeException("BAD");
        };

        Supplier<String> supplierWithRecovery = SupplierUtils.recover(supplier, (exception) -> "Hello Recovery: " + exception.getMessage());
        String result1 = circuitBreaker.executeSupplier(supplierWithRecovery);
        logger.info(result1);
        logger.info("{}/{}", circuitBreaker.getMetrics().getNumberOfSuccessfulCalls(), circuitBreaker.getMetrics().getNumberOfFailedCalls());

        Supplier<String> supplierWithResultAndExceptionHandler = SupplierUtils.andThen(supplier, (result, exception) -> "Hello Recovery: " + result + ", " + exception.getMessage());
        String result2 = circuitBreaker.executeSupplier(supplierWithResultAndExceptionHandler);
        logger.info(result2);
        logger.info("{}/{}", circuitBreaker.getMetrics().getNumberOfSuccessfulCalls(), circuitBreaker.getMetrics().getNumberOfFailedCalls());

        CheckedFunction0<String> checkedSupplier = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> {throw new RuntimeException("Failure as shame"); });

        Try<String> result = Try.of(checkedSupplier).recover(throwable -> "Hello Recovery 1");
        logger.info(result + "");
        logger.info("{}/{}", circuitBreaker.getMetrics().getNumberOfSuccessfulCalls(), circuitBreaker.getMetrics().getNumberOfFailedCalls());
        Try<String> result3 = Try.of(checkedSupplier).recover(throwable -> "Hello Recovery 2");

        logger.info(result3 + "");
        logger.info("{}/{}", circuitBreaker.getMetrics().getNumberOfSuccessfulCalls(), circuitBreaker.getMetrics().getNumberOfFailedCalls());

    }

    public static void main(String[] args) {
        CircuitBreakerTest cbt = new CircuitBreakerTest();
        cbt.recoverFromException();

    }

}