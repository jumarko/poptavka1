package cz.poptavka.sample.client.user.demands.develmodule.s.list;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.user.demands.widget.table.ColumnFactory;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.message.TableDisplay;

public class SupplierListGrid<T> extends DataGrid<T> {

    private ListDataProvider<T> dataProvider = new ListDataProvider<T>();
    private ListHandler<T> sortHandler = new ListHandler<T>(dataProvider.getList());


    public SupplierListGrid() {
        super();
        //insert widget to be displayed, where no relevant data are available
        this.setEmptyTableWidget(new HTML("<div style=\"text-align: center;\">No relevant data</div>"));
    }

    public SupplierListGrid(ProvidesKey<T> keyProvider) {
        super(keyProvider);
        //insert widget to be displayed, where no relevant data are available
        this.setEmptyTableWidget(new HTML("<div style=\"text-align: center;\">No relevant data</div>"));
        this.addColumnSortHandler(sortHandler);
        dataProvider.addDataDisplay(this);
    }

    public void setDataProvider(ListDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public ListDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    /**
     * Add the columns to the table.
     */
    public void initTableColumns(final SelectionModel<T> selectionModel) {

        ColumnFactory<T> factory = new ColumnFactory<T>();
        this.addColumn(factory.createCheckboxColumn((SelectionModel<T>) this.getSelectionModel()),
                SafeHtmlUtils.fromSafeConstant("<br/>"));
        this.setColumnWidth(getColumn(0), 40, Unit.PX);
        Column<T, ImageResource> col = factory.createStarColumn();
        this.addColumn(col, SafeHtmlUtils.fromSafeConstant("<br/>"));
        col.setFieldUpdater(new FieldUpdater<T, ImageResource>() {

            @Override
            public void update(int index, T object, ImageResource value) {
                TableDisplay obj = (TableDisplay) object;
                obj.setStarred(!obj.isStarred());
            }
        });
        this.setColumnWidth(getColumn(1), 40, Unit.PX);

        Column<T, String> titleCol = factory.createTitleColumn(sortHandler, true);
        this.addColumn(titleCol, Storage.MSGS.title());
//        addColumn(new TextCell(), "S", new GetValue<ImageResource>() {
//
//            @Override
//            public ImageResource getValue(PotentialDemandMessage object) {
//                TableDisplay obj = (TableDisplay) object;
//                if (obj.isStarred()) {
//                    return Storage.RSCS.images().starGold();
//                } else {
//                    return Storage.RSCS.images().starSilver();
//                }
//            }
//        }, null);

     // ClickableTextCell.
//        this.addColumn(new TextCell(), "T");


        /**
      // Checkbox column. This table will uses a checkbox column for selection.
      // Alternatively, you can call dataGrid.setSelectionEnabled(true) to enable
      // mouse selection.
      Column<ContactInfo, Boolean> checkColumn =
          new Column<ContactInfo, Boolean>(new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(ContactInfo object) {
              // Get the value from the selection model.
              return selectionModel.isSelected(object);
            }
          };
      dataGrid.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
      dataGrid.setColumnWidth(checkColumn, 40, Unit.PX);

      // First name.
      Column<ContactInfo, String> firstNameColumn =
          new Column<ContactInfo, String>(new EditTextCell()) {
            @Override
            public String getValue(ContactInfo object) {
              return object.getFirstName();
            }
          };
      firstNameColumn.setSortable(true);
      sortHandler.setComparator(firstNameColumn, new Comparator<ContactInfo>() {
        public int compare(ContactInfo o1, ContactInfo o2) {
          return o1.getFirstName().compareTo(o2.getFirstName());
        }
      });
      dataGrid.addColumn(firstNameColumn, constants.cwDataGridColumnFirstName());
      firstNameColumn.setFieldUpdater(new FieldUpdater<ContactInfo, String>() {
        public void update(int index, ContactInfo object, String value) {
          // Called when the user changes the value.
          object.setFirstName(value);
          ContactDatabase.get().refreshDisplays();
        }
      });
      dataGrid.setColumnWidth(firstNameColumn, 20, Unit.PCT);

      // Last name.
      Column<ContactInfo, String> lastNameColumn =
          new Column<ContactInfo, String>(new EditTextCell()) {
            @Override
            public String getValue(ContactInfo object) {
              return object.getLastName();
            }
          };
      lastNameColumn.setSortable(true);
      sortHandler.setComparator(lastNameColumn, new Comparator<ContactInfo>() {
        public int compare(ContactInfo o1, ContactInfo o2) {
          return o1.getLastName().compareTo(o2.getLastName());
        }
      });
      dataGrid.addColumn(lastNameColumn, constants.cwDataGridColumnLastName());
      lastNameColumn.setFieldUpdater(new FieldUpdater<ContactInfo, String>() {
        public void update(int index, ContactInfo object, String value) {
          // Called when the user changes the value.
          object.setLastName(value);
          ContactDatabase.get().refreshDisplays();
        }
      });
      dataGrid.setColumnWidth(lastNameColumn, 20, Unit.PCT);

      // Age.
      Column<ContactInfo, Number> ageColumn = new Column<ContactInfo, Number>(new NumberCell()) {
        @Override
        public Number getValue(ContactInfo object) {
          return object.getAge();
        }
      };
      lastNameColumn.setSortable(true);
      sortHandler.setComparator(ageColumn, new Comparator<ContactInfo>() {
        public int compare(ContactInfo o1, ContactInfo o2) {
          return o1.getBirthday().compareTo(o2.getBirthday());
        }
      });
      Header<String> ageFooter = new Header<String>(new TextCell()) {
        @Override
        public String getValue() {
          List<ContactInfo> items = dataGrid.getVisibleItems();
          if (items.size() == 0) {
            return "";
          } else {
            int totalAge = 0;
            for (ContactInfo item : items) {
              totalAge += item.getAge();
            }
            return "Avg: " + totalAge / items.size();
          }
        }
      };
*/
    }

    private interface GetValue<C> {
        C getValue(PotentialDemandMessage object);
    }


}
