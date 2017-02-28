package hsj.czy.mybatisframe.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据表注解
 * 
 * @author Hong
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.TYPE})
public @interface Table {
    public String table_name() default "";
}
