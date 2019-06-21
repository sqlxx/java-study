package ind.sq.test;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;

public class RetryTest {
    private static final Logger logger = LoggerFactory.getLogger(RetryTest.class);

    private Retry retry;

    public RetryTest() {

        var config = RetryConfig.<String>custom()
            .maxAttempts(2)
            .waitDuration(Duration.ofMillis(100))
            .retryOnResult((String response) -> response.isBlank())
            .build();

        this.retry = Retry.of("testRetry", config);
    }

    public void retry() {
        CheckedRunnable retryableSupplier = Retry.decorateCheckedRunnable(retry, () -> {
            logger.info("Doing something failed");
            throw new RuntimeException("Oh...");
        });
        Try.run(retryableSupplier).andFinally(() -> logger.info("finally"));

    }


}