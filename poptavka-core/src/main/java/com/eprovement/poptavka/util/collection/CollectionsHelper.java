package com.eprovement.poptavka.util.collection;

import com.eprovement.poptavka.domain.common.DomainObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public final class CollectionsHelper {

    private CollectionsHelper() {
        // utility class - DO NOT INSTANTIATE!
    }

    public static boolean containsOnlyNulls(List<?> objects) {
        if (objects != null) {
            return (containsNulls(objects));
        }

        return true;
    }


    public static boolean containsOnlyNulls(Object[] objects) {
        if (objects != null) {
            return containsNulls(Arrays.asList(objects));
        }

        return true;
    }

    /**
     * Takes given collection of domain objects and transform it to the collection containing IDs
     * of those domain objects.
     * @param domainObjects
     * @param <T> any descendant of {@link DomainObject}
     * @return collection containing IDs of given domain objects
     */
    public static <T extends DomainObject> Collection<Long> getCollectionOfIds(Collection<T> domainObjects) {
        if (CollectionUtils.isEmpty(domainObjects)) {
            return Collections.emptyList();
        }

        return CollectionUtils.collect(domainObjects, new Transformer() {
            @Override
            public Object transform(Object input) {
                if (!(input instanceof DomainObject)) {
                    throw new IllegalStateException("Element in collection must be of type=" + DomainObject.class
                            + ". Unexpected type found: " + (input != null ? input.getClass() : null));
                }
                return ((DomainObject) input).getId();
            }
        });
    }


    /**
     * Convert to Set - this operation removes the duplicates in original list.
     * @param list
     * @return
     */
    public static <T> Set<T> converToSet(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptySet();
        }
        return new HashSet<T>(list);
    }


    //-------------------------------- HELPER METHODS ------------------------------------------------------------------
    private static boolean containsNulls(List<?> objects) {
        for (Object object : objects) {
            if (object != null)  {
                return false;
            }
        }

        return true;
    }


}
