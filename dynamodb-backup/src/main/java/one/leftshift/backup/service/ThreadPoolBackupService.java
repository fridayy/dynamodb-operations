package one.leftshift.backup.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import one.leftshift.backup.BackupRequest;
import one.leftshift.backup.service.task.DefaultBackupTask;
import one.leftshift.common.concurrent.AbstractThreadPoolService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public class ThreadPoolBackupService extends AbstractThreadPoolService implements BackupService {
    private final AmazonDynamoDB dynamoDBClient;

    public ThreadPoolBackupService(AmazonDynamoDB dynamoDBClient, ExecutorService executorService) {
        super(executorService);
        this.dynamoDBClient = dynamoDBClient;
    }

    public ThreadPoolBackupService(AmazonDynamoDB dynamoDBClient) {
        super(Executors.newFixedThreadPool(8));
        this.dynamoDBClient = dynamoDBClient;
    }


    @Override
    public void backup(List<BackupRequest> backupRequests) {
        backupRequests.forEach(request -> this.executorService.submit(new DefaultBackupTask(request, dynamoDBClient)));
        this.gracefullyShutdownThreadPool();
    }
}
