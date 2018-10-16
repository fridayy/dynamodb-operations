package one.leftshift.backup;

import one.leftshift.common.util.ObjectUtil;

import java.util.Objects;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
class DefaultBackupRequestBuilder implements BackupRequest.Builder {

    String tableName;
    BackupDestination backupDestination;

    @Override
    public BackupRequest.Builder tableName(String tableName) {
        Objects.requireNonNull(tableName, "tableName can not be null");
        this.tableName = tableName;
        return this;
    }

    @Override
    public BackupRequest.Builder backupDestination(BackupDestination backupDestination) {
        Objects.requireNonNull(backupDestination, "backupDestination can not be null");
        this.backupDestination = backupDestination;
        return this;
    }

    @Override
    public BackupRequest build() {
        ObjectUtil.assertNotNull("tableName and destination must be set", tableName, backupDestination);
        return new DefaultBackupRequest(this);
    }
}
