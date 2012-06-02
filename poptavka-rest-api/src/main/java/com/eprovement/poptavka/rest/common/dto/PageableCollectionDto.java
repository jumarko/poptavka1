/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.common.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PageableCollectionDto<Dto> {
    private final Collection<Dto> collection;
    private final Map<String, String> links = new HashMap<String, String>();
    private final Paging paging;


    public PageableCollectionDto(Collection<Dto> collection, Paging paging, Map<String, String> links) {
        this.collection = Collections.unmodifiableCollection(collection);
        this.paging = paging;
        this.links.putAll(links);
    }

    public Collection<Dto> getCollection() {
        return collection;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public Paging getPaging() {
        return paging;
    }


    /**
     * Class represents paging object used for resources that support paging.
     */
    public static class Paging {
        private final int offset;
        private final int count;
        private final String next;

        /**
         * Creates new Paging object used for navigability of Pageable Collections.
         *
         * @param offset the ordering number of first element in collection that is described by this paging object
         * @param count total number of elements in collection - less or equal to "limit" request parameter specified
         *              when retrieving page
         * @param nextPageLink link to the next page, null if there is not any next page
         *
         * @see PageableCollectionDto
         */
        public Paging(int offset, int count, String nextPageLink) {
            this.offset = offset;
            this.count = count;
            this.next = nextPageLink;
        }

        public int getOffset() {
            return offset;
        }

        public int getCount() {
            return count;
        }

        public String getNext() {
            return next;
        }
    }

}
