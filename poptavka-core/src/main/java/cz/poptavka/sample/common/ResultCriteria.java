package cz.poptavka.sample.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Additional criteria that can be applied on the result - primary on operations that load data from database.
 *
 * @author Juraj Martinka
 *         Date: 30.4.11
 */
public final class ResultCriteria {

    /**
     * The collection of columns by which we want to order. The columns are applied in order as returned by
     * Collection iterator.
     * Map contains pairs (propertyName, OrderType). Ordering direction is specified by OrderType. Default OrderType
     * is ascending.
     * <p>
     *     It is a full responsibility of programmer (client) that each column specify in this collection
     *     represents some property on target entity.
     * <p>
     *     @see GenericServiceIntegrationTest for examples on using this criteria class.
     */
    private Map<String, OrderType> orderByColumns;


    /** Maximum number of results that should be return. */
    private Integer maxResults;

    /**
     * The position from which to start selection. This is usually used together with {@link #maxResults}.
     * Numbered from 0!
     */
    private Integer firstResult;


    public static class Builder {

        private Map<String, OrderType> orderByColumns = new HashMap<String, OrderType>();

        private Integer maxResults;

        private Integer firstResult;


        public Builder() {
        }

        public Builder orderByColumns(List<String> orderByColumns) {
            for (String orderByColumn : orderByColumns) {
                this.orderByColumns.put(orderByColumn, OrderType.ASC);
            }
            return this;
        }

        public Builder orderByColumns(Map<String, OrderType> orderByColumns) {
            this.orderByColumns = orderByColumns;
            return this;
        }

        public Builder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public Builder firstResult(Integer firstResult) {
            this.firstResult = firstResult;
            return this;
        }

        public  ResultCriteria build() {
            return new ResultCriteria(this);
        }
    }



    private ResultCriteria(Builder builder) {
        // fill all atributes from builder
        this.orderByColumns = builder.orderByColumns;
        this.maxResults = builder.maxResults;
        this.firstResult = builder.firstResult;
    }


    //---------------------------------- GETTERS AND SETTERS -----------------------------------------------------------

    public Map<String, OrderType> getOrderByColumns() {
        return orderByColumns;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public Integer getFirstResult() {
        return firstResult;
    }

}
