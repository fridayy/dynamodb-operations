package one.leftshift.backup;

/**
 * @author benjamin.krenn@leftshift.one - 10/11/18.
 * @since 1.0.0
 */
public class DefaultBackupRequest implements BackupRequest {

    private final String tableName;
    private final BackupDestination backupDestination;

    public DefaultBackupRequest(DefaultBackupRequestBuilder builder) {
        this.tableName = builder.tableName;
        this.backupDestination = builder.backupDestination;
    }

    @Override
    public String tableName() {
        return this.tableName;
    }

    @Override
    public BackupDestination backupDestination() {
        return this.backupDestination;
    }
}
