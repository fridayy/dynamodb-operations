package one.leftshift.backup;

import one.leftshift.backup.preprocessor.BackupPreprocessor;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public interface BackupRequest {

    Set<String> tableNames();

    BackupDestination backupDestination();

    List<BackupPreprocessor> preprocessors();

    static Builder builder() {
        return new DefaultBackupRequestBuilder();
    }

    interface Builder {

        Builder addTable(String tableName);

        Builder tableNames(String... tableNames);

        Builder tableNames(Collection<String> tableNames);

        Builder backupDestination(BackupDestination backupDestination);

        Builder addPreprocessor(BackupPreprocessor preprocessor);

        BackupRequest build();
    }
}
