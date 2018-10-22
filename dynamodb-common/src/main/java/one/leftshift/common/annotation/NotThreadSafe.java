package one.leftshift.common.annotation;

import java.lang.annotation.*;

/**
 * @author benjamin.krenn@leftshift.one - 10/22/18.
 * @since 1.0.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface NotThreadSafe {
}
