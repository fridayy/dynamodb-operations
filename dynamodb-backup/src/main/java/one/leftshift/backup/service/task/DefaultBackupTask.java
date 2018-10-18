package one.leftshift.backup.service.task;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.leftshift.backup.BackupRequest;
import one.leftshift.backup.repository.SynchronousDynamoDBRepository;
import one.leftshift.common.repository.DynamoDBRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public class DefaultBackupTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBackupTask.class);
    private final BackupRequest request;
    private final DynamoDBRepository<Map<String, Object>> repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DefaultBackupTask(BackupRequest request, AmazonDynamoDB dynamoDBClient) {
        this.request = request;
        this.repository = new SynchronousDynamoDBRepository(dynamoDBClient, request.tableName());
    }


    @Override
    public void run() {
        try {
            IOUtils.write(this.objectMapper.writeValueAsBytes(this.repository.findAll()), new FileOutputStream(new File(this.request.backupDestination().getPath())));
        } catch (IOException e) {
            LOGGER.error("Could not write to file", e);
        }
    }
}
