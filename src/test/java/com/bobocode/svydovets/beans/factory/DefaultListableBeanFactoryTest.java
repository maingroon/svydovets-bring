package com.bobocode.svydovets.beans.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bobocode.svydovets.beans.scanner.quoter.bookshelfs.FantasyBookshelf;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.scanner.BeanPostprocessorScanner;
import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.beans.scanner.ConfigurationBeanScanner;
import com.bobocode.svydovets.beans.scanner.quoter.books.BeanPostprocessor1;
import com.bobocode.svydovets.beans.scanner.quoter.books.BeanPostprocessor2;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import org.mockito.Mockito;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultListableBeanFactoryTest {

    public static final String ROOT_MOCK_PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";
    public static final String PACKAGE_WITH_POSTPROCESSORS = "com.bobocode.svydovets.beans.scanner.quoter.book";
    public static final String EXCEPTION_INJECT_MOCK_PACKAGE =
      "com.bobocode.svydovets.beans.scanner.exception.bookshelves.inject";
    private Map<String, BeanDefinition> definitionMap;
    private BeanFactory factory;
    private BeanPostprocessorScanner postprocessorScanner;

    @BeforeAll
    public void setUp() {
        var componentsScanResult = new ComponentBeanScanner().scan(ROOT_MOCK_PACKAGE);
        var configScanResult = new ConfigurationBeanScanner().scan(ROOT_MOCK_PACKAGE);
        definitionMap = Stream.of(componentsScanResult, configScanResult)
          .flatMap(map -> map.entrySet().stream())
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        postprocessorScanner = new BeanPostprocessorScanner();
    }

    @Test
    void shouldCreateNewBeansMapFromBeanDefinitionsMaps() {
        // WHEN

        factory = new DefaultListableBeanFactory(postprocessorScanner.scan(ROOT_MOCK_PACKAGE));
        var beans = factory.createBeans(definitionMap);

        // THEN
        assertTrue(beans.containsKey("hp"));
        assertTrue(beans.get("hp").getClass().isAssignableFrom(HarryPotterQuoter.class));
        assertTrue(beans.containsKey("discworld"));
        assertTrue(beans.containsKey(FantasyBookshelf.class.getName()));
        assertEquals(8, beans.size());
    }

    @Test
    void configDeclaredBeanWithConstructorInjectionFail() {
        var configScanResult = new ConfigurationBeanScanner().scan(EXCEPTION_INJECT_MOCK_PACKAGE);
        factory = new DefaultListableBeanFactory(postprocessorScanner.scan(ROOT_MOCK_PACKAGE));
        assertThrowsExactly(UnsupportedOperationException.class, () -> factory.createBeans(configScanResult),
          "Creating bean instance with other injected beans is not yet supported");
    }

    @Test
    void shouldCallPostProcessBeforeInitializationAfterCreationBeans() {
        BeanPostprocessor1 beanPostprocessor1 = Mockito.mock(BeanPostprocessor1.class);
        BeanPostprocessor2 beanPostprocessor2 = Mockito.mock(BeanPostprocessor2.class);

        factory = new DefaultListableBeanFactory(Set.of(beanPostprocessor1, beanPostprocessor2));
        Map<String, Object> beans = factory.createBeans(new ComponentBeanScanner().scan(PACKAGE_WITH_POSTPROCESSORS));

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Mockito.verify(beanPostprocessor1, Mockito.times(1)).postProcessBeforeInitialization(entry.getValue(), entry.getKey());
            Mockito.verify(beanPostprocessor2, Mockito.times(4)).postProcessBeforeInitialization(entry.getValue(), entry.getKey());
        }
    }
}
