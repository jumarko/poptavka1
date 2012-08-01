package com.eprovement.poptavka.client.common.category;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.view.client.SelectionModel;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.TreeViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link TreeViewModel} used to organize contacts into a hierarchy.
 */
public class CategoryTreeViewModel implements TreeViewModel {

    /**
     * The images used for this example.
     */
    interface Images extends ClientBundle {

        ImageResource contact();

        ImageResource contactsGroup();
    }

    /**
     * The cell used to render categories.
     */
    private static class CategoryCell extends AbstractCell<CategoryDetail> {

        /**
         * The html of the image used for contacts.
         */
        public CategoryCell() {
        }

        @Override
        public void render(Context context, CategoryDetail value, SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendEscaped(value.getName());
            }
        }
    }

    /**
     * Tracks the number of contacts in a category that begin with the same
     * letter.
     */
    private static class LetterCount implements Comparable<LetterCount> {

        private final CategoryDetail category;
        private final char firstLetter;
        private int count;

        /**
         * Construct a new {@link LetterCount} for one contact.
         *
         * @param category the category
         * @param firstLetter the first letter of the contacts name
         */
        public LetterCount(CategoryDetail category, char firstLetter) {
            this.category = category;
            this.firstLetter = firstLetter;
            this.count = 1;
        }

        @Override
        public int compareTo(LetterCount o) {
            return (o == null) ? -1 : (firstLetter - o.firstLetter);
        }

        @Override
        public boolean equals(Object o) {
            return compareTo((LetterCount) o) == 0;
        }

        @Override
        public int hashCode() {
            return firstLetter;
        }

        /**
         * Increment the count.
         */
        public void increment() {
            count++;
        }
    }

    /**
     * A Cell used to render the LetterCount.
     */
    private static class CategoryCountCell extends AbstractCell<CategoryDetail> {

        @Override
        public void render(Context context, CategoryDetail value, SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendEscaped(value.getName());
//                sb.append(" (").append(value.count).append(")");//ziskaj pocet
            }
        }
    }
    /**
     * The static images used in this model.
     */
    private Cell<CategoryDetail> categoryCell = null;
    private final SelectionModel<CategoryDetail> selectionModel;
    private final DefaultSelectionEventManager<CategoryDetail> selectionManager =
            DefaultSelectionEventManager.createCheckboxManager();
    private CategoryRPCServiceAsync categoryService;
    /**
     * Check boxes usage.
     * True - all levels use check box for item selection
     * False - only last level use check box for item selection
     */
    private int checkboxes = -1;

    public CategoryTreeViewModel(final SelectionModel<CategoryDetail> selectionModel,
            CategoryRPCServiceAsync categoryService, int checkboxesUsage) {
        this.selectionModel = selectionModel;
        this.categoryService = categoryService;
        this.checkboxes = checkboxesUsage;

        List<HasCell<CategoryDetail, ?>> hasCells = new ArrayList<HasCell<CategoryDetail, ?>>();
        hasCells.add(new HasCell<CategoryDetail, Boolean>() {
            private CheckboxCell cell = new CheckboxCell(true, false);

            @Override
            public Cell<Boolean> getCell() {
                return cell;
            }

            @Override
            public FieldUpdater<CategoryDetail, Boolean> getFieldUpdater() {
                return null;
            }

            @Override
            public Boolean getValue(CategoryDetail object) {
                return selectionModel.isSelected(object);
            }
        });
        hasCells.add(new HasCell<CategoryDetail, CategoryDetail>() {
            private CategoryCell cell = new CategoryCell();

            @Override
            public Cell<CategoryDetail> getCell() {
                return cell;
            }

            @Override
            public FieldUpdater<CategoryDetail, CategoryDetail> getFieldUpdater() {
                return null;
            }

            @Override
            public CategoryDetail getValue(CategoryDetail object) {
                return object;
            }
        });
        categoryCell = new CompositeCell<CategoryDetail>(hasCells) {

            @Override
            public void render(Context context, CategoryDetail value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<table><tbody><tr>");
                super.render(context, value, sb);
                sb.appendHtmlConstant("</tr></tbody></table>");
            }

            @Override
            protected Element getContainerElement(Element parent) {
                // Return the first TR element in the table.
                return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
            }

            /**
             * Render items of hashcell. Define whether to render also checkbox or
             * only text.
             */
            @Override
            protected <X> void render(Context context, CategoryDetail value,
                    SafeHtmlBuilder sb, HasCell<CategoryDetail, X> hasCell) {
                Cell<X> cell = hasCell.getCell();
                sb.appendHtmlConstant("<td>");
                switch (checkboxes) {
                    case Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS:
                        if (cell instanceof CheckboxCell) {
                            //Ak je checkbox, renderuj, len ak je to list
                            if (value.isLeaf()) {
                                cell.render(context, hasCell.getValue(value), sb);
                            }
                        } else {
                            cell.render(context, hasCell.getValue(value), sb);
                        }
                        break;
                    default:
                        cell.render(context, hasCell.getValue(value), sb);
                        break;

                }
                sb.appendHtmlConstant("</td>");
            }
        };
    }

    /**
     * Then inside getNodeInfo(T value) of your TreeViewModel just return a new
     * DeafultNodeInfo with a new MyDataProvider. It defines type what cells to use
     * in each level. In this way NodeInfo is returned synchronously, but the data provider
     * updates itself asynchronously.
     * @param <T>
     * @param value
     * @return
     */
    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {
        CategoryDetail detail = (CategoryDetail) value;
        switch (checkboxes) {
            case Constants.WITH_CHECK_BOXES:
                CategoryDataProvider dataProvider = new CategoryDataProvider(detail, categoryService);
                return new DefaultNodeInfo(dataProvider, categoryCell, selectionModel, selectionManager, null);
            case Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS:
                CategoryDataProvider dataProvider1 = new CategoryDataProvider(detail, categoryService);
                return new DefaultNodeInfo(dataProvider1, categoryCell, selectionModel, selectionManager, null);
            default:
                CategoryDataProvider dataProvider2 = new CategoryDataProvider(detail, categoryService);
                return new DefaultNodeInfo(dataProvider2, new CategoryCell());
        }
    }

    @Override
    public boolean isLeaf(Object value) {
        if (value == null) {
            return false;
        } else {
            return ((CategoryDetail) value).isLeaf();
        }
    }
}