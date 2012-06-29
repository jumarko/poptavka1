package com.eprovement.poptavka.util.reflection;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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
     *     It will find methods starting with "get" as well as "is". However, this is ambiguous, therefore
     *     be aware to not use this for class which has both such methods, e.g.:
     *     <pre>
     *         public Message getThreadRoot() { return threadRoot; }
     *         public boolean isThreadRoot() { return threadRoot == null; }
     *     </pre>
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


    //---------------------------------------------- HELPER METHODS ---------------------------------------------------

    private static Method getAccessor(Class aClass, String fieldName, String[] accessorPrefixes) {

        final List<Method> allPublicMethods = Arrays.asList(aClass.getMethods());
        final Field field = org.springframework.util.ReflectionUtils.findField(aClass, fieldName);
        if (field == null) {
            return null;
        }

        for (Method publicMethod : allPublicMethods) {
            if (publicMethod.getName().endsWith(ReflectionUtils.getFieldNameFirstLetterUpperCase(field))
                    && startsWithSomePrefix(publicMethod.getName(), accessorPrefixes)) {
                return publicMethod;
            }
            // special case are boolean properties which names start with "is" - they can have getter with same name
            if (field.getName().startsWith(GETTER_BOOLEAN_PREFIX) && publicMethod.getName().equals(field.getName())) {
                return publicMethod;
            }
        }

        return null;
    }

    private static boolean startsWithSomePrefix(String name, String[] allPrefixes) {
        if (StringUtils.isBlank(name)) {
            return false;
        }

        for (String prefix : allPrefixes) {
            if (name.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }


    private static String getFieldNameFirstLetterUpperCase(Field field) {
        return field.getName().substring(0, 1).toUpperCase() // first letter with Upper case
                + field.getName().substring(1, field.getName().length()); // the rest of name without change
    }


}
