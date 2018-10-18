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
        ObjectUtil.assertNotNull("tableName can not be null", tableName);
        this.tableName = tableName;
        return this;
    }

    @Override
    public BackupRequest.Builder backupDestination(BackupDestination backupDestination) {
        ObjectUtil.assertNotNull("destination can not be null", backupDestination);
        this.backupDestination = backupDestination;
        return this;
    }

    @Override
    public BackupRequest build() {
        if (Objects.isNull(tableName) || Objects.isNull(backupDestination)) {
            throw new IllegalStateException("tableName and destination are required.");
        }
        return new DefaultBackupRequest(this);
    }
}
