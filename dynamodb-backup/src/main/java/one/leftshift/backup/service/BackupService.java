package one.leftshift.backup.service;

import one.leftshift.backup.BackupRequest;

import java.util.List;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public interface BackupService {

    void backup(List<BackupRequest> backupRequests);

}
