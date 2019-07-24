package com.gzdzss.redis.plugin.limit;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestLimitAspect {

    /**
     * 允许访问的限制次数（默认50次）
     */
    long times() default 50L;

    /**
     * 时间段，单位：秒，多少时间段内运行访问count次（默认60s）
     */
    long expire() default 60L;

}
