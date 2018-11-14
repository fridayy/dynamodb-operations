package one.leftshift.backup.service.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import one.leftshift.common.dynamodb.repository.SynchronousDynamoDBRepository;
import one.leftshift.common.functional.Functions;
import one.leftshift.common.dynamodb.repository.DynamoDBRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public class DefaultBackupTask implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(DefaultBackupTask.class);

    private final TableBackupRequest backupRequest;
    private final DynamoDBRepository<Map<String, Object>> repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DefaultBackupTask(TableBackupRequest backupRequest) {
        this.backupRequest = backupRequest;
        this.repository = new SynchronousDynamoDBRepository(backupRequest.getDynamoDBClient(), backupRequest.getTableName());
    }


    @Override
    public void run() {
        try {
            Collection<Map<String, Object>> items = this.repository.findAll();
            List<Map<String,Object>> preproccessedItems = items.stream()
                    .map(Functions.collapse(backupRequest.getPreprocessors()))
                    .collect(Collectors.toList());
            IOUtils.write(this.objectMapper.writeValueAsBytes(preproccessedItems),
                    new FileOutputStream(new File(this.backupRequest.getInitialRequest().backupDestination().getPath().resolve(this.backupRequest.getTableName() + ".json"))));
        } catch (IOException e) {
            log.error("Could not write to file", e);
        }
    }
}
