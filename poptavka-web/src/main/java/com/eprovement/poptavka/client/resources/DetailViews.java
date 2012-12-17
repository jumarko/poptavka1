/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 *
 * @author slavkovsky.martin, Jaro
 */
public interface DetailViews extends CssResource {

    @ClassName("demandDetailTable")
    String demandDetailTable();

    @ClassName("detail-block")
    String detailBlock();

    @ClassName("detail-header")
    String detailHeader();

    @ClassName("demand-detail-content")
    String demandDetailContent();

    @ClassName("detail-block-label")
    String detailBlockLabel();

    @ClassName("detail-category-label")
    String detailCategoryLabel();

    @ClassName("detail-locality-label")
    String detailLocalityLabel();

    @ClassName("demandDetailSectionHeader")
    String demandDetailSectionHeader();

    @ClassName("supplierDetailTable")
    String supplierDetailTable();

    @ClassName("supplierDetailSectionHeader")
    String supplierDetailSectionHeader();
}
