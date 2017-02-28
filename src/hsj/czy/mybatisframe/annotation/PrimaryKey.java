package hsj.czy.mybatisframe.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 主键注解
 * @author 31417
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD})
public @interface PrimaryKey {
	public String key() default "";
}
