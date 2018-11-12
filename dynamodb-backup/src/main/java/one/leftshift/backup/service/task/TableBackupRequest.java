package one.leftshift.backup.service.task;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import one.leftshift.backup.BackupRequest;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author benjamin.krenn@leftshift.one - 10/22/18.
 * @since 1.0.0
 */
public class TableBackupRequest {
    private final String tableName;
    private final BackupRequest initialRequest;
    private final AmazonDynamoDB dynamoDBClient;
    private final List<Function<Map<String, Object>, Map<String,Object>>> preprocessors;

    private TableBackupRequest(BuilderImpl builderImpl) {
        this.tableName = builderImpl.tableName;
        this.initialRequest = builderImpl.initialRequest;
        this.dynamoDBClient = builderImpl.dynamoDBClient;
        this.preprocessors = builderImpl.preprocessors;
    }

    String getTableName() {
        return tableName;
    }

    BackupRequest getInitialRequest() {
        return initialRequest;
    }

    AmazonDynamoDB getDynamoDBClient() {
        return dynamoDBClient;
    }

    public List<Function<Map<String, Object>, Map<String,Object>>> getPreprocessors() {
        return Collections.unmodifiableList(preprocessors);
    }

    public static BuilderImpl builder() {
        return new BuilderImpl();
    }

    public static class BuilderImpl implements one.leftshift.common.Builder<TableBackupRequest> {
        private String tableName;
        private BackupRequest initialRequest;
        private AmazonDynamoDB dynamoDBClient;
        private List<Function<Map<String, Object>, Map<String,Object>>> preprocessors = new ArrayList<>();

        public BuilderImpl tableName(String tableName) {
            Objects.requireNonNull(tableName, "tableName can not be null");
            this.tableName = tableName;
            return this;
        }

        public BuilderImpl initialRequest(BackupRequest initialRequest) {
            Objects.requireNonNull(initialRequest, "initialRequest can not be null");
            this.initialRequest = initialRequest;
            return this;
        }

        public BuilderImpl dynamoDBClient(AmazonDynamoDB dynamoDBClient) {
            Objects.requireNonNull(dynamoDBClient, "dynamoDBClient can not be null");
            this.dynamoDBClient = dynamoDBClient;
            return this;
        }

        public BuilderImpl withPreprocessor(Function<Map<String, Object>, Map<String,Object>> preprocessor) {
            Objects.requireNonNull(dynamoDBClient, "preprocessor can not be null");
            this.preprocessors.add(preprocessor);
            return this;
        }

        public BuilderImpl withPreprocessors(List<UnaryOperator<Map<String,Object>>> preprocessors) {
            Objects.requireNonNull(dynamoDBClient, "preprocessors can not be null");
            this.preprocessors.addAll(preprocessors);
            return this;
        }

        @Override
        public TableBackupRequest build() {
            return new TableBackupRequest(this);
        }
    }
}
