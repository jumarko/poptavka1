package com.eprovement.poptavka.util.reflection;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * This utility class provides handy methods for reflection techniques.
 * <p/>
 * These methods are extensively used in {@link com.eprovement.poptavka.domain.DomainObjectTest}.
 *
 * @author Juraj Martinka
 *         Date: 1.5.11
 */
public final class ReflectionUtils {

    private static final String GETTER_PREFIX = "get";
    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_BOOLEAN_PREFIX = "is";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);


    private ReflectionUtils() {
        // utility class - DO NOT INSTANTIATE!
    }


    /**
     * Tries to find GETTER for property with name <code>propertyName</code> on class <code>aClass</code>.
     * <p>
     *     It will first search for a method prefixed with <code>get</code>
     *     if there's none, it will search for a method prefixed with
     *     <code>is</code>. If even this isn't found, <code>null</code> is
     *     returned.
     * </p>
     *
     * @param aClass
     * @param propertyName
     * @return
     */
    public static Method getGetterMethod(Class aClass, String propertyName) {
        return getAccessor(aClass, propertyName, new String[]{GETTER_PREFIX, GETTER_BOOLEAN_PREFIX});
    }

    /**
     * Tries to find SETTER for property with name <code>propertyName</code> on class <code>aClass</code>.
     *
     * @param aClass
     * @param propertyName
     * @return
     */
    public static Method getSetterMethod(Class aClass, String propertyName) {
        return getAccessor(aClass, propertyName, new String[]{SETTER_PREFIX});
    }

    public static boolean hasGetter(Class aClass, String fieldName) {
        return null != getGetterMethod(aClass, fieldName);
    }


    public static boolean hasSetter(Class aClass, String fieldName) {
        return null != getSetterMethod(aClass, fieldName);
    }


    /**
     * Find value of given property with name <code>propertyName</code> if such a public property (with public GETTER
     * method) exists.
     *
     * @param propertyName
     * @return value of given property for given object IF such property is accessible, otherwise return null
     */
    public static Object getValue(Object valueObject, String propertyName) {
        Preconditions.checkArgument(valueObject != null, "Object from which we want to get value must not be null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(propertyName), "Property name must not be blank!");

        final Method getterMethod = getGetterMethod(valueObject.getClass(), propertyName);
        if (getterMethod == null) {
            return null;
        }
        try {
            return getterMethod.invoke(valueObject);
        } catch (Exception e) {
            LOGGER.error("Exception [" + e.getMessage() + "] has been thrown while trying to get value of property ["
                    + propertyName + "].");
            return null;
        }
    }


    public static List<Class> findAllClasses(String basePackage) throws RuntimeException {
        return findClasses(basePackage, new ClassFilter() {
            @Override
            public boolean match(ClassMetadata classMetadata) {
                return true;
            }
        });
    }


    public static List<Class> findClasses(String basePackage, ClassFilter classFilter) throws RuntimeException {
        Validate.notEmpty(basePackage, "basePackage cannot be empty!");
        Validate.notNull(classFilter, "classFilter cannot be null!");

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        List<Class> candidates = new ArrayList<Class>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + resolveBasePackage(basePackage) + "/" + "**/*.class";
        Resource[] resources = new Resource[0];
        try {
            resources = resourcePatternResolver.getResources(packageSearchPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = null;
                try {
                    metadataReader = metadataReaderFactory.getMetadataReader(resource);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (classFilter.match(metadataReader.getClassMetadata())) {
                        candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return candidates;
    }

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------

    private static Method getAccessor(Class aClass, String fieldName, String[] accessorPrefixes) {

        final List<Method> allPublicMethods = Arrays.asList(aClass.getMethods());
        final Field field = org.springframework.util.ReflectionUtils.findField(aClass, fieldName);
        if (field == null) {
            return null;
        }

        for (String prefix : accessorPrefixes) {
            for (Method publicMethod : allPublicMethods) {
                if (publicMethod.getName().endsWith(ReflectionUtils.getFieldNameFirstLetterUpperCase(field))
                        && publicMethod.getName().startsWith(prefix)) {
                    return publicMethod;
                }
                if (publicMethod.getName().endsWith(removeStartingIs(field.getName()))
                        && publicMethod.getName().startsWith(prefix)) {
                    return publicMethod;
                }
            }
        }

        return null;
    }

    private static String getFieldNameFirstLetterUpperCase(Field field) {
        return field.getName().substring(0, 1).toUpperCase() // first letter with Upper case
                + field.getName().substring(1, field.getName().length()); // the rest of name without change
    }

    private static String removeStartingIs(String name) {
        if (name.startsWith("is")) {
            return name.substring(2);
        }
        return name;
    }


    private static String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

}
