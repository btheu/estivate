package estivate.annotations;

public @interface Is {

    String select() default "";

    String value();

}
