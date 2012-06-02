/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.common.resource;

import com.eprovement.poptavka.rest.common.dto.PageableCollectionDto;

public interface PageableResource<Dto> {
    /**
     * Return one page of all objects of type {@code Domain}. Page is bounded by {@code offset} and {@code limit}.
     * Domain objects are by default ordered by id.
     * <p>
     *     Example:
     *     If we have 600 objects of type Demand in database and {@code offset = 100}, {@code limit = 200}
     *     then first element is that on the 101th position and last returned element is that on the 300th position.
     * </p>
     *
     * @param sort name of attribute by which the whole collection will be sorted - defaults to "id"
     * @param offset starting position for first element in returned page, defaults to "0"
     * @param limit max number of objects in returned page, defaults to 100.
     * @return collection of objects representing one particular page
     */
    PageableCollectionDto<Dto> listPage(String sort, int offset, int limit);


}
