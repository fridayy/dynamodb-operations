package one.leftshift.backup;

import one.leftshift.backup.preprocessor.BackupPreprocessor;
import one.leftshift.common.util.ObjectUtil;

import java.util.*;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
class DefaultBackupRequestBuilder implements BackupRequest.Builder {

    Set<String> tableNames = new HashSet<>();
    List<BackupPreprocessor> preprocessors = new ArrayList<>();
    BackupDestination backupDestination;

    @Override
    public BackupRequest.Builder addTable(String tableName) {
        ObjectUtil.assertNotNull("tableNames can not be null", tableName);
        this.tableNames.add(tableName);
        return this;
    }

    @Override
    public BackupRequest.Builder tableNames(String... tableNames) {
        ObjectUtil.assertNotNull("tableNames can not be null", (Object[]) tableNames);
        this.tableNames.addAll(Arrays.asList(tableNames));
        return this;
    }

    @Override
    public BackupRequest.Builder tableNames(Collection<String> tableNames) {
        ObjectUtil.assertNotNull("tableNames can not be null", tableNames);
        this.tableNames.addAll(tableNames);
        return this;
    }

    @Override
    public BackupRequest.Builder backupDestination(BackupDestination backupDestination) {
        ObjectUtil.assertNotNull("destination can not be null", backupDestination);
        this.backupDestination = backupDestination;
        return this;
    }

    @Override
    public BackupRequest.Builder addPreprocessor(BackupPreprocessor preprocessor) {
        ObjectUtil.assertNotNull("preprocessor can not be null", preprocessor);
        this.preprocessors.add(preprocessor);
        return this;
    }

    @Override
    public BackupRequest build() {
        if (tableNames.isEmpty() || Objects.isNull(backupDestination)) {
            throw new IllegalStateException("tableNames and destination are required.");
        }
        return new DefaultBackupRequest(this);
    }
}
