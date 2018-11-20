package one.leftshift.mirror;

import com.amazonaws.regions.Regions;
import one.leftshift.mirror.service.TableMirroringService;
import one.leftshift.mirror.service.DefaultTableMirroringService;

import java.util.Arrays;
import java.util.List;

/**
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        //@formatter:off
        List<String> tables = Arrays.asList(
                                            "GAIA_Prompt"
        );
        //@formatter:on
        MirrorRequest request = MirrorRequest.builder()
                .from(Regions.EU_WEST_1)
                .to(Regions.EU_WEST_3)
                .tableNames(tables).build();
        TableMirroringService tms = new DefaultTableMirroringService();
        tms.mirror(request);
    }
}
