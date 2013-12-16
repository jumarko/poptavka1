package com.eprovement.poptavka.client.catLocSelector.others;

import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorInstanceManager.PresentersInterface;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiRenderer;
import com.google.gwt.view.client.SetSelectionModel;

/**
 * The cell used to render categories.
 *
 * @author Martin
 */
public class CatLocCell extends AbstractCell<ICatLocDetail> {

    /**************************************************************************/
    /* UiRenderer                                                             */
    /**************************************************************************/
    private static CatLocCell.MyUiRenderer renderer = GWT.create(CatLocCell.MyUiRenderer.class);

    interface MyUiRenderer extends UiRenderer {

        void render(SafeHtmlBuilder sb, String imageCss, SafeUri imageSrc, SafeHtml categoryName);

        void onBrowserEvent(CatLocCell o, NativeEvent e, Element p, ICatLocDetail n);
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Display string Constants. **/
    public static final String LEFT_BRACE = "(";
    public static final String RIGHT_BRACE = ")";
    public static final String SPACE = " ";
    /** Class attributes. **/
    private SetSelectionModel<ICatLocDetail> selectionModel;
    private PresentersInterface presenter;

    /**************************************************************************/
    /* Constuctor                                                             */
    /**************************************************************************/
    /**
     * The html of the image used for contacts.
     */
    public CatLocCell(PresentersInterface presenter, SetSelectionModel<ICatLocDetail> selectionModel) {
        super("click");
        this.presenter = presenter;
        this.selectionModel = selectionModel;
    }

    /**************************************************************************/
    /* Override methods                                                       */
    /**************************************************************************/
    @Override
    public void render(Cell.Context context, ICatLocDetail value, SafeHtmlBuilder sb) {
        if (value != null) {
            final SafeHtml categoryString = prepareCategoryName(value);
            final SafeUri imageSource = prepareImageSource(value);
            final String imageStyle = prepareImageStyle(value);
            renderer.render(sb, imageStyle, imageSource, categoryString);
        }
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, ICatLocDetail value,
            NativeEvent event, ValueUpdater<ICatLocDetail> valueUpdater) {
        if ("click".equals(event.getType()) && presenter.getEventBus() != null) {
            switch (presenter.getBuilder().getWidgetType()) {
                case CatLocSelectorBuilder.WIDGET_TYPE_BROWSER:
                    cellBrowserClickHandler(value);
                    break;
                case CatLocSelectorBuilder.WIDGET_TYPE_TREE:
                    cellTreeClickHandler(value);
                    break;
                default:
                    break;
            }
            renderer.onBrowserEvent(this, event, parent, value);
        }
    }

    private void cellBrowserClickHandler(ICatLocDetail value) {
        switch (presenter.getBuilder().getCheckboxes()) {
            case CatLocSelectorBuilder.CHECKBOXES:
                manageSelectedItems(value);
                break;
            case CatLocSelectorBuilder.CHECKBOXES_ON_LEAF_ONLY:
                if (value.isLeaf()) {
                    manageSelectedItems(value);
                }
                break;
            default: //CatLocSelectorBuilder.CHECKBOXES_DISABLED
                break;
        }
    }

    private void cellTreeClickHandler(ICatLocDetail value) {
        presenter.getEventBus().requestHierarchy(
                presenter.getBuilder().getSelectorType(), value, presenter.getInstanceId());
        manageSelectedItems(value);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Toogle selection: if selected -> deselect and vice versa
     * @param clickedCategory
     */
    public void manageSelectedItems(ICatLocDetail clickedCategory) {
        if (selectionModel.getSelectedSet().contains(clickedCategory)) {
            selectionModel.setSelected(clickedCategory, false);
        } else {
            selectionModel.setSelected(clickedCategory, true);
        }
    }

    private SafeHtml prepareCategoryName(ICatLocDetail category) {
        SafeHtmlBuilder text = new SafeHtmlBuilder();

        text.appendEscaped(category.toString());
        switch (presenter.getBuilder().getDisplayCountsOfWhat()) {
            case CatLocSelectorBuilder.COUNTS_DEMANDS:
                text.appendEscaped(SPACE);
                text.appendEscaped(LEFT_BRACE);
                text.append(category.getDemandsCount());
                text.appendEscaped(RIGHT_BRACE);
                break;
            case CatLocSelectorBuilder.COUNTS_SUPPLIERS:
                text.appendEscaped(SPACE);
                text.appendEscaped(LEFT_BRACE);
                text.append(category.getSuppliersCount());
                text.appendEscaped(RIGHT_BRACE);
                break;
            default:
                //nothing by default
                break;
        }
        return text.toSafeHtml();
    }

    private SafeUri prepareImageSource(ICatLocDetail category) {
        //if rendering detail object is selected, render selected checbox.
        if (selectionModel != null && selectionModel.getSelectedSet().contains(category)) {
            return Storage.RSCS.images().checkboxSelected().getSafeUri();
        } else {
            return Storage.RSCS.images().checkbox().getSafeUri();
        }
    }

    private String prepareImageStyle(ICatLocDetail category) {
        switch (presenter.getBuilder().getCheckboxes()) {
            case CatLocSelectorBuilder.CHECKBOXES:
                return "";
            case CatLocSelectorBuilder.CHECKBOXES_ON_LEAF_ONLY:
                if (!category.isLeaf()) {
                    return "hidden";
                }
                return "";
            default: // -> CHECKBOXES_DISABLED:
                return "hidden";
        }
    }
}
