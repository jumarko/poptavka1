package cz.poptavka.sample.util.collection;

import java.util.Arrays;
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
