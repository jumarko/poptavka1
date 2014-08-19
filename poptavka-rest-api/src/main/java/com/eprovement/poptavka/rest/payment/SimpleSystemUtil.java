package com.eprovement.poptavka.rest.payment;

public class SimpleSystemUtil implements SystemUtil {
    private boolean production;

    @Override
    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }
}
