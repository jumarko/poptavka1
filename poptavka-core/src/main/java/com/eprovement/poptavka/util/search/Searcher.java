package com.eprovement.poptavka.util.search;

import com.eprovement.poptavka.util.reflection.ReflectionUtils;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vojtech Hubr
 */
public final class Searcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Searcher.class);

    private Searcher() {
        // utility class - DO NOT INSTANTIATE!
    }

    /**
     * Applies a given <code>search</code> on the <code>haystack</code>
     *
     * Filtering currently does not support nesting and it only joins
     * the filters with and
     *
     * @param haystack the collection that should be searched (filtered, sorted
     * and trimmed)
     * @param search search object that specifies parameters of the desired
     * search
     * @return a list that is a result of filtering, sorting and trimming of
     * <code>haystack</code> as directed by <code>search</code>
     * @throws SearcherException
     */
    public static <E> List<E> searchCollection(Collection<E> haystack,
            Search search) throws SearcherException {
        List<E> result = new ArrayList<E>();
        if (CollectionUtils.isEmpty(haystack)) {
            return result;
        }
        Validate.notNull(search, "Search cannot be null");
        Validate.notNull(search.getSearchClass(), "Search class cannot be null");
        filterCollection(search, haystack, result);
        sortList(search, result);
        result = trim(search, result);

        return result;
    }

    /**
     * Applies a given <code>search</code> on the <code>haystack</code>'s keys and preserves
     * the key-value mapping.
     *
     * Filtering currently does not support nesting and it only joins
     * the filters with and
     *
     * @param haystack the collection whose keys should be searched (filtered, sorted
     * and trimmed)
     * @param search search object that specifies parameters of the desired
     * search
     * @return a <code>LinkedMap</code> that is a result of filtering, sorting and trimming of
     * <code>haystack</code> as directed by <code>search</code>
     */
    public static <K, V> LinkedHashMap<K, V> searchMapByKeys(Map<K, V> haystack,
            Search search) {
        List<K> keys = new LinkedList(haystack.keySet());
        keys = searchCollection(keys, search);
        LinkedHashMap<K, V> result = new LinkedHashMap();
        for (K key : keys) {
            result.put(key, haystack.get(key));
        }
        return result;
    }

    private static <E> void filterCollection(Search search, Collection<E> haystack,
            List<E> result) throws SearcherException {
        Map<Filter, List<Method>> getterChains = new HashMap<Filter,
                        List<Method>>();
        for (Filter filter : search.getFilters()) {
            getterChains.put(filter, getGetterChain(filter.getProperty(),
                    search.getSearchClass()));
        }
        for (E item : haystack) {
                // if no filter criteria are specified then item belongs to the result automatically
            boolean belongsToResult = getterChains.isEmpty();
            for (Map.Entry<Filter, List<Method>> entry : getterChains.entrySet()) {
                Filter filter = entry.getKey();
                List<Method> getterChain = entry.getValue();
                Class type = getType(getterChain);
                Object value = getValue(item, getterChain, false);
                value = type.cast(value);

                if (value == null || filter.getValue() == null) {
                    belongsToResult = value != null && filter.getValue() != null;
                    if (belongsToResult) {
                        continue;
                    } else {
                        break;
                    }
                }
                if (filter.getOperator() == Filter.OP_EQUAL) {
                    belongsToResult = value.equals(filter.getValue());
                    if (belongsToResult) {
                        continue;
                    } else {
                        break;
                    }
                }
                if (filter.getOperator() == Filter.OP_NOT_EQUAL) {
                    belongsToResult = !value.equals(filter.getValue());
                    if (belongsToResult) {
                        continue;
                    } else {
                        break;
                    }
                }
                if (filter.getOperator() == Filter.OP_IN) {
                    belongsToResult = ((Collection<Object>) filter.getValue())
                            .contains(value);
                    if (belongsToResult) {
                        continue;
                    } else {
                        break;
                    }
                }
                if (Arrays.asList(type.getInterfaces()).contains(
                        Comparable.class)) {
                    Comparable comparableValue = (Comparable) value;
                    if (filter.getOperator() == Filter.OP_GREATER_OR_EQUAL) {
                        belongsToResult = comparableValue.compareTo(
                                filter.getValue()) >= 0;
                        if (belongsToResult) {
                            continue;
                        } else {
                            break;
                        }
                    }
                    if (filter.getOperator() == Filter.OP_GREATER_THAN) {
                        belongsToResult = comparableValue.compareTo(filter.getValue()) > 0;
                        if (belongsToResult) {
                            continue;
                        } else {
                            break;
                        }
                    }
                    if (filter.getOperator() == Filter.OP_LESS_OR_EQUAL) {
                        belongsToResult = comparableValue.compareTo(filter.getValue()) <= 0;
                        if (belongsToResult) {
                            continue;
                        } else {
                            break;
                        }
                    }
                    if (filter.getOperator() == Filter.OP_LESS_THAN) {
                        belongsToResult = comparableValue.compareTo(filter.getValue()) < 0;
                        if (belongsToResult) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }
                if (value instanceof String) {
                    if (filter.getOperator() == Filter.OP_LIKE) {
                        String stringValue = (String) value;
                        String filterValue = (String) filter.getValue();
                        belongsToResult = stringValue.contains(filterValue);
                        if (belongsToResult) {
                            continue;
                        } else {
                            break;
                        }
                    }
                    if (filter.getOperator() == Filter.OP_ILIKE) {
                        String stringValue = ((String) value).toLowerCase();
                        String filterValue = ((String) filter.getValue()).toLowerCase();
                        belongsToResult = stringValue.contains(filterValue);
                        if (belongsToResult) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }

            }
            if (belongsToResult) {
                result.add(item);
            }
        }
    }

    /**
     * Sorts the <code>list</code> according to the sorts in <code>search</code>
     * It supports multiple sort criteria, ascending and descending order
     * and also case sensitive and insensitive sorting for strings.
     *
     * @param search defines the sorting parameters
     * @param list the list to be sorted in situ
     * @throws SearcherException
     */
    public static <E> void sortList(Search search, List<E> list) throws SearcherException {
        final Map<Sort, List<Method>> sortGetterChains = new HashMap<Sort, List<Method>>();
        for (Sort sort : search.getSorts()) {
            sortGetterChains.put(sort, getGetterChain(sort.getProperty(),
                    search.getSearchClass()));
        }

        Collections.sort(list, new Comparator<E>() {
            @Override
            public int compare(E e1, E e2) {
                for (Map.Entry<Sort, List<Method>> entry : sortGetterChains
                        .entrySet()) {
                    Sort sort = entry.getKey();
                    int ascDesc = sort.isDesc() ? -1 : 1;
                    List<Method> getterChain = entry.getValue();
                    Class type = getType(getterChain);
                    Object value1 = getValue(e1, getterChain,
                            sort.isIgnoreCase());
                    value1 = type.cast(value1);
                    Object value2 = getValue(e2, getterChain,
                            sort.isIgnoreCase());
                    value2 = type.cast(value2);
                    if (value1 == null && value2 == null) {
                        return 0;
                    }
                    if (value1 == null && value2 != null) {
                        return -1 * ascDesc;
                    }
                    if (value1 != null && value2 == null) {
                        return 1 * ascDesc;
                    }
                    int result = 0;
                    if (sort.isIgnoreCase() && type.equals(String.class)) {
                        String stringValue1 = ((String) value1).toLowerCase();
                        String stringValue2 = ((String) value2).toLowerCase();
                        result = stringValue1.compareTo(stringValue2) * ascDesc;
                    } else if (Arrays.asList(type.getInterfaces()).contains(
                            Comparable.class)) {
                        Comparable comparableValue1 = (Comparable) value1;
                        Comparable comparableValue2 = (Comparable) value2;
                        result = comparableValue1.compareTo(comparableValue2)
                                * ascDesc;
                    }
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }

        });
    }

    /**
     * Trims the <code>list</code> mainly as to support pagination.
     * @param search is used to obtain the (<code>firstResult</code> or
     * <code>page</code>) and <code>maxResults</code> properties
     * @param list the list to be trimmed (it is not modified by this method
     * @return the trimmed <code>list</code>
     */
    private static <E> List<E> trim(Search search, List<E> list) {
        if (search.getFirstResult() != -1 && search.getMaxResults() > 0) {
            int start = search.getFirstResult();
            if (start > list.size() - 1) {
                return new ArrayList<E>();
            }
            int end = Math.min(start + search.getMaxResults() + 1,
                    list.size());
            return list.subList(start, end);
        } else if (search.getPage() != -1 && search.getMaxResults() > 0) {
            int start = search.getPage() * search.getMaxResults();
            if (start > list.size() - 1) {
                return new ArrayList<E>();
            }
            int end = Math.min(start + search.getMaxResults() + 1,
                    list.size());
            return list.subList(start, end);
        }
        return list;
    }

    /**
     * Gets a <code>List</code> of getters that are to be applied in a
     * chain-like manner to get a property of the <code>baseClass</code>
     * the path to which is described by <code>property</code>
     * @param property path to the property to get the getters for
     * @param baseClass the class whose property we want to get
     * @return a <code>List<code> of methods - getter, to be applied to get the
     * desired property. First, we apply the method indexed with 0 to the object
     * of the <code>baseClass</code> itself, then we apply the method indexed
     * with 1 to the object we got by applying the first result and we continue
     * until we get to the end of the list. The final result is the desired
     * property.
     * @throws SearcherException
     */
    private static List<Method> getGetterChain(String property,
            Class baseClass) throws SearcherException {
        String[] properties = property.split("\\.");
        List<Method> getterChain = new ArrayList<Method>();
        for (int i = 0; i < properties.length; i++) {
            Method getter = ReflectionUtils.getGetterMethod(baseClass,
                    properties[i]);
            if (getter == null) {
                throw new SearcherException("Invalid path to property supplied."
                        + "\npath: \"" + property + "\""
                        + "\nThe class \"" + baseClass.getCanonicalName() + "\""
                        + "\nhas no property \"" + properties[i] + "\"");
            }
            baseClass = getter.getReturnType();
            getterChain.add(getter);
        }
        return getterChain;
    }

    /**
     * Gets the method of the desired property from the chain of getters
     * obtained by the <link>getGetterChain</link> method. It also ensures
     * that any primitive types are transformed to their corresponding
     * wrapping classes
     * @param getterChain it's last method's return type is used to obtain
     * the resulting class
     * @return a class corresponding to the type of the last object returned
     * by the supplied <code>getterChain</code>
     */
    private static Class getType(List<Method> getterChain) {
        Class type = getterChain.get(getterChain.size() - 1)
                .getReturnType();
        if (type.isPrimitive()) {
            type = ClassUtils.primitiveToWrapper(type);
        }
        return type;
    }

    /**
     * Executes the <code>getterChain</code> on the <code>item</code> and
     * returns the resulting value. If the <code>item</code> or any resulting
     * object on the evaluation path is <code>null</code>, the method will
     * return <code>null</code>.
     * @param item the object on which the evaluation path should start
     * @param getterChain the chain of getters to follow
     * @param toUpperCase whether to convert the result to uppercase provided
     * it is an instance of <code>String</code>
     * @return
     */
    private static <E> Object getValue(E item, List<Method> getterChain,
            boolean toUpperCase) {
        Object value = item;
        try {
            for (Method getter : getterChain) {
                if (value == null) {
                    return null;
                }
                value = getter.invoke(value);
            }
            if (toUpperCase && value instanceof String) {
                ((String) value).toUpperCase();
            }
        } catch (IllegalAccessException ex) {
            LOGGER.warn("Cannot get value from item={}", item, ex);
        } catch (IllegalArgumentException ex) {
            LOGGER.warn("Cannot get value from item={}", item, ex);
        } catch (InvocationTargetException ex) {
            LOGGER.warn("Cannot get value from item={}", item, ex);
        }
        return value;
    }
}
