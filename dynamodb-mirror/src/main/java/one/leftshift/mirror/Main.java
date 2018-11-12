package one.leftshift.mirror;

import com.amazonaws.regions.Regions;
import one.leftshift.mirror.service.TableMirroringService;
import one.leftshift.mirror.service.TableMirroringServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        //@formatter:off
        List<String> tables = Arrays.asList("GAIA_API_Key",
                                            "GAIA_Behaviour",
                                            "GAIA_Billing",
                                            "GAIA_Channel_Execution",
                                            "GAIA_Code",
                                            "GAIA_Conversation",
                                            "GAIA_Conversation_Context",
                                            "GAIA_Exception",
                                            "GAIA_Fulfilment",
                                            "GAIA_Identity",
                                            "GAIA_Intent",
                                            "GAIA_Process_Execution",
                                            "GAIA_Project_Attributes",
                                            "GAIA_Prompt",
                                            "GAIA_Publisher_Evolution",
                                            "GAIA_Statement",
                                            "GAIA_Tenant",
                                            "GAIA_Tenant_Key",
                                            "GAIA_User");
        //@formatter:on
        MirrorRequest request = MirrorRequest.builder()
                .from(Regions.EU_CENTRAL_1)
                .to(Regions.EU_WEST_3)
                .tableNames(tables).build();
        TableMirroringService tms = new TableMirroringServiceImpl();
        tms.mirror(request);
    }
}
