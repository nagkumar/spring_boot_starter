package org.bps.metrics.dbcount;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DBCountAutoConfiguration.class)
@Documented
public @interface EnableDBCounting
{
}
