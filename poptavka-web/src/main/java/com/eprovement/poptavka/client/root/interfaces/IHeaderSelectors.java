package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Selector;
import com.google.gwt.query.client.Selectors;

/**
 * Selectors for Login view ui elements.
 *
 * @author Martin Slavkovsky
 */
public interface IHeaderSelectors extends Selectors {

    @Selector("#gwt-debug-menuPanel")
    GQuery getMenuPanel();

    @Selector("#gwt-debug-searchElement")
    GQuery getSearchElement();
}
