package one.leftshift.backup;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public interface BackupRequest {

    String tableName();

    BackupDestination backupDestination();

    static Builder builder() {
        return new DefaultBackupRequestBuilder();
    }

    interface Builder {

        Builder tableName(String tableName);

        Builder backupDestination(BackupDestination backupDestination);

        BackupRequest build();
    }
}
