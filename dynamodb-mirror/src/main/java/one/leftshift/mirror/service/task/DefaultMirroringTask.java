package one.leftshift.mirror.service.task;

import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import one.leftshift.common.dynamodb.AmazonDynamoDBFactory;
import one.leftshift.common.dynamodb.AmazonDynamoDBTableCreator;
import one.leftshift.common.dynamodb.DefaultAmazonDynamoDBTableCreator;
import one.leftshift.common.dynamodb.repository.DynamoDBRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public class DefaultMirroringTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DefaultMirroringTask.class);
    private final MirroringContext context;
    private final DynamoDBRepository<Map<String, Object>> fromRepository;
    private final DynamoDBRepository<Map<String, Object>> toRepository;

    public DefaultMirroringTask(MirroringContext context,
                                DynamoDBRepository<Map<String, Object>> fromRepository,
                                DynamoDBRepository<Map<String, Object>> toRepository) {
        this.context = context;
        this.fromRepository = fromRepository;
        this.toRepository = toRepository;
    }

    @Override
    public void run() {
        log.info("mirroring {} from {} to {}", this.context.getTableName(), this.context.getFrom().getName(), this.context.getTo().getName());
        AmazonDynamoDBTableCreator creator = new DefaultAmazonDynamoDBTableCreator(AmazonDynamoDBFactory.synchronous(this.context.getTo()));
        log.info("Describing source table {} in source region {}", this.context.getTableName(), this.context.getFrom().getName());
        DescribeTableResult sourceTable = fromRepository.describe();
        log.info("Deleting table {} in target region {}", this.context.getTableName(), this.context.getTo().getName());
        this.toRepository.delete();
        log.info("Creating table {} in target region {}", this.context.getTableName(), this.context.getTo().getName());
        creator.create(sourceTable);
        log.info("Batch writing items from {}-{} to {}-{}", this.context.getTableName(), this.context.getFrom().getName(), this.context.getTableName(), this.context.getTo().getName());
        this.toRepository.save(this.fromRepository.findAll());
        log.info("Done mirroring table {}", this.context.getTableName());
    }
}
