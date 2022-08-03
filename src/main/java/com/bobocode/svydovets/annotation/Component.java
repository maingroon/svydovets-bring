package com.bobocode.svydovets.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

/**
 * This annotation is a marker for the Bring that Bring have to create an instance of the class that marked with this
 * annotation and then manage lifecycle of created bean.
 * This annotation us applicable to class.
 * You can pass bean name, if you didn't pass the name then as name will be taken {@link Class#getSimpleName()} with the
 * first latter in lowerCase.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

    /**
     * Specify the name of {@link BeanDefinition} associated with annotated class.
     *
     * @return the suggested component name (if not empty)
     */
    String value() default "";
}
