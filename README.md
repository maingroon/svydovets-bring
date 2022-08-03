# Bring, svydovets team
 The Bring is the [Inversion of Control (IoC) framework](https://en.wikipedia.org/wiki/Inversion_of_control#:~:text=In%20software%20engineering%2C%20inversion%20of,control%20from%20a%20generic%20framework.).  
 The Bring takes care of [Dependency Injection (DI)](https://en.wikipedia.org/wiki/Dependency_injection) into the Bean and manages full Bean lifecycle.  

## Description for annotations: 
**[@Configuration](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Configuration.java)**
> This annotation is a marker that applicable to class and by this annotation the Bring is looking for beans configurations.

**[@Bean](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Bean.java)**
> This annotation is usually applicable to POJO that cannot be market as **[@Component](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Component.java)**
> and in cases when we need to provide custom initialization for the POJO object.
> 
>> ### Example:  
>> **Imagine that you have external library, and you want to pass to the Bring all the control of lifecycle of this POJO:**  
>>> Step 1 -> Create the configuration class (class that is marked as **[@Configuration](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Configuration.java)**)  
>>> ![bean_config](https://user-images.githubusercontent.com/55089853/180369044-7af2ffe2-fc35-4c95-a625-909702b92db6.png)
>> 
>>> Step 2 -> Create the method that returns required POJO and mark this method as ***@Bean***  
>>> ![name_without_name](https://user-images.githubusercontent.com/55089853/180370309-98a8e083-d631-425f-beaf-900a9a34f767.png)  
>>> If you need to pass some unique name -> ***@Bean("bean_name")***  
>>> ![bean_with_name](https://user-images.githubusercontent.com/55089853/180370406-2f9af395-5cbb-4f6a-8362-169a848a2c57.png)
>>
>>> Step 3 -> return initialized POJO  
>>> ![bean_return_underlined](https://user-images.githubusercontent.com/55089853/180369662-65dcf06c-e7be-45c2-bae4-d4bdea5d0934.png)
>>
>>> Full class  
>>> ![bean_full](https://user-images.githubusercontent.com/55089853/180369802-cb99a79d-5a9f-414e-8d58-ab30d57fc246.png)


**[@Component](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Component.java)**
> This annotation is applicable to class. The Bring will create a bean and will manage all lifecycle of this bean.  
> 
>> ### Example:
>> Annotate class as **@Component**, in case presented on the picture below, the name for the bean will be 
>> *"harryPotterQuoter"*  
>> ![component](https://user-images.githubusercontent.com/55089853/180456037-c38d2cc1-c59c-4c87-b028-122dc388b407.png)
>> 
>> You can pass your custom name for bean. This is helpful in cases when tou have beans that implement one interface,
>> this works in conjunction with **[@Inject](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Inject.java)**
>> annotation.
>> In the case on the picture below the bean name will be *"hp""*
>> ![component_name](https://user-images.githubusercontent.com/55089853/180457950-8b18416e-0f73-4fd4-af2c-b42d978e1ef9.png)


**[@Inject](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Inject.java)**
> This annotation makes [Dependency Injection (DI)](https://en.wikipedia.org/wiki/Dependency_injection) into bean.   
> This annotation applicable in classes that marked as *@Component* or if we create a bean (via *@Bean* annotation) for 
> the class where we use it. You can mark by this annotation a field that is a bean or an interface that have realization
> that is marked as *@Component* or *@Bean* and the [Bring-Bean-Container](#bring-bean-container) contains it, then the
> Bring will inject it. If [Bring-Bean-Container](#bring-bean-container) doesn't have it -> the 
> [NoSuchBeanDefinitionException](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/exception/NoSuchBeanDefinitionException.java)
> will be thrown. If an interface contains more than one realization, then the 
> [NoUniqueBeanDefinitionException](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/exception/NoUniqueBeanDefinitionException.java)
> will be thrown.
>> ### Example:  
>> ![quoter](https://user-images.githubusercontent.com/55089853/180592649-d162226e-1871-458a-a448-cda86ac68a6a.png)
>> ![quoter_r_1](https://user-images.githubusercontent.com/55089853/180594022-98e33b75-28ec-43fe-8df2-4d3abd7ed2af.png)
>> ![quoter_r_2](https://user-images.githubusercontent.com/55089853/180594024-98219402-ce96-4d1c-b8ae-f249abbe6ab2.png)
>> ![hp_inject_exception](https://user-images.githubusercontent.com/55089853/180594027-eb1fa8a6-7874-4af2-a976-d6e05b81ef49.png)
>
> You can pass name of the Bean via annotation value *@Inject(bean_name)* and then the Bring will know, that 'here should
> be injected bean with this name'.
>> ### Example:
>> ![hp_injected_quoterpng](https://user-images.githubusercontent.com/55089853/180594031-483426c6-149c-47ee-a2df-051822a86fba.png)

## Bring-Bean-Container
> *Bring-Bean-Container* is a Map, that contains *bean name* and *Bean* and persists in the ApplicationContext realization.
> TODO: should be added a screenshot

## Description for the [ApplicationContext](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/context/ApplicationContext.java): 
> The *ApplicationContext* is the main interface in the Bring. It has realizations for some configurations of bean 
> lifecycle regulations and control. Nowadays, the Bring contains only AnnotationConfigurationApplicationContext but 
> we are on the way of evolving and will add other ConfigurationApplicationContext (for instance XmlConfigurationApplicationContext).
> TODO: add more description for this and screenshots.

## Simple example of code:
> TODO: need to create a description with screenshots with step-by-step creation flow and description of why we are 
> using it.

## Install the svydovets-bring-framework:
1. Add maven repository to your build.gradle. You need your github username and github packages read token.
```
repositories {
  mavenCentral()
  maven {
    url = uri("https://maven.pkg.github.com/maingroon/svydovets-bring")
      credentials {
        username = System.getenv("GH_USER")
        password = System.getenv("GH_PACKAGES_READ_TOKEN")
      }
   }
}
```
2. Add dependency:
```
implementation 'com.svydovets:svydovets-bring-framework:0.0.1-SNAPSHOT'
```