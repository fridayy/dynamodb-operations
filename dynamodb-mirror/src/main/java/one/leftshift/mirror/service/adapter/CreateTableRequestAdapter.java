package one.leftshift.mirror.service.adapter;

import com.amazonaws.services.dynamodbv2.model.*;

import java.util.List;
import java.util.Objects;

/**
 * TODO: null safety!!!
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public class CreateTableRequestAdapter extends CreateTableRequest {

    private final DescribeTableResult adaptee;

    public CreateTableRequestAdapter(DescribeTableResult adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String getTableName() {
        return adaptee.getTable().getTableName();
    }

    @Override
    public List<AttributeDefinition> getAttributeDefinitions() {
        return adaptee.getTable().getAttributeDefinitions();
    }

    @Override
    public List<KeySchemaElement> getKeySchema() {
        return adaptee.getTable().getKeySchema();
    }

    @Override
    public ProvisionedThroughput getProvisionedThroughput() {
        return new ProvisionedThroughput(
                adaptee.getTable().getProvisionedThroughput().getReadCapacityUnits(),
                adaptee.getTable().getProvisionedThroughput().getWriteCapacityUnits()
        );
    }

    @Override
    public SSESpecification getSSESpecification() {
        SSEDescription description = this.adaptee.getTable().getSSEDescription();
        if (description != null) {
            return new SSESpecification()
                    .withSSEType(description.getSSEType())
                    .withEnabled(sseStatusToBoolean(description.getStatus()));
        }
        return new SSESpecification().withEnabled(false);
    }

    private Boolean sseStatusToBoolean(String sseStatus) {
        if (Objects.isNull(sseStatus)) {
            return Boolean.FALSE;
        }
        switch (sseStatus) {
            case "ENABLING":
                return Boolean.TRUE;
            case "ENABLED":
                return Boolean.TRUE;
            default:
                return Boolean.FALSE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CreateTableRequestAdapter that = (CreateTableRequestAdapter) o;
        return Objects.equals(adaptee, that.adaptee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), adaptee);
    }
}
