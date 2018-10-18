package one.leftshift.mirror;

import com.amazonaws.regions.Regions;
import one.leftshift.common.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public class DefaultMirrorRequestBuilder implements MirrorRequest.Builder {

    List<String> tableNames = new ArrayList<>();
    Regions from;
    Regions to;


    @Override
    public MirrorRequest.Builder tableNames(List<String> tableNames) {
        ObjectUtil.assertNotNull("tableNames can not be null", tableNames);
        this.tableNames.addAll(tableNames);
        return this;
    }

    @Override
    public MirrorRequest.Builder tableNames(String... tableNames) {
        ObjectUtil.assertNotNull("tableNames can not be null", (Object[]) tableNames);
        this.tableNames.addAll(Arrays.asList(tableNames));
        return this;
    }

    @Override
    public MirrorRequest.Builder from(Regions from) {
        ObjectUtil.assertNotNull("from can not be null", from);
        this.from = from;
        return this;
    }

    @Override
    public MirrorRequest.Builder to(Regions to) {
        ObjectUtil.assertNotNull("from can not be null", to);
        this.to = to;
        return this;
    }

    @Override
    public MirrorRequest build() {
        if (Objects.isNull(this.from) || Objects.isNull(this.to) || (this.tableNames.isEmpty())) {
            throw new IllegalStateException("from, to and tableNames are required.");
        }
        return new DefaultMirrorRequest(this);
    }
}
