package one.leftshift.backup.preprocessor;

import java.util.Map;
import java.util.function.Function;

/**
 * @author benjamin.krenn@leftshift.one - 10/22/18.
 * @since 1.0.0
 */
@FunctionalInterface
public interface BackupPreprocessor extends Function<Map<String, Object>, Map<String, Object>> {
}
