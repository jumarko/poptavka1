/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 *
 * @author slavkovsky.martin
 */
public interface DetailViews extends CssResource {

    @ClassName("demandDetailTable")
    String demandDetailTable();

    @ClassName("demandDetailSectionHeader")
    String demandDetailSectionHeader();

    @ClassName("supplierDetailTable")
    String supplierDetailTable();

    @ClassName("supplierDetailSectionHeader")
    String supplierDetailSectionHeader();
}
