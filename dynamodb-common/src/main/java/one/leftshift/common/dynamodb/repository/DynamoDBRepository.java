package one.leftshift.common.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;

import java.util.Collection;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public interface DynamoDBRepository<T> {

    Collection<T> findAll();

    void delete();

    DescribeTableResult describe();

}
