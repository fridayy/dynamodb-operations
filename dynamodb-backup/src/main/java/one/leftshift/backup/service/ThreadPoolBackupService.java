package one.leftshift.backup.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import one.leftshift.backup.BackupRequest;
import one.leftshift.backup.service.task.DefaultBackupTask;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public class ThreadPoolBackupService implements BackupService {
    private final AmazonDynamoDB dynamoDBClient;
    private final ExecutorService executorService;

    public ThreadPoolBackupService(AmazonDynamoDB dynamoDBClient, ExecutorService executorService) {
        this.dynamoDBClient = dynamoDBClient;
        this.executorService = executorService;
    }

    public ThreadPoolBackupService(AmazonDynamoDB dynamoDBClient) {
        this.dynamoDBClient = dynamoDBClient;
        this.executorService = Executors.newFixedThreadPool(8);
    }

    @Override
    public void backup(List<BackupRequest> backupRequests) {
        backupRequests.forEach(request -> this.executorService.submit(new DefaultBackupTask(request, dynamoDBClient)));
        this.gracefullyShutdownThreadPool();
    }

    private void gracefullyShutdownThreadPool() {
        try {
            this.executorService.shutdown();
            this.executorService.awaitTermination(30L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
