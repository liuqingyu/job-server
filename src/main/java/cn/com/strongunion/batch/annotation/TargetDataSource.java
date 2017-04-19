package cn.com.strongunion.batch.annotation;

import java.lang.annotation.*;

/**
 * 动态选择数据源注解,
 * 可标注于方法和类上
 * 方法上的注解会覆盖类上的而注解
 *
 */
@Documented
@Inherited  // 声明注解可继承
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface TargetDataSource {
    String value() default "";
}
