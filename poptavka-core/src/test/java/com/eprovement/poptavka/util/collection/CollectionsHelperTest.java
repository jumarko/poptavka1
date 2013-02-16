/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.util.collection;

import com.eprovement.poptavka.domain.demand.Category;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CollectionsHelperTest {

    @Test
    public void testGetCollectionOfIds() throws Exception {

        final Collection<Long> categoriesIds = CollectionsHelper.getCollectionOfIds(
                Arrays.asList(createCategoryWithId(1), createCategoryWithId(2), createCategoryWithId(30)));

        assertThat(categoriesIds.size(), is(3));
        final Iterator<Long> idIter = categoriesIds.iterator();
        assertThat(idIter.next(), is(1L));
        assertThat(idIter.next(), is(2L));
        assertThat(idIter.next(), is(30L));

    }


    private Category createCategoryWithId(long id) {
        final Category category = new Category();
        category.setId(id);
        return category;
    }
}
