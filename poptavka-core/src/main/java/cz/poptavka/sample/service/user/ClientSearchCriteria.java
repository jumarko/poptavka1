package cz.poptavka.sample.service.user;

/**
 * @author Juraj Martinka
 *         Date: 8.1.11
 */
public class ClientSearchCriteria {
    private String name;
    private String surName;


    public ClientSearchCriteria(String name, String surName) {
        this.name = name;
        this.surName = surName;
    }


    public String getName() {
        return name;
    }

    public ClientSearchCriteria setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurName() {
        return surName;
    }

    public ClientSearchCriteria setSurName(String surName) {
        this.surName = surName;
        return this;
    }
}
