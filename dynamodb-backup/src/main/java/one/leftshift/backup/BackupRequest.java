package one.leftshift.backup;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public interface BackupRequest {

    Set<String> tableNames();

    BackupDestination backupDestination();

    List<UnaryOperator<Map<String,Object>>> preprocessors();

    static Builder builder() {
        return new DefaultBackupRequestBuilder();
    }

    interface Builder {

        Builder addTable(String tableName);

        Builder tableNames(String... tableNames);

        Builder tableNames(Collection<String> tableNames);

        Builder backupDestination(BackupDestination backupDestination);

        Builder addPreprocessor(UnaryOperator<Map<String,Object>> preprocessor);

        BackupRequest build();
    }
}
