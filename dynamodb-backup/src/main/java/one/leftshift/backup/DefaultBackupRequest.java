package one.leftshift.backup;

import one.leftshift.backup.preprocessor.BackupPreprocessor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author benjamin.krenn@leftshift.one - 10/11/18.
 * @since 1.0.0
 */
public class DefaultBackupRequest implements BackupRequest {

    private final Set<String> tableNames;
    private final BackupDestination backupDestination;
    private final List<BackupPreprocessor> preprocessors;

    public DefaultBackupRequest(DefaultBackupRequestBuilder builder) {
        this.tableNames = builder.tableNames;
        this.backupDestination = builder.backupDestination;
        this.preprocessors = builder.preprocessors;
    }

    @Override
    public Set<String> tableNames() {
        return Collections.unmodifiableSet(this.tableNames);
    }

    @Override
    public BackupDestination backupDestination() {
        return this.backupDestination;
    }

    @Override
    public List<BackupPreprocessor> preprocessors() {
        return Collections.unmodifiableList(preprocessors);
    }
}
