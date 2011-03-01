package cz.poptavka.sample.shared.domain;

import java.io.Serializable;

public class CategoryDetail implements Serializable {

    //TODO change id to code when change on server side is made
    private long id;
    private String name;
    private int demands;
    private int suppliers;

    public CategoryDetail() {
    }

    public CategoryDetail(Long id, String name, int demands, int suppliers) {
        this.id = id;
        this.name = name;
        this.demands = demands;
        this.suppliers = suppliers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getDemands() {
        return demands;
    }

    public void setDemands(int demands) {
        this.demands = demands;
    }

    public int getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(int suppliers) {
        this.suppliers = suppliers;
    }

}
