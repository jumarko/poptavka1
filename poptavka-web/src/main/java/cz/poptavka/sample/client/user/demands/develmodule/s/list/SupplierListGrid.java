package cz.poptavka.sample.client.user.demands.develmodule.s.list;

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
//        this.addColumn(factory.createCheckboxColumn((SelectionModel<T>) this.getSelectionModel()),
//                SafeHtmlUtils.fromSafeConstant("<br/>"));
//        this.setColumnWidth(getColumn(0), 40, Unit.PX);
        this.addColumn(factory.createStarColumn(), SafeHtmlUtils.fromSafeConstant("<br/>"));
//        this.setColumnWidth(getColumn(1), 40, Unit.PX);

        Column<T, String> col = factory.createTitleColumn(sortHandler, true);
        this.addColumn(col, Storage.MSGS.title());
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
      dataGrid.addColumn(ageColumn, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant(constants
          .cwDataGridColumnAge())), ageFooter);
      dataGrid.setColumnWidth(ageColumn, 7, Unit.EM);

      // Category.
      final Category[] categories = ContactDatabase.get().queryCategories();
      List<String> categoryNames = new ArrayList<String>();
      for (Category category : categories) {
        categoryNames.add(category.getDisplayName());
      }
      SelectionCell categoryCell = new SelectionCell(categoryNames);
      Column<ContactInfo, String> categoryColumn = new Column<ContactInfo, String>(categoryCell) {
        @Override
        public String getValue(ContactInfo object) {
          return object.getCategory().getDisplayName();
        }
      };
      dataGrid.addColumn(categoryColumn, constants.cwDataGridColumnCategory());
      categoryColumn.setFieldUpdater(new FieldUpdater<ContactInfo, String>() {
        public void update(int index, ContactInfo object, String value) {
          for (Category category : categories) {
            if (category.getDisplayName().equals(value)) {
              object.setCategory(category);
            }
          }
          ContactDatabase.get().refreshDisplays();
        }
      });
      dataGrid.setColumnWidth(categoryColumn, 130, Unit.PX);

      // Address.
      Column<ContactInfo, String> addressColumn = new Column<ContactInfo, String>(new TextCell()) {
        @Override
        public String getValue(ContactInfo object) {
          return object.getAddress();
        }
      };
      addressColumn.setSortable(true);
      sortHandler.setComparator(addressColumn, new Comparator<ContactInfo>() {
        public int compare(ContactInfo o1, ContactInfo o2) {
          return o1.getAddress().compareTo(o2.getAddress());
        }
      });
      dataGrid.addColumn(addressColumn, constants.cwDataGridColumnAddress());
      dataGrid.setColumnWidth(addressColumn, 60, Unit.PCT);
      */
    }



}
