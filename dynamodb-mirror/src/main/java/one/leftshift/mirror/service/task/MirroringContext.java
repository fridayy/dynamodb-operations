package one.leftshift.mirror.service.task;

import com.amazonaws.regions.Regions;
import one.leftshift.common.util.Tuple;

import java.util.Objects;

/**
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public class MirroringContext {
    private final Regions from;
    private final Regions to;
    private final String tableName;

    private MirroringContext(Regions from, Regions to, String tableName) {
        this.from = from;
        this.to = to;
        this.tableName = tableName;
    }

    public static MirroringContext from(Tuple<Regions, Regions> fromTo, String tableName) {
        return new MirroringContext(fromTo.getLeft(), fromTo.getRight(), tableName);
    }

    public Regions getFrom() {
        return from;
    }

    public Regions getTo() {
        return to;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MirroringContext that = (MirroringContext) o;
        return from == that.from &&
                to == that.to &&
                Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, tableName);
    }
}
