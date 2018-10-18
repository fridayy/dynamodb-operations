package one.leftshift.mirror.service;

import one.leftshift.mirror.MirrorRequest;

/**
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public interface TableMirroringService {
    void mirror(MirrorRequest mirrorRequest);
}
