package one.leftshift.backup;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import one.leftshift.backup.service.BackupService;
import one.leftshift.backup.service.ThreadPoolBackupService;

/**
 * @author benjamin.krenn@leftshift.one - 10/23/18.
 * @since 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        BackupService backupService = new ThreadPoolBackupService(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build());
        backupService.backup(BackupRequest.builder()
                .tableNames("GAIA_Code", "GAIA_Behaviour", "GAIA_Fulfilment", "GAIA_Intent", "GAIA_Prompt", "GAIA_Statement")
                .backupDestination(BackupDestination.withFileDestination("/home/bnjm/dev/tmp/"))
                .addPreprocessor(map -> {
                    map.replace("partitionKey", "1");
                    return map;
                })
                .build());
    }
}
