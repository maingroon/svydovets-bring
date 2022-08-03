package com.bobocode.svydovets.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.annotation.Inject;
import com.bobocode.svydovets.beans.bpp.BeanPostProcessor;
import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.exception.BeanInstantiationException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class DefaultListableBeanFactory implements BeanFactory {

    @Getter
    private final Set<BeanPostProcessor> beanPostProcessors;

    /**
     * Create a new map of bean instances, from bean definitions.
     *
     * @param definitionMap bean definition map, key - the name of the bean, the value - bean definition object
     * @return bean map, where key - bean name, and value - new instance of the bean
     */
    @Override
    public Map<String, Object> createBeans(Map<String, BeanDefinition> definitionMap) {
        var componentBeans = definitionMap.values().stream()
          .filter(bd -> Objects.isNull(bd.getFactoryMethod()))
          .collect(Collectors.toMap(BeanDefinition::getName, this::createComponentBean));

        var configurationDeclaredBeans = definitionMap.values().stream()
          .filter(bd -> Objects.nonNull(bd.getFactoryMethod()))
          .map(confDeclaredBeanDefinitionEntry -> createConfigurationDeclaredBean(confDeclaredBeanDefinitionEntry,
            componentBeans))
          .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

        Map<String, Object> beanMap = Stream.of(componentBeans, configurationDeclaredBeans)
          .flatMap(map -> map.entrySet().stream())
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        injectComponentBeans(beanMap);
        injectConfigurationBeans(definitionMap, beanMap);

        return beanMap;
    }

    private Pair<String, Object> createConfigurationDeclaredBean(BeanDefinition beanDefinition,
                                                                 Map<String, Object> componentBeans) {
        var declaredClass = beanDefinition.getConfigurationClass();
        var declaredClassConfigValue = declaredClass.getAnnotation(Configuration.class).value().trim();
        var componentBeanName =
          declaredClassConfigValue.isEmpty() ? StringUtils.uncapitalize(declaredClass.getName()) :
            declaredClassConfigValue;
        var declaredClassInstance = componentBeans.get(componentBeanName);
        try {
            var beanInstance = beanDefinition.getFactoryMethod().invoke(declaredClassInstance,
              new Object[beanDefinition.getFactoryMethod().getParameters().length]);
            var beanInstanceProcessedBeforeInitialization =
              applyPostprocessorsBeforeInitialization(beanInstance, componentBeanName);
            return Pair.of(beanDefinition.getName(), beanInstanceProcessedBeforeInitialization);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new BeanInstantiationException("Could not instantiate a bean.", ex);
        } catch (IllegalArgumentException ex) {
            throw new BeanInstantiationException(
              String.format("Could not instantiate a bean: %s. Default constructor should be created.",
                beanDefinition.getName()), ex);
        }
    }

    private Object createComponentBean(BeanDefinition beanDefinition) {
        try {
            Object originalBean = beanDefinition.getBeanClass().getConstructor().newInstance();
            return applyPostprocessorsBeforeInitialization(originalBean, beanDefinition.getName());
        } catch (InvocationTargetException | InstantiationException
                 | IllegalAccessException | NoSuchMethodException exception) {
            throw new BeanInstantiationException(
              String.format("Could not instantiate a bean: %s. Default constructor should be created.",
                beanDefinition.getName()), exception);
        }
    }

    private Object applyPostprocessorsBeforeInitialization(Object bean, String beanName) {
        var result = bean;
        for (var postprocessor : beanPostProcessors) {
            result = postprocessor.postProcessBeforeInitialization(bean, beanName);
            if (result == null) {
                log.info("Postprocessor {} returns null, all subsequent postprocessors will be skipped",
                  postprocessor.getClass().getSimpleName());
                break;
            }
        }
        return result;
    }

    private void injectComponentBeans(Map<String, Object> beanMap) {
        beanMap.values().forEach(bean ->
          Arrays.stream(bean.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .forEach(field -> inject(bean, field, beanMap)));
    }

    private void injectConfigurationBeans(Map<String, BeanDefinition> definitionMap, Map<String, Object> beanMap) {
        definitionMap.values().stream()
          .filter(beanDefinition -> beanDefinition.getDependsOn() != null)
          .forEach(beanDefinition -> inject(beanDefinition, beanMap));
    }
    private void inject(Object bean, Field field, Map<String, Object> beanMap) {
        Inject fieldAnnotation = field.getAnnotation(Inject.class);

        if (StringUtils.isNotEmpty(fieldAnnotation.value())) {
            injectToFiled(bean, field, beanMap.get(fieldAnnotation.value()));
        } else {
            injectToFiled(bean, field, beanMap.get(field.getType().getName()));
        }
    }

    private void inject(BeanDefinition beanDefinition, Map<String, Object> beanMap) {
        var bean = beanMap.get(beanDefinition.getName());
        var beanClass = bean.getClass();

        var dependencyMap = Arrays.stream(beanDefinition.getDependsOn())
          .collect(Collectors.toMap(Function.identity(), Function.identity()));

        Arrays.stream(beanClass.getDeclaredFields())
          .filter(field -> dependencyMap.get(field.getType().getName()) != null)
          .forEach(field -> inject(bean, field, beanMap));
    }

    private void injectToFiled(Object bean, Field field, Object injectBean) {
        try {
            field.setAccessible(true);
            field.set(bean, injectBean);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(String.format("Unable to inject '%s' into '%s'. Fall with exception: [%s]",
              injectBean.getClass().getSimpleName(), bean.getClass().getSimpleName(), exception));
        }
    }
}
