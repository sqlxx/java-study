package ind.sq.test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.vavr.control.Try;

public class TimeLimiterTest {

    private static final Logger logger = LoggerFactory.getLogger(TimeLimiterTest.class);

    private TimeLimiter timeLimiter;

    public TimeLimiterTest() {
        TimeLimiterConfig config = TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(1))
                .cancelRunningFuture(true).build();

        this.timeLimiter = TimeLimiter.of(config);
    }

    public void normCase() throws Exception {

        Supplier<CompletableFuture<Integer>> futureSupplier = () -> CompletableFuture.supplyAsync(() -> 1);

        // exctute the future
        Integer returnValue = timeLimiter.executeFutureSupplier(futureSupplier);

        logger.info("returned value is {}", returnValue);

        // decorate your supplier so that futre can be retrieved and executed upon
        var restrictedCall = TimeLimiter.decorateFutureSupplier(timeLimiter, futureSupplier);

        var result = Try.of(restrictedCall::call).onFailure(throwable -> logger.info("A timeout possibly occurred."));
        logger.info("Is success: {}", result.isSuccess());
    }

    public void timedout() throws Exception {
        Supplier<CompletableFuture<Integer>> futureSupplier = () -> CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        });

        // decorate your supplier so that futre can be retrieved and executed upon
        var restrictedCall = TimeLimiter.decorateFutureSupplier(timeLimiter, futureSupplier);

        var result = Try.of(restrictedCall::call).onFailure(throwable -> logger.info("A timeout possibly occurred."));
        logger.info("Is success: {}", result.isSuccess());
    }

    public void chainedWithCircuitBreaker() {
        Supplier<CompletableFuture<Integer>> futureSupplier = () -> CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        });

        var restrictedCall = TimeLimiter.decorateFutureSupplier(timeLimiter, futureSupplier);

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().failureRateThreshold(50)
                .ringBufferSizeInClosedState(2).build();
        var circuitBreaker = CircuitBreaker.of("test", circuitBreakerConfig);
        var chainedCallable = CircuitBreaker.decorateCallable(circuitBreaker, restrictedCall);

        Try.of(chainedCallable::call).onFailure(
                throwable -> logger.info("timed out or the circuit breaker has opened. {}", throwable.getMessage()));
        logger.info("1st try, current status: {}", circuitBreaker.getState());

        Try.of(chainedCallable::call).onFailure(
                throwable -> logger.info("timed out or the circuit breaker has opened. {}", throwable.getMessage()));
        logger.info("2nd try, current status: {}", circuitBreaker.getState());

        Try.of(chainedCallable::call).onFailure(
                throwable -> logger.info("timed out or the circuit breaker has opened. {}", throwable.getMessage()));
    }
}