package cz.poptavka.sample.domain;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.util.reflection.ReflectionUtils;
import junit.framework.TestCase;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Basic checks for domain classes.
 * The abstract classes, enums and test classes are ignored.
 *
 * @author Juraj Martinka
 *         Date: 28.1.11
 */
public class DomainObjectTest {

    /**
     * Name of package where ALL domain classes are located.
     */
    private static final String DOMAIN_PACKAGE_NAME = "cz.poptavka.sample.domain";
    private static final String FIELD_VALIDATIONS_SEPARATOR = ", ";

    private static final String POPTAVKA_CORE_PACKAGE = "cz.poptavka";


    /**
     * Set of all domain objects in application.
     * Enums are NOT presented!
     */
    private static Set<BeanDefinition> domainObjects = new HashSet<BeanDefinition>();

    /**
     * Set of all domain objects' classes. It is presented because many times we need a reflection.
     */
    private static Set<Class<?>> domainObjectsClasses = new HashSet<Class<?>>();
    private static final String ENTITY_ANNOTATION = "Entity";
    private static final String JPA_ENTITY_ANNOTATION = "javax.persistence.Entity";


    /**
     * Load all domain objects in application.
     */
    @BeforeClass
    public static void setUp() throws Exception {
        final ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(true);
        // do not check abstract classes
        provider.addIncludeFilter(new AbstractClassTestingTypeFilter() {
            @Override
            protected boolean match(ClassMetadata metadata) {
                return true;
            }
        });

        final Set<BeanDefinition> domainObjects = provider.findCandidateComponents(DOMAIN_PACKAGE_NAME);

        if (CollectionUtils.isEmpty(domainObjects)) {
            Assert.fail("Unable to find package with DOMAIN objects");
        }

        for (BeanDefinition domainObject : domainObjects) {
            final Class<?> domainObjectClass =
                    DomainObjectTest.class.getClassLoader().loadClass(domainObject.getBeanClassName());
            if (domainObjectClass.isEnum() || isTestClass(domainObjectClass) || isAspectjClosure(domainObjectClass)) {
                continue;
            }
            domainObjects.add(domainObject);
            domainObjectsClasses.add(domainObjectClass);
        }
    }

    /**
     * Tests if all persistent entities inherit from base class {@link DomainObject}.
     *
     * @throws ClassNotFoundException
     */
    @Test
    public void testDomainObjects() throws ClassNotFoundException {
        final ArrayList<Class> violationClasses = new ArrayList<Class>(); // classes that violates tested requirement
        for (Class<?> domainObjectClass : domainObjectsClasses) {
            if (isPersistenceEntity(domainObjectClass)) {
                if (!DomainObject.class.isAssignableFrom(domainObjectClass)) {
                    violationClasses.add(domainObjectClass);
                }
            }
        }

        Assert.assertTrue("Entity classes: " + violationClasses + " do not extend DomainObject",
                violationClasses.isEmpty());
    }


    /**
     * Tests if all domain classes have getter and setter for each property.
     */
    @Test
    public void testGettersAndSetters() {

        // contains all violations to checked rules in following form:
        // for each class the corrupted fields and each field has an error message assigned
        Map<Class, Map<Field, String>> violations = new HashMap<Class, Map<Field, String>>();

        for (Class<?> domainObjectClass : domainObjectsClasses) {
            HashMap<Field, String> classFieldsViolations = new HashMap<Field, String>(); // all violations for one class
            for (Field field : domainObjectClass.getDeclaredFields()) {
                if (isPersistenceProperty(domainObjectClass, field)) {
                    final boolean hasGetter = ReflectionUtils.hasGetter(domainObjectClass, field.getName());
                    final boolean hasSetter = ReflectionUtils.hasSetter(domainObjectClass, field.getName());
                    if (!hasGetter) {
                        if (!hasSetter) {
                            classFieldsViolations.put(field, "No Getter and Setter method");
                        } else {
                            classFieldsViolations.put(field, "No Getter method");
                        }
                    } else if (!hasSetter) {
                        classFieldsViolations.put(field, "No setter method");
                    }

                }
            }
            if (!classFieldsViolations.isEmpty()) {
                violations.put(domainObjectClass, classFieldsViolations);
            }
        }

        Assert.assertTrue("Missing gettters or setters in following classes: "
                + getAccessorsViolationsMessage(violations),
                violations.isEmpty());
    }


    /**
     * Tests if domain objects don't use prohibited classes.
     * <p>
     * Domain objects are used with GWT module, therefore they can be associated only to the other domain objects
     * or external classes.
     * It means, the cannot use any classes from <code>cz.poptavka.**</code> with the one exception:
     * <code>cz.poptavka.sample.domain.**</code>
     */
    @Test
    public void testRestrictedDomainObjectsDependencies() {
        // contains all violations to checked rules in following form:
        // for each class the corrupted fields and each field has an error message assigned
        Map<Class, Map<Field, String>> violations = new HashMap<Class, Map<Field, String>>();

        for (Class<?> domainObjectClass : domainObjectsClasses) {
            HashMap<Field, String> classFieldsViolations = new HashMap<Field, String>(); // all violations for one class

            for (Field field : domainObjectClass.getDeclaredFields()) {
                if (isInProhibitedPackage(field.getType())) {
                    classFieldsViolations.put(field, "Prohibited field type [" + field.getType() + "]"
                            + " is not from allowed packages");
                }
            }

            if (!classFieldsViolations.isEmpty()) {
                violations.put(domainObjectClass, classFieldsViolations);
            }
        }

        Assert.assertTrue("Usage of prohibited classes in following classes: "
                + getAccessorsViolationsMessage(violations),
                violations.isEmpty());
    }


    /**
     * Tests if all persistent entities use correct {@link Entity} annotation and not e.g. specific Hibernate
     * annotation {@link org.hibernate.annotations.Entity} or any other.
     */
    @Test
    public void testCorrectEntityAnnotation() {
        final ArrayList<Class> violationClasses = new ArrayList<Class>(); // classes that violates tested requirement
        for (Class<?> domainObjectClass : domainObjectsClasses) {
            for (Annotation annotation : domainObjectClass.getAnnotations()) {
                if (annotation.annotationType().getName().endsWith(ENTITY_ANNOTATION)
                        &&  ! annotation.annotationType().getName().equals(JPA_ENTITY_ANNOTATION)) {
                    // incorrect "Entity" annotation is used!
                    violationClasses.add(domainObjectClass);
                    // jump to the next domain class
                    break;
                }
            }
        }

        Assert.assertTrue("Entity classes: " + violationClasses + " do not use correct 'javax.persistence.Entity'"
                + " annotation!",
                violationClasses.isEmpty());
    }


    /**
     * Check if each domain object has meaningful (i.e. it overrides the default {@link Object#toString()})
     * toString() method.
     */
    @Test
    public void testToStringOverriden() {
        final ArrayList<Class> violationClasses = new ArrayList<Class>(); // classes that violates tested requirement
        for (Class<?> domainObjectClass : domainObjectsClasses) {
            try {
                final Method toStringMethod = domainObjectClass.getMethod("toString");
                if (Object.class.equals(toStringMethod.getDeclaringClass())) {
                    // Object#toString() is not overriden in domainObjectClass or its superclass
                    violationClasses.add(domainObjectClass);
                } else if (! domainObjectClass.equals(toStringMethod.getDeclaringClass())) {
                    // toString() method  is not overriden directly in domainObjectClass - inform user
                    System.err.println("Class [" + domainObjectClass.getName() + "] does not override"
                            + " 'toString()' method.");
                }
            } catch (NoSuchMethodException e) {
                violationClasses.add(domainObjectClass);
            }
        }

        Assert.assertTrue("Classes: " + violationClasses + " do not override 'toString()' method!",
                violationClasses.isEmpty());
    }


    //-------------------------------- HELPER METHODS ------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    private static boolean isInProhibitedPackage(Class<?> fieldType) {
        if (fieldType.getName().startsWith(POPTAVKA_CORE_PACKAGE)) {
            // this is poptavka's class - check if it is in domain package
            return ! fieldType.getName().startsWith(DOMAIN_PACKAGE_NAME);
        }

        // classes that are not poptavka's own classes cannot be reasonably checked.
        return false;
    }


    private static boolean isTestClass(Class<?> aClass) {
        return TestCase.class.isAssignableFrom(aClass)
                || aClass.getName().endsWith("Test");
    }

    private static boolean isAspectjClosure(Class<?> aClass) {
        return aClass.getName().contains("AjcClosure");
    }



    private static String getAccessorsViolationsMessage(Map<Class, Map<Field, String>> violations) {

        StringBuilder violationMessage = new StringBuilder("[");
        for (Map.Entry<Class, Map<Field, String>> classViolation : violations.entrySet()) {
            violationMessage.append("\nCLASS [" + classViolation.getKey().getName() + "]:\n    [");
            for (Map.Entry<Field, String> fieldViolation : classViolation.getValue().entrySet()) {
                violationMessage.append("FIELD [" + fieldViolation.getKey().getName()
                        + "] -- " + fieldViolation.getValue() + FIELD_VALIDATIONS_SEPARATOR);
            }
            // remove trailing separator
            violationMessage.delete(violationMessage.length() - FIELD_VALIDATIONS_SEPARATOR.length(),
                    violationMessage.length());

            violationMessage.append("]");
        }

        return violationMessage.append("]").toString();
    }


    /**
     * Checks if given class is a persistent entity.
     * @param aClass
     * @return
     */
    private static boolean isPersistenceEntity(Class aClass) {
        if (aClass != null) {
            return  aClass.getAnnotation(Entity.class) != null;
        }

        return false;
    }


    private static boolean isPersistenceProperty(Class aClass, Field field) {
        if (Modifier.isStatic(field.getModifiers())
                || field.isSynthetic()
                || Modifier.isFinal(field.getModifiers())
                || Modifier.isTransient(field.getModifiers())) {
            return false;
        }

        if (field.getAnnotation(Transient.class) != null) {
            // we do not want check transient fields
            return false;
        }

        return !(aClass.getAnnotation(Entity.class) == null
                && aClass.getAnnotation(org.hibernate.annotations.Entity.class) == null);

    }
}
