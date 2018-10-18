package one.leftshift.common.repository;

import java.util.Collection;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public interface DynamoDBRepository<T> {

    Collection<T> findAll();

}
