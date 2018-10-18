package one.leftshift.mirror;

import com.amazonaws.regions.Regions;

import java.util.List;

/**
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public class DefaultMirrorRequest implements MirrorRequest {

    private final List<String> tableNames;
    private final Regions from;
    private final Regions to;

    DefaultMirrorRequest(DefaultMirrorRequestBuilder builder) {
        this.tableNames = builder.tableNames;
        this.from = builder.from;
        this.to = builder.to;
    }

    @Override
    public List<String> tableNames() {
        return this.tableNames;
    }

    @Override
    public Regions from() {
        return this.from;
    }

    @Override
    public Regions to() {
        return this.to;
    }
}
