package cz.poptavka.sample.util.orm;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import java.util.Collection;

/**
 * Simplify creation of usage of Criteria API in Dao layer.
 * <strong>ALWAYS call {@link #buildCriteria(org.hibernate.Criteria, org.hibernate.criterion.Criterion...)}
 * before "evaluation" of criteria</strong>.
 * <p>
 * Its usage is exactly the same as of {@link Restrictions}
 * class but methods of this class automatically checks if argument are not empty and in that case they return null.
 * The conditions for determination when the value is "empty" are given by the method {@link #empty(Object)}.
 *
 * <p>
 * Each method comes with two variants: One that exactly match the related method from
 * {@link Restrictions} and another one with additional <code>boolean</code> parameter <code>predicate</code>
 * which can be used for evaluation of "emptyness". It means that new restriction will be created ONLY IF
 * that parameter is true.
 *
 * <p>
 * Example:<pre>
 *      RestrictionsHelper.buildCriteria(
 *              getHibernateSession().createCriteria(Client.class).createCriteria("person"),
 *              RestrictionsHelper.eq("firstName", clientSearchCritera.getName()),
 *              RestrictionsHelper.eq("lastName", clientSearchCritera.getSurName()))
 *              .list();
 * </pre>
 * Instead of more verbous:
 * <pre>
 *      final Criteria personCriteria = getHibernateSession().createCriteria(Client.class)
 *              .createCriteria("person");
 *      if (StringUtils.isNotBlank(clientSearchCritera.getName())) {
 *         personCriteria.add(Restrictions.eq("firstName", clientSearchCritera.getName()));
 *      }
 *      if (StringUtils.isNotBlank(clientSearchCritera.getSurName())) {
 *          personCriteria.add(Restrictions.eq("lastName", clientSearchCritera.getSurName()));
 *      }
 *      personCriteria.list();
 * </pre>
 *
 * @author Juraj Martinka
 *         Date: 30.1.11
 */
public final  class RestrictionsHelper  {

    private RestrictionsHelper() {
        // DO NOT INSTANTIATE utility class!
    }


    /**
     * This method must be explicitly called before evaluations of criteria!!
     * @param criteria
     * @param criterions
     * @return
     */
    public static Criteria buildCriteria(Criteria criteria, Criterion... criterions) {
        if (criteria == null) {
            return null;
        }
        if (criterions == null || criterions.length == 0) {
            return criteria;
        }

        // skip empty criterions
        for (Criterion criterion : criterions) {
            if (criterion != null) {
                criteria.add(criterion);
            }
        }

        return criteria;
    }


    public static <T> Criterion eq(String propertyName, String value) {
        return RestrictionsHelper.eq(propertyName, value, empty(value));
    }

    public static <T> Criterion eq(String propertyName, String propertyValue, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.eq(propertyName, propertyValue);
    }



    /**
     * Apply an "equal" constraint to the identifier property.
     * @param value
     * @return Criterion
     */
    public static Criterion idEq(Object value) {
        return RestrictionsHelper.idEq(value, empty(value));
    }

    /**
     * Apply an "equal" constraint to the identifier property.
     * @param value
     * @return Criterion
     */
    public static Criterion idEq(Object value, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.idEq(value);
    }

    /**
     * Apply an "equal" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression eq(String propertyName, Object value) {
        return RestrictionsHelper.eq(propertyName, value, empty(value));
    }

    /**
     * Apply an "equal" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression eq(String propertyName, Object value, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.eq(propertyName, value);
    }

    /**
     * Apply a "not equal" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression ne(String propertyName, Object value) {
        return RestrictionsHelper.ne(propertyName, value, empty(value));
    }

    /**
     * Apply a "not equal" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression ne(String propertyName, Object value, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.ne(propertyName, value);
    }


    /**
     * Apply a "like" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression like(String propertyName, Object value) {
        return RestrictionsHelper.like(propertyName, value, empty(value));
    }

    /**
     * Apply a "like" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression like(String propertyName, Object value, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.like(propertyName, value);
    }

    /**
     * Apply a "like" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression like(String propertyName, String value, MatchMode matchMode) {
        return RestrictionsHelper.like(propertyName, value, matchMode, empty(value));
    }

    /**
     * Apply a "like" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression like(String propertyName, String value, MatchMode matchMode, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.like(propertyName, value, matchMode);
    }

    /**
     * A case-insensitive "like", similar to Postgres <tt>ilike</tt>
     * operator.
     *
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static Criterion ilike(String propertyName, String value, MatchMode matchMode) {
        return RestrictionsHelper.ilike(propertyName, value, matchMode, empty(value));
    }

    /**
     * A case-insensitive "like", similar to Postgres <tt>ilike</tt>
     * operator.
     *
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static Criterion ilike(String propertyName, String value, MatchMode matchMode, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.ilike(propertyName, value, matchMode);
    }

    /**
     * A case-insensitive "like", similar to Postgres <tt>ilike</tt>
     * operator.
     *
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static Criterion ilike(String propertyName, Object value) {
        return RestrictionsHelper.ilike(propertyName, value, empty(value));
    }

    /**
     * A case-insensitive "like", similar to Postgres <tt>ilike</tt>
     * operator.
     *
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static Criterion ilike(String propertyName, Object value, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.ilike(propertyName, value);
    }

    /**
     * Apply a "greater than" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression gt(String propertyName, Object value) {
        return RestrictionsHelper.gt(propertyName, value, empty(value));
    }

    /**
     * Apply a "greater than" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression gt(String propertyName, Object value, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.gt(propertyName, value);
    }

    /**
     * Apply a "less than" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression lt(String propertyName, Object value) {
        return RestrictionsHelper.lt(propertyName, value, empty(value));
    }

    /**
     * Apply a "less than" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression lt(String propertyName, Object value, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.lt(propertyName, value);
    }

    /**
     * Apply a "less than or equal" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression le(String propertyName, Object value) {
        return RestrictionsHelper.le(propertyName, value, empty(value));
    }

    /**
     * Apply a "less than or equal" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression le(String propertyName, Object value, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.le(propertyName, value);
    }

    /**
     * Apply a "greater than or equal" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression ge(String propertyName, Object value) {
        return RestrictionsHelper.ge(propertyName, value, empty(value));
    }

    /**
     * Apply a "greater than or equal" constraint to the named property.
     * @param propertyName
     * @param value
     * @return Criterion
     */
    public static SimpleExpression ge(String propertyName, Object value, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.ge(propertyName, value);
    }

    /**
     * Apply a "between" constraint to the named property.
     *
     * @param propertyName
     * @param lo value
     * @param hi value
     * @return Criterion or null if any bounds is empty
     */
    public static Criterion between(String propertyName, Object lo, Object hi) {
        return RestrictionsHelper.between(propertyName, lo, hi, empty(lo) || empty(hi));
    }

    /**
     * Apply a "between" constraint to the named property.
     *
     * @param propertyName
     * @param lo value
     * @param hi value
     * @return Criterion or null if any bounds is empty
     */
    public static Criterion between(String propertyName, Object lo, Object hi, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.between(propertyName, lo, hi);
    }


    /**
     * Apply an "in" constraint to the named property.
     * @param propertyName
     * @param values
     * @return Criterion
     */
    public static Criterion in(String propertyName, Object[] values) {
        return RestrictionsHelper.in(propertyName, values, empty(values));
    }

    /**
     * Apply an "in" constraint to the named property.
     * @param propertyName
     * @param values
     * @return Criterion
     */
    public static Criterion in(String propertyName, Object[] values, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.in(propertyName, values);
    }

    /**
        * Apply an "in" constraint to the named property.
        * @param propertyName
        * @param values
        * @return Criterion
        */
    public static Criterion in(String propertyName, Collection values) {
        return RestrictionsHelper.in(propertyName, values, empty(values));
    }


    /**
     * Apply an "in" constraint to the named property.
     * @param propertyName
     * @param values
     * @return Criterion
     */
    public static Criterion in(String propertyName, Collection values, boolean isEmpty) {
        if (isEmpty) {
            return null;
        }

        return Restrictions.in(propertyName, values);
    }



   //-------------------------------------------------------------------------------------------------------------------
   /**
     * Checks wether given <code>value</code> is empty.
     *
     * @param value
     * @return
     */
    public static boolean empty(Object value) {
        if (value == null) {
            return true;
        }

        if (value instanceof String) {
            return StringUtils.isBlank((String) value);
        } else if (value instanceof Collection) {
            return ((Collection) value).isEmpty();
        } else if (value.getClass().isArray()) {
            return true;
        }

        return false;
    }




    //------------------- simple test -----------------------
    public static void main(String[] args) {
        System.out.println("array empty: " + empty(new String[] {}));
        System.out.println("array empty: " + empty(new short[] {}));
        System.out.println("array empty: " + empty(new boolean[] {}));
        System.out.println("array empty: " + empty(new short[] {}));
        System.out.println("array empty: " + empty(new byte[] {}));
        System.out.println("array empty: " + empty(new long[] {}));
        System.out.println("array empty: " + empty(new double[] {}));
        System.out.println("array empty: " + empty(new float[] {}));
        System.out.println("array empty: " + empty(new int[] {}));

    }
}
