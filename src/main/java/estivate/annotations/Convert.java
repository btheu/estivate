package estivate.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import estivate.TypeConvertor;

/**
 * @author Benoit Theunissen
 *
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface Convert {

    Class<? extends TypeConvertor> value();

}
