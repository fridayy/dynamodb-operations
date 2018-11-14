package one.leftshift.common.dynamodb.exception;

/**
 * @author benjamin.krenn@leftshift.one - 11/12/18.
 * @since 1.0.0
 */
public class TableCreationFailureException extends RuntimeException {
    public TableCreationFailureException(Throwable cause) {
        super(cause);
    }
}
