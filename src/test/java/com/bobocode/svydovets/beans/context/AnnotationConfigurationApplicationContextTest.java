package com.bobocode.svydovets.beans.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import com.bobocode.svydovets.context.AnnotationConfigurationApplicationContext;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

public class AnnotationConfigurationApplicationContextTest {

    public static final String START_PACKAGE = "com.bobocode.svydovets.beans.";
    public static final String MOCK_PACKAGE = START_PACKAGE + "scanner.quoter";
    public static final String EMPTY_PACKAGE_NAME = "";
    public static final String NO_BEAN_FOR_INJECTION_PACKAGE_NAME = START_PACKAGE + "example.injection.failure.no.bean";
    public static final String NOT_UNIQUE_BEAN_FOR_INJECTION_PACKAGE_NAME =
      START_PACKAGE + "example.injection.failure.not.unique";

    @Test
    void shouldCallScanWhenContextIsCreated() {
        var componentBeanScanner = mock(ComponentBeanScanner.class);

        new AnnotationConfigurationApplicationContext(MOCK_PACKAGE);

        verify(componentBeanScanner, Mockito.times(1)).scan(MOCK_PACKAGE);
    }

    @Test
    void shouldThrowExceptionWhenPackageNameIsNull() {
        assertThrows(NullPointerException.class, () -> new AnnotationConfigurationApplicationContext(null));
    }

    @Test
    void shouldThrowExceptionWhenPackageNameIsEmpty() {
        assertThrows(IllegalArgumentException.class,
          () -> new AnnotationConfigurationApplicationContext(EMPTY_PACKAGE_NAME));
    }

    @Test
    void shouldThrowExceptionWhenBeanContainerDoesNotHaveBeanForInjection() {
        assertThrows(NoSuchBeanDefinitionException.class, () ->
          new AnnotationConfigurationApplicationContext(NO_BEAN_FOR_INJECTION_PACKAGE_NAME));
    }

    @Test
    void shouldThrowExceptionWhenBeanContainerContainsTwoBeansForInjectionWithoutSpecifyingName() {
        assertThrows(NoUniqueBeanDefinitionException.class, () ->
          new AnnotationConfigurationApplicationContext(NOT_UNIQUE_BEAN_FOR_INJECTION_PACKAGE_NAME));
    }

    @Test
    void shouldInjectBean() {
        var context = new AnnotationConfigurationApplicationContext(MOCK_PACKAGE);
        HarryPotter harryPotterBook = (HarryPotter) (context.getBean("harryPotterBook"));

        assertNotNull(harryPotterBook);
        assertNotNull(harryPotterBook.getQuoter());
        assertEquals(HarryPotterQuoter.class, harryPotterBook.getQuoter().getClass());
    }
}