package one.leftshift.backup.service.task;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import one.leftshift.backup.BackupRequest;
import one.leftshift.backup.preprocessor.BackupPreprocessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author benjamin.krenn@leftshift.one - 10/22/18.
 * @since 1.0.0
 */
public class TableBackupRequest {
    private final String tableName;
    private final BackupRequest initialRequest;
    private final AmazonDynamoDB dynamoDBClient;
    private final List<BackupPreprocessor> preprocessors;

    private TableBackupRequest(Builder builder) {
        this.tableName = builder.tableName;
        this.initialRequest = builder.initialRequest;
        this.dynamoDBClient = builder.dynamoDBClient;
        this.preprocessors = builder.preprocessors;
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

    public List<BackupPreprocessor> getPreprocessors() {
        return Collections.unmodifiableList(preprocessors);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements one.leftshift.common.Builder<TableBackupRequest> {
        private String tableName;
        private BackupRequest initialRequest;
        private AmazonDynamoDB dynamoDBClient;
        private List<BackupPreprocessor> preprocessors = new ArrayList<>();

        public Builder tableName(String tableName) {
            Objects.requireNonNull(tableName, "tableName can not be null");
            this.tableName = tableName;
            return this;
        }

        public Builder initialRequest(BackupRequest initialRequest) {
            Objects.requireNonNull(initialRequest, "initialRequest can not be null");
            this.initialRequest = initialRequest;
            return this;
        }

        public Builder dynamoDBClient(AmazonDynamoDB dynamoDBClient) {
            Objects.requireNonNull(dynamoDBClient, "dynamoDBClient can not be null");
            this.dynamoDBClient = dynamoDBClient;
            return this;
        }

        public Builder withPreprocessor(BackupPreprocessor preprocessor) {
            Objects.requireNonNull(dynamoDBClient, "preprocessor can not be null");
            this.preprocessors.add(preprocessor);
            return this;
        }

        public Builder withPreprocessors(List<BackupPreprocessor> preprocessors) {
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
