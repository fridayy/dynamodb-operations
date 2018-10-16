package one.leftshift.backup;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public class BackupDestination {

    private final IODestination ioDestination;
    private final URI path;

    private BackupDestination(IODestination ioDestination, URI path) {
        this.ioDestination = ioDestination;
        this.path = path;
    }

    public static BackupDestination of(IODestination destination, URI path) {
        return new BackupDestination(destination, path);
    }

    public static BackupDestination withFileDestination(URI path) {
        return new BackupDestination(IODestination.FILE, path);
    }

    public static BackupDestination withFileDestination(String path) {
        return new BackupDestination(IODestination.FILE, Paths.get(path).toUri());
    }

    public static BackupDestination withFileDestination(Path path) {
        return new BackupDestination(IODestination.FILE, path.toUri());
    }

    public enum IODestination {
        FILE
    }

    public IODestination getIoDestination() {
        return ioDestination;
    }

    public URI getPath() {
        return path;
    }
}
