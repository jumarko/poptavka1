package cz.poptavka.sample.client.user.demands.widgets;

public class MyDemandsInfo {

    private String name;
    private String price;

    public MyDemandsInfo(String name, String price) {
        this.setName(name);
        this.setPrice(price);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }
}
