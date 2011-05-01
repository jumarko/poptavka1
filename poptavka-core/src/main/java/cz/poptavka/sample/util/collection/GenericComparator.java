package cz.poptavka.sample.util.collection;

import cz.poptavka.sample.util.reflection.ReflectionUtils;

import java.util.Comparator;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 1.5.11
 */
public class GenericComparator<T> implements Comparator<T> {


    private final Set<String> orderByProperties;

    public GenericComparator(Set<String> orderByProperties) {
        this.orderByProperties = orderByProperties;
    }

    @Override
    public int compare(T o1, T o2) {
        for (String orderByProperty : this.orderByProperties) {
            final Object o1PropertyValue = ReflectionUtils.getValue(o1, orderByProperty);
            final Object o2PropertyValue = ReflectionUtils.getValue(o2, orderByProperty);
            if (o1PropertyValue == null) {
                return -1;
            } else if (o2PropertyValue == null) {
                return 1;
            } else if (o1PropertyValue instanceof Comparable && o2PropertyValue instanceof Comparable) {
                if (!equalPropertyValues(o1PropertyValue, o2PropertyValue)) {
                    return ((Comparable) o1PropertyValue).compareTo(o2PropertyValue);
                }
            }

            // try comparison of next order by property
            continue;
        }

        // objects are either equal or cannot be compared to each other at all
        return 0;
    }

    private boolean equalPropertyValues(Object o1PropertyValue, Object o2PropertyValue) {
        if (o1PropertyValue == o2PropertyValue) {
            return true;
        }

        if (o1PropertyValue instanceof Comparable && o2PropertyValue instanceof Comparable) {
            return ((Comparable) o1PropertyValue).compareTo(o2PropertyValue) == 0;
        }

        return false;
    }

}
