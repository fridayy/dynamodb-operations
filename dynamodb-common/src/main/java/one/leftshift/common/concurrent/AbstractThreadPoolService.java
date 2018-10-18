package one.leftshift.common.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public abstract class AbstractThreadPoolService {
    protected final ExecutorService executorService;

    public AbstractThreadPoolService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    protected void gracefullyShutdownThreadPool() {
        try {
            this.executorService.shutdown();
            this.executorService.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
