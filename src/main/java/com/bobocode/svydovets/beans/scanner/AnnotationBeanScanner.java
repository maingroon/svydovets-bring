package com.bobocode.svydovets.beans.scanner;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.beans.definition.BeanDefinition;

public class AnnotationBeanScanner implements BeanScanner {

    /**
     * This method scan package to find classes annotated with @Component annotation
     * and returns bean definition for this classes.
     *
     * @param packageName - package name that will be scanned
     * @return - map of bean d
     */
    @Override
    public Map<String, BeanDefinition> scan(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Component.class);
        return typesAnnotatedWith.stream()
                .map(c -> {
                    var name = c.getAnnotation(Component.class).value();
                    name = name.isEmpty() ? c.getName() : name;

                    return new BeanDefinition(name, c);
                })
                .collect(Collectors.toMap(
                        BeanDefinition::getName,
                        bd -> bd
                ));
    }
}
