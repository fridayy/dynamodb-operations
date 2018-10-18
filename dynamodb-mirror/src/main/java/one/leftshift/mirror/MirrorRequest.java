package one.leftshift.mirror;

import com.amazonaws.regions.Regions;

import java.util.List;

/**
 * Requests the mirroring from one AWS regions to another
 *
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public interface MirrorRequest {

    List<String> tableNames();

    Regions from();

    Regions to();

    static Builder builder() {
        return new DefaultMirrorRequestBuilder();
    }

    interface Builder {

        Builder tableNames(List<String> tableNames);

        Builder tableNames(String... tableNames);

        Builder from(Regions from);

        Builder to(Regions to);

        MirrorRequest build();
    }
}
