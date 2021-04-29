package ind.sq.study.resilience4j;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;

public class RateLimiterTest {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterTest.class);

    private RateLimiterRegistry registry;

    public RateLimiterTest() {
        RateLimiterConfig config = RateLimiterConfig.custom().limitForPeriod(1)
                .limitRefreshPeriod(Duration.ofMillis(100)).timeoutDuration(Duration.ofMillis(90)).build();
        this.registry = RateLimiterRegistry.of(config);
    }

    public void rateLimiter() {

        var rateLimiter = this.registry.rateLimiter("testRateLimiter");

        CheckedRunnable restrictedCall = RateLimiter.decorateCheckedRunnable(rateLimiter,
                () -> logger.info("I'm doing something..."));
        Try.run(restrictedCall).andThenTry(restrictedCall).andThenTry(restrictedCall)
                .onFailure(throwable -> logger.info("Wait before call it again"));

        logger.info("Available permissions: {}", rateLimiter.getMetrics().getAvailablePermissions());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // ignored
        }

        logger.info("Available permissions: {}", rateLimiter.getMetrics().getAvailablePermissions());

    }
}