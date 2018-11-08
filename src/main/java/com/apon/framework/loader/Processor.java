package com.apon.framework.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that indicates the class should be loaded by the CommandProvider.
 * Note that you can only annotated classes that extend CommandProcessor!
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Processor {
    Class<? extends CommandProcessorOptions> options() default CommandProcessorOptions.class;
}
